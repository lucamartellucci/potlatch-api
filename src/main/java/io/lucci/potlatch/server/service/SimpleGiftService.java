package io.lucci.potlatch.server.service;

import io.lucci.potlatch.server.persistence.model.GiftDBTO;
import io.lucci.potlatch.server.persistence.model.UserActionDBTO;
import io.lucci.potlatch.server.persistence.model.UserActionIdDBTO;
import io.lucci.potlatch.server.persistence.repository.GiftDBTORepository;
import io.lucci.potlatch.server.persistence.repository.UserActionDBTORepository;
import io.lucci.potlatch.server.service.exception.GiftNotFoundExcetption;
import io.lucci.potlatch.server.service.exception.GiftServiceException;
import io.lucci.potlatch.server.web.model.Gift;
import io.lucci.potlatch.server.web.model.Gift.GiftStatus;
import io.lucci.potlatch.server.web.model.PaginatorResult;
import io.lucci.potlatch.server.web.model.SimplePaginator;
import io.lucci.potlatch.server.web.model.User;
import io.lucci.potlatch.server.web.model.adapter.GiftAdapter;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriTemplate;

@Service
public class SimpleGiftService implements GiftService {
	
	private static Logger logger = LoggerFactory.getLogger(SimpleGiftService.class);
	
	@Autowired
	private GiftDBTORepository giftRepository;
	
	@Autowired
	private UserActionDBTORepository userActionRepository;
	
	@Autowired
	private GiftAdapter giftAdapter;

	@Value("${server.path}")
	private String serverPath;

	@Override 
	public Gift getGiftByUuid(String uuid) throws GiftServiceException, GiftNotFoundExcetption {
		try {
			
			GiftDBTO giftDBTO = giftRepository.findByUuid(uuid);
			if (giftDBTO == null) {
				throw new GiftNotFoundExcetption(new StringBuilder("Unable to find the gift with id [").append(uuid).append("]").toString());
			}
			
			// adapt the gift
			Gift gift = giftAdapter.dbtoToTo(giftDBTO, false);
			return gift;
			
		} catch (GiftNotFoundExcetption e) {
			throw e;
		} catch (Exception e) {
			throw new GiftServiceException(new StringBuilder("Unable to find the gift with id [").append(uuid).append("]").toString(), e);
		}
	}

	@Override
	public PaginatorResult<Gift> loadGifts(User user, SimplePaginator paginator) throws GiftServiceException {
		try {
			Pageable p = new PageRequest(paginator.getPage(), paginator.getSize());
			
			logger.info("Loading gifts for user [{}]", user.getId());
			logger.info("Block inappropriate [{}]", user.getBlockInappropriate());

			Page<GiftDBTO> page = null;
			logger.info("Loading page [], with size []", p.getPageNumber(), p.getPageSize());
			if (user.getBlockInappropriate()!= null && user.getBlockInappropriate().booleanValue()) {
				page = giftRepository.findAllByUserIdFilterInappropriate(user.getId(),p);
			} else {
				page = giftRepository.findAllByUserId(user.getId(), p);
			}
			
			PaginatorResult<Gift> result = new PaginatorResult<>();
			
			List<Gift> adaptedGifts = giftAdapter.dbtoToTo(page.getContent(), true);
			result.setResult(adaptedGifts);
			result.setTotalPages(page.getTotalPages());
			result.setPageSize(page.getSize());
			result.setCurrentPage(page.getNumber());
			return result;
			
		} catch (Exception e) {
			throw new GiftServiceException(new StringBuilder("Unable to retrieve gifts for user [").append(user.getId()).append("]").toString(),e);
		}
	}
	
	private List<Gift> findAllGifts(User user) throws GiftServiceException {
		try {
			logger.info("Loading gifts for user [{}]", user.getId());
			logger.info("Block inappropriate [{}]", user.getBlockInappropriate());
			
			List<GiftDBTO> gifts = null;
			if (user.getBlockInappropriate()!= null && user.getBlockInappropriate().booleanValue()) {
				gifts = giftRepository.findAllByUserIdFilterInappropriate(user.getId());
			} else {
				gifts = giftRepository.findAllByUserId(user.getId());
			}
			
			return giftAdapter.dbtoToTo(gifts, true);
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

	@Override
	public Gift createGift(Gift gift, String parentUuid, User user) throws GiftServiceException, GiftNotFoundExcetption {
		
		try {
			
			Long parentId = null;
			if (StringUtils.isEmpty(parentUuid)) {
				logger.info("Create the new toplevel gift: {} for user: {}", gift, user);
			} else {
				logger.info("Create the new child gift: {} for user: {}", gift, user);
				logger.info("Search the parent gift with id [{}]", parentUuid);
				GiftDBTO giftDBTO = giftRepository.findByUuid(parentUuid);
				if (giftDBTO == null) {
					throw new GiftNotFoundExcetption(new StringBuilder("Unable to find the gift with id [").append(parentUuid).append("]").toString());
				} else {
					parentId = giftDBTO.getId();
					logger.debug("Parent found! parentId is [{}]",giftDBTO.getId());
				}
			}
			
			final String uuid = UUID.randomUUID().toString();
			final String uri = new UriTemplate("{SERVER_PATH}/api/v1/gift/{UUID}/data").expand(serverPath, uuid).toString();
			logger.info("Create gift with id [{}] and uri [{}]", uuid);
			
			GiftDBTO giftDBTO = giftAdapter.toToDbto(gift, user);
			giftDBTO.setUuid(uuid);
			giftDBTO.setUri(uri);
			giftDBTO.setTimestamp(new Date());
			giftDBTO.setParentId(parentId);
			giftDBTO.setStatus(GiftStatus.ready_for_upload.toString());
			
			
			GiftDBTO savedGiftDBTO = giftRepository.save(giftDBTO);
			return giftAdapter.dbtoToTo(savedGiftDBTO, false);
			
		} catch (GiftNotFoundExcetption e) {	
			throw e;
		} catch (Exception e) {
			throw new GiftServiceException("Unable to create the gift",e);
		}
	}
	
	@Override
	public Gift updateGiftStatus(String uuid, GiftStatus status) throws GiftServiceException, GiftNotFoundExcetption {
		try {
			logger.debug("Update gift status to: {} for gift: {}", status, uuid);
			
			GiftDBTO giftDBTO = giftRepository.findByUuid(uuid);
			if (giftDBTO == null) {
				throw new GiftNotFoundExcetption("Unable to find gift with uuid {}" + uuid);
			}
			
			giftDBTO.setStatus(status.toString());
			GiftDBTO savedGiftDBTO = giftRepository.save(giftDBTO);
			return giftAdapter.dbtoToTo(savedGiftDBTO, false);
			
		} catch (Exception e) {
			throw new GiftServiceException("Unable to update the gift",e);
		}
	}
	
	private List<Gift> findAllGifts(String parentUuid, User user) throws GiftServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PaginatorResult<Gift> loadChain(String parentUuid, User user, SimplePaginator paginator) throws GiftServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	/* ************************************** */
	/* PRIVATE METHODS                        */
	/* ************************************** */
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
