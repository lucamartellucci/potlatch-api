package io.lucci.potlatch.service;

import io.lucci.potlatch.model.Gift;
import io.lucci.potlatch.model.User;
import io.lucci.potlatch.persistence.model.GiftDBTO;
import io.lucci.potlatch.persistence.model.UserActionDBTO;
import io.lucci.potlatch.persistence.model.UserActionIdDBTO;
import io.lucci.potlatch.persistence.repository.GiftDBTORepository;
import io.lucci.potlatch.persistence.repository.UserActionDBTORepository;
import io.lucci.potlatch.service.adapter.GiftAdapter;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SimpleGiftService implements GiftService {
	
	private static Logger logger = LoggerFactory.getLogger(SimpleGiftService.class);
	
	@Autowired
	private GiftDBTORepository giftRepository;
	
	@Autowired
	private UserActionDBTORepository userActionRepository;
	
	@Autowired
	private GiftAdapter giftAdapter;

	@Transactional @Override public Gift getGiftByUuid(String uuid) throws GiftServiceException {
		GiftDBTO giftDBTO = giftRepository.findByUuid(uuid);
		Gift gift = giftAdapter.dbtoToTo(giftDBTO, true);
		return gift;
	}



	@Override
	public List<Gift> findAllGifts(User user, Pageable p) throws GiftServiceException {
		try {
			logger.info("Loading gifts for user []", user.getId());
			logger.info("Block inappropriate []", user.getBlockInappropriate());
			List<GiftDBTO> gifts = null;
			if (p != null) {
				Page<GiftDBTO> page = null;
				logger.info("Loading page [], with size []", p.getPageNumber(), p.getPageSize());
				if (user.getBlockInappropriate()!= null && user.getBlockInappropriate().booleanValue()) {
					page = giftRepository.findAllByUserIdFilterInappropriate(user.getId(),p);
				} else {
					page = giftRepository.findAllByUserId(user.getId(), p);
				}
				gifts = page.getContent();
			} else {
				
				logger.info("Loading full list");
				if (user.getBlockInappropriate()!= null && user.getBlockInappropriate().booleanValue()) {
					gifts = giftRepository.findAllByUserIdFilterInappropriate(user.getId());
				} else {
					gifts = giftRepository.findAllByUserId(user.getId());
				}
			}
			return giftAdapter.dbtoToTo(gifts, false);
		} catch (Exception e) {
			throw new GiftServiceException(new StringBuilder("Unable to retrieve gifts for user [").append(user.getId()).append("]").toString(),e);
		}
	}



	@Override
	public Long likeGift(String uuid, Long userId) throws GiftServiceException, GiftNotFoundExcetption {
		
		try {
			
			GiftDBTO giftDBTO = giftRepository.findByUuid(uuid);
			if (giftDBTO == null) {
				throw new GiftNotFoundExcetption(new StringBuilder("Unable to find the gift with uuid = ").append(uuid).toString());
			}
			
			UserActionDBTO userActionDBTO = userActionRepository.findOne(new UserActionIdDBTO(userId,giftDBTO.getId()));
			if (userActionDBTO != null) {
				if (alreadyLikes(userActionDBTO)) {
					return giftDBTO.getNumberOfLikes();
				} else {
					userActionDBTO.setLike(Boolean.TRUE);
					userActionRepository.save(userActionDBTO);
					return incrementGiftPrefCounter(giftDBTO);
				}
			} else {
				userActionRepository.save(new UserActionDBTO(userId,giftDBTO.getId(),Boolean.TRUE,null));
				return incrementGiftPrefCounter(giftDBTO);
			}
		} catch (GiftNotFoundExcetption e) {	
			throw e;
		} catch (Exception e) {
			throw new GiftServiceException("Unable to increment like counter",e);
		}
	}

	@Override
	public Long unlikeGift(String uuid, Long userId) throws GiftServiceException, GiftNotFoundExcetption {
		// TODO Auto-generated method stub
		return null;
	}


	
	
	private Long incrementGiftPrefCounter(GiftDBTO giftDBTO) {
		Long numberOfLikes = giftDBTO.getNumberOfLikes();
		logger.debug("Current likes: {}", numberOfLikes);
		
		if (numberOfLikes == null) {
			numberOfLikes = 0L;
		}
		giftDBTO.setNumberOfLikes(++numberOfLikes);
		giftRepository.save(giftDBTO);
		return numberOfLikes;
	}

	private boolean alreadyLikes(UserActionDBTO userAction) {
		if (userAction.getLike() != null && userAction.getLike().booleanValue()) {
			return true;
		}
		return false;
	}

}
