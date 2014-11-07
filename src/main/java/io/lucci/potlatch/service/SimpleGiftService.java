package io.lucci.potlatch.service;

import io.lucci.potlatch.persistence.model.GiftDBTO;
import io.lucci.potlatch.persistence.model.UserActionDBTO;
import io.lucci.potlatch.persistence.model.UserActionIdDBTO;
import io.lucci.potlatch.persistence.repository.GiftDBTORepository;
import io.lucci.potlatch.persistence.repository.UserActionDBTORepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SimpleGiftService implements GiftService {
	
	private static Logger logger = LoggerFactory.getLogger(SimpleGiftService.class);
	
	@Autowired
	private GiftDBTORepository giftRepository;
	
	@Autowired
	private UserActionDBTORepository userActionRepository;

	@Override
	public Long likeGift(Long giftId, Long userId) throws GiftServiceException {
		
		try {
			
			UserActionDBTO userAction = userActionRepository.findOne(new UserActionIdDBTO(userId,giftId));
			if (userAction != null) {
				if (alreadyLikes(userAction)) {
					GiftDBTO gift = giftRepository.findOne(giftId);
					return gift.getNumberOfLikes();
				} else {
					userAction.setLike(Boolean.TRUE);
					userActionRepository.save(userAction);
					return incrementGiftPrefCounter(giftId);
				}
			} else {
				userActionRepository.save(new UserActionDBTO(userId,giftId,Boolean.TRUE,null));
				return incrementGiftPrefCounter(giftId);
			}
			
		} catch (Exception e) {
			throw new GiftServiceException("Unable to increment like counter",e);
		}
	}

	private Long incrementGiftPrefCounter(Long giftId) {
		GiftDBTO gift = giftRepository.findOne(giftId);
		Long numberOfLikes = gift.getNumberOfLikes();
		logger.debug("Current likes: {}", numberOfLikes);
		
		if (numberOfLikes == null) {
			numberOfLikes = 0L;
		}
		gift.setNumberOfLikes(++numberOfLikes);
		giftRepository.save(gift);
		return numberOfLikes;
	}

	private boolean alreadyLikes(UserActionDBTO userAction) {
		if (userAction.getLike() != null && userAction.getLike().booleanValue()) {
			return true;
		}
		return false;
	}

	@Override
	public Long unlikeGift(Long giftId, Long userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GiftDBTO getGiftById(Long giftId) throws GiftServiceException {
		GiftDBTO gift = giftRepository.findOne(giftId);
		return gift;
	}

}
