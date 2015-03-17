package io.lucci.potlatch.server.service;

import io.lucci.potlatch.server.persistence.model.GiftDBTO;
import io.lucci.potlatch.server.persistence.model.UserActionDBTO;
import io.lucci.potlatch.server.persistence.model.UserActionIdDBTO;
import io.lucci.potlatch.server.persistence.repository.GiftDBTORepository;
import io.lucci.potlatch.server.persistence.repository.UserActionDBTORepository;
import io.lucci.potlatch.server.service.StorageService.StorageAction;
import io.lucci.potlatch.server.service.exception.GiftManagerException;
import io.lucci.potlatch.server.service.exception.GiftNotChainExcetption;
import io.lucci.potlatch.server.service.exception.GiftNotFoundExcetption;
import io.lucci.potlatch.server.web.model.Gift;
import io.lucci.potlatch.server.web.model.Gift.GiftStatus;
import io.lucci.potlatch.server.web.model.PaginatorResult;
import io.lucci.potlatch.server.web.model.SimplePaginator;
import io.lucci.potlatch.server.web.model.User;
import io.lucci.potlatch.server.web.model.adapter.GiftAdapter;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class GiftManagerImpl implements GiftManager {
	
	private static final Logger logger = LoggerFactory.getLogger(GiftManagerImpl.class);

	@Autowired
	private StorageService storageService;
	
	@Autowired
	private GiftDBTORepository giftRepository;
	
	@Autowired
	private UserActionDBTORepository userActionRepository;
	
	@Autowired
	private GiftAdapter giftAdapter;

	@Override 
	public Gift getGiftById(Long id) throws GiftManagerException, GiftNotFoundExcetption {
		try {
			
			GiftDBTO giftDBTO = giftRepository.findOne(id);
			if (giftDBTO == null) {
				throw new GiftNotFoundExcetption(new StringBuilder("Unable to find the gift with id [").append(id).append("]").toString());
			}
			
			// adapt the gift
			Gift gift = giftAdapter.dbtoToTo(giftDBTO, false);
			// build and set the gift image URL
			gift.setImageUrl(storageService.buildGiftUrl(StorageAction.READ, giftDBTO.getUuid()).toString());
			return gift;
			
		} catch (GiftNotFoundExcetption e) {
			throw e;
		} catch (Exception e) {
			throw new GiftManagerException(new StringBuilder("Unable to find the gift with id [").append(id).append("]").toString(), e);
		}
	}

	@Override
	public PaginatorResult<Gift> getGifts(User user, SimplePaginator paginator) throws GiftManagerException {
		try {
			logger.info("Loading gifts for user [{}] with block inappropriate flag set to [{}]", user.getId(), user.getBlockInappropriate());
	
			Pageable p = buildPageable(paginator);
			if (p != null){
				return loadGifts(user, p);
			} else {
				List<Gift> gifts = loadGifts(user);
				PaginatorResult<Gift> result = new PaginatorResult<>();
				result.setCurrentPage(1);
				result.setTotalPages(1);
				result.setPageSize(gifts.size());
				result.setResult(gifts);
				return result;
			}
		} catch (Exception e) {
			throw new GiftManagerException(new StringBuilder("Unable to retrieve gifts for user [").append(user.getId()).append("]").toString(),e);
		}
	}

	@Override
	public PaginatorResult<Gift> getGiftChain(Long chainUuid, User user, SimplePaginator paginator) throws GiftManagerException,
			GiftNotFoundExcetption, GiftNotChainExcetption, StorageServiceException {
		throw new RuntimeException("Not yet implemented");
	}

	@Override
	public Gift createGift(Gift gift, Long parentId, User user) throws GiftManagerException, GiftNotFoundExcetption {
		
		try {
			
			if (parentId == null) {
				logger.info("Create the new toplevel gift: {} for user: {}", gift, user);
			} else {
				logger.info("Create the new gift: {} with parentId {} for user: {}", new Object[]{gift, parentId, user});
				logger.info("Search the parent gift with id [{}]", parentId);
				GiftDBTO giftDBTO = giftRepository.findOne(parentId);
				if (giftDBTO == null) {
					throw new GiftNotFoundExcetption(new StringBuilder("Unable to find the gift with id [").append(parentId).append("]").toString());
				} 
			}
			
			GiftDBTO giftDBTO = giftAdapter.toToDbto(gift, user);
			giftDBTO.setTimestamp(new Date());
			giftDBTO.setParentId(parentId);
			giftDBTO.setStatus(GiftStatus.ready_for_upload.toString());
			giftDBTO.setUuid(UUID.randomUUID().toString());
			
			GiftDBTO savedGiftDBTO = giftRepository.save(giftDBTO);
			return giftAdapter.dbtoToTo(savedGiftDBTO, false);
			
		} catch (GiftNotFoundExcetption e) {	
			throw e;
		} catch (Exception e) {
			throw new GiftManagerException("Unable to create the gift",e);
		}
	}

	@Override
	public Gift updateGiftStatus(Long id, GiftStatus status) throws GiftManagerException, GiftNotFoundExcetption {
		try {
			logger.debug("Update gift status to: {} for gift: {}", status, id);
			
			GiftDBTO giftDBTO = giftRepository.findOne(id);
			if (giftDBTO == null) {
				throw new GiftNotFoundExcetption("Unable to find gift with uuid {}" + id);
			}
			
			giftDBTO.setStatus(status.toString());
			GiftDBTO savedGiftDBTO = giftRepository.save(giftDBTO);
			return giftAdapter.dbtoToTo(savedGiftDBTO, false);
			
		} catch (Exception e) {
			throw new GiftManagerException("Unable to update the gift",e);
		}
	}

	@Override
	public Gift setGiftData(Long id, InputStream data) throws GiftManagerException, GiftNotFoundExcetption, StorageServiceException {
		
		// search the gift
		logger.info("Storing data for Gift [{}]", id);
		GiftDBTO giftDBTO = giftRepository.findOne(id);
		if (giftDBTO == null) {
			throw new GiftNotFoundExcetption(new StringBuilder("Unable to find the gift with id [").append(id).append("]").toString());
		}
		
		if (!GiftStatus.ready_for_upload.equals(GiftStatus.valueOf(giftDBTO.getStatus()))) {
			throw new GiftManagerException(new StringBuffer("Unable to store the image. The current status of the gift [").append(giftDBTO.getId()).append("]").append(" is [").append(giftDBTO.getStatus()).append("]").toString() );
		}
		
		// write the data into the object storage
   		storageService.storeGiftData(data, giftDBTO.getUuid());
		 
   		// updates the status of the gift
   		logger.debug("Update the status of the gift [{}] to: [{}]", GiftStatus.active, id);
		Gift result = updateGiftStatus(id, GiftStatus.active);
		result.setImageUrl(storageService.buildGiftUrl(StorageAction.READ, giftDBTO.getUuid()).toString());
		return result;
	}

	@Override
	public InputStream getGiftData(Long id) throws GiftManagerException, GiftNotFoundExcetption, StorageServiceException {
		
		// search the gift
		logger.info("Reading data for Gift [{}]", id);
		Gift gift = getGiftById(id);
		logger.debug("Gift [{}] found!", id);
		logger.debug("Gift name is [{}]", gift.getTitle());
		
		if (!GiftStatus.active.equals(GiftStatus.valueOf(gift.getStatus()))) {
			throw new GiftManagerException(new StringBuffer("Unable to read the image. The current status of the gift [").append(gift.getId()).append("]").append(" is [").append(gift.getStatus()).append("]").toString() );
		}
		
		return storageService.loadGiftData(gift.getImageFilename());
	}

	@Override
	public Long likeGift(Long id, Long userId) throws GiftManagerException, GiftNotFoundExcetption {
		
		try {
			
			GiftDBTO giftDBTO = giftRepository.findOne(id);
			if (giftDBTO == null) {
				throw new GiftNotFoundExcetption(new StringBuilder("Unable to find the gift with uuid = ").append(id).toString());
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
			throw new GiftManagerException("Unable to increment like counter",e);
		}
	}

	@Override
	public Long unlikeGift(Long id, Long userId) throws GiftManagerException,
			GiftNotFoundExcetption {
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
	
	private PaginatorResult<Gift> loadGifts(User user, Pageable p) throws StorageServiceException {
		Page<GiftDBTO> page = null;
		
		logger.info("Loading page [], with size []", p.getPageNumber(), p.getPageSize());
		if (user.getBlockInappropriate()!= null && user.getBlockInappropriate().booleanValue()) {
			page = giftRepository.findAllByUserIdRemoveInappropriate(user.getId(), p);
		} else {
			page = giftRepository.findAllByUserId(user.getId(), p);
		}
		
		List<Gift> adaptedGifts = new ArrayList<>();
		for (GiftDBTO giftDBTO : page.getContent()) {
			Gift gift = giftAdapter.dbtoToTo(giftDBTO, true);
			gift.setImageFilename(storageService.buildGiftUrl(StorageAction.READ, giftDBTO.getUuid()).toString());
			adaptedGifts.add(gift);
		}
		
		PaginatorResult<Gift> result = new PaginatorResult<>();
		result.setResult(adaptedGifts);
		result.setTotalPages(page.getTotalPages());
		result.setPageSize(page.getSize());
		result.setCurrentPage(page.getNumber());
		return result;
	}
	

	private List<Gift> loadGifts(User user) throws GiftManagerException, StorageServiceException {
		logger.info("Loading all gifts");
		List<Gift> result = new ArrayList<>();
		List<GiftDBTO> giftDBTOs = null;
		if (user.getBlockInappropriate()!= null && user.getBlockInappropriate().booleanValue()) {
			giftDBTOs = giftRepository.findAllByUserIdRemoveInappropriate(user.getId());
		} else {
			giftDBTOs = giftRepository.findAllByUserId(user.getId());
		}
		
		for (GiftDBTO giftDBTO : giftDBTOs) {
			Gift gift = giftAdapter.dbtoToTo(giftDBTO, true);
			gift.setImageFilename(storageService.buildGiftUrl(StorageAction.READ, giftDBTO.getUuid()).toString());
			result.add(gift);
		}
		
		return result;
	}
	
	private Pageable buildPageable(SimplePaginator paginator) {
		if (paginator != null) {
			return new PageRequest(paginator.getPage(), paginator.getSize());
		}
		return null;
	}
	
	private Long checkChainUuid(Long chainId)
			throws GiftManagerException, GiftNotFoundExcetption,
			GiftNotChainExcetption {
		Gift chainMasterGift = getGiftById(chainId);
		if (chainMasterGift == null) {
			throw new GiftNotFoundExcetption(new StringBuilder("Unable to find the Chain with id [").append(chainId).append("]").toString());
		}
		if (!chainMasterGift.getChainMaster()) {
			throw new GiftNotChainExcetption(new StringBuilder("The Gift with id [").append(chainId).append("] is not a Chain").toString());
		}
		
		return chainId;
	}
	
}
