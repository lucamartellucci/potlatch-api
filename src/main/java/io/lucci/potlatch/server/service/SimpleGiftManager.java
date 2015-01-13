package io.lucci.potlatch.server.service;

import io.lucci.potlatch.server.service.StorageService.StorageAction;
import io.lucci.potlatch.server.service.exception.GiftNotChainExcetption;
import io.lucci.potlatch.server.service.exception.GiftNotFoundExcetption;
import io.lucci.potlatch.server.service.exception.GiftServiceException;
import io.lucci.potlatch.server.web.model.Gift;
import io.lucci.potlatch.server.web.model.Gift.GiftStatus;
import io.lucci.potlatch.server.web.model.PaginatorResult;
import io.lucci.potlatch.server.web.model.SimplePaginator;
import io.lucci.potlatch.server.web.model.User;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SimpleGiftManager implements GiftManager {
	
	private static final Logger logger = LoggerFactory.getLogger(SimpleGiftManager.class);

	@Autowired
	private StorageService storageService;
	
	@Autowired
	private GiftService giftService;
	
	@Override
	public Gift setGiftData(String uuid, InputStream data) throws GiftServiceException, GiftNotFoundExcetption, StorageServiceException {
		
		// search the gift
		logger.info("Storing data for Gift [{}]", uuid);
		Gift gift = giftService.getGiftByUuid(uuid);
		logger.debug("Gift [{}] found!", uuid);
		logger.debug("Gift name is [{}]", gift.getTitle());
		
		if (!GiftStatus.ready_for_upload.equals(GiftStatus.valueOf(gift.getStatus()))) {
			throw new GiftServiceException(new StringBuffer("Unable to store the image. The current status of the gift [").append(gift.getUuid()).append("]").append(" is [").append(gift.getStatus()).append("]").toString() );
		}
		
		// write the data into the object storage
   		storageService.storeGiftData(data, buildObjectName(uuid));
		 
   		// updates the status of the gift
   		logger.debug("Update the status of the gift [{}] to: [{}]", GiftStatus.active, uuid);
		Gift result = giftService.updateGiftStatus(uuid, GiftStatus.active);
		return result;
		
	}

	@Override
	public InputStream getGiftData(String uuid) throws GiftServiceException, GiftNotFoundExcetption, StorageServiceException {
		
		// search the gift
		logger.info("Reading data for Gift [{}]", uuid);
		Gift gift = giftService.getGiftByUuid(uuid);
		logger.debug("Gift [{}] found!", uuid);
		logger.debug("Gift name is [{}]", gift.getTitle());
		
		if (!GiftStatus.active.equals(GiftStatus.valueOf(gift.getStatus()))) {
			throw new GiftServiceException(new StringBuffer("Unable to read the image. The current status of the gift [").append(gift.getUuid()).append("]").append(" is [").append(gift.getStatus()).append("]").toString() );
		}
		
		return storageService.loadGiftData(buildObjectName(uuid));
	}

	private String buildObjectName(String uuid) {
		return String.format("%s.jpg", uuid);
	}

	@Override
	public Gift getGiftByUuid(String uuid) throws GiftServiceException, GiftNotFoundExcetption {
		return giftService.getGiftByUuid(uuid);
	}

	@Override
	public PaginatorResult<Gift> loadGifts(User user, SimplePaginator paginator) throws GiftServiceException, StorageServiceException {
		return injectGiftDataUri(giftService.loadGifts(user, paginator));
	}
	
	@Override
	public Gift createGift(Gift gift, String chainUuid, User user) throws GiftServiceException, GiftNotFoundExcetption {
		return giftService.createGift(gift, chainUuid, user);
	}

	@Override
	public PaginatorResult<Gift> loadChain(String chainUuid, User user, SimplePaginator paginator) throws GiftServiceException,
			GiftNotFoundExcetption, GiftNotChainExcetption, StorageServiceException {
		return injectGiftDataUri(giftService.loadChain(checkChainUuid(chainUuid), user, paginator));
	}
	
	

	private PaginatorResult<Gift> injectGiftDataUri(PaginatorResult<Gift> gifts)
			throws StorageServiceException {
		for (Gift gift : gifts.getResult()) {
			gift.setUri(storageService.buildGiftUri(StorageAction.READ, gift.getUuid()).toString());
		}
		return gifts;
	}

	private String checkChainUuid(String chainUuid)
			throws GiftServiceException, GiftNotFoundExcetption,
			GiftNotChainExcetption {
		Gift parentGift = giftService.getGiftByUuid(chainUuid);
		if (parentGift == null) {
			throw new GiftNotFoundExcetption(new StringBuilder("Unable to find the Chain with id [").append(chainUuid).append("]").toString());
		}
		if (!parentGift.getChainMaster()) {
			throw new GiftNotChainExcetption(new StringBuilder("The Gift with id [").append(chainUuid).append("] is not a Chain").toString());
		}
		
		return chainUuid;
	}
	
}
