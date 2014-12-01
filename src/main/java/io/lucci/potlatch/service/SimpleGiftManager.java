package io.lucci.potlatch.service;

import io.lucci.potlatch.service.exception.GiftNotFoundExcetption;
import io.lucci.potlatch.service.exception.GiftServiceException;
import io.lucci.potlatch.web.controller.exception.InternalServerErrorException;
import io.lucci.potlatch.web.controller.exception.ResourceNotFoundException;
import io.lucci.potlatch.web.model.Gift;
import io.lucci.potlatch.web.model.Gift.GiftStatus;

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
	public Gift setGiftData(String uuid, InputStream data) throws InternalServerErrorException, ResourceNotFoundException {
		
		try {
    		storageService.storeObject(data, String.format("%s.jpg", uuid));
		} catch (Exception e) {
			logger.error("Unable to store the gift data", e);
			throw new InternalServerErrorException("Unable to store the gift data", e);
		}
		
		try {
			Gift gift = giftService.updateGiftStatus(uuid, GiftStatus.active);
			return gift;
		} catch (GiftServiceException e) {
			logger.error(e.getMessage(), e);
			final String msg = new StringBuilder("Unable to update the gift with id [").append(uuid).append("]").toString();
			throw new InternalServerErrorException(msg, e);
		} catch (GiftNotFoundExcetption e) {
			final String msg = new StringBuilder("Unable to find the gift with id [").append(uuid).append("]").toString();
			logger.error(msg, e);
			throw new ResourceNotFoundException(msg, e);
		}
		
	}
	
}
