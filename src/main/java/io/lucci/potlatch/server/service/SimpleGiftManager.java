package io.lucci.potlatch.server.service;

import io.lucci.potlatch.server.service.exception.GiftNotFoundExcetption;
import io.lucci.potlatch.server.service.exception.GiftServiceException;
import io.lucci.potlatch.server.web.model.Gift;
import io.lucci.potlatch.server.web.model.User;
import io.lucci.potlatch.server.web.model.Gift.GiftStatus;

import java.io.InputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
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
   		storageService.storeObject(data, buildObjectName(uuid));
		 
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
		
		return storageService.readObject(buildObjectName(uuid));
	}

	private String buildObjectName(String uuid) {
		return String.format("%s.jpg", uuid);
	}

	@Override
	public Gift getGiftByUuid(String uuid) throws GiftServiceException, GiftNotFoundExcetption {
		return giftService.getGiftByUuid(uuid);
	}

	@Override
	public List<Gift> findAllGifts(User user, Pageable p) throws GiftServiceException {
		return giftService.findAllGifts(user, p);
	}

	@Override
	public Gift createGift(Gift gift, String parentUuid, User user) throws GiftServiceException, GiftNotFoundExcetption {
		return giftService.createGift(gift, parentUuid, user);
	}
	
}
