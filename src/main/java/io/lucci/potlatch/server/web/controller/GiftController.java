package io.lucci.potlatch.server.web.controller;

import io.lucci.potlatch.server.service.GiftManager;
import io.lucci.potlatch.server.service.StorageServiceException;
import io.lucci.potlatch.server.service.exception.GiftNotFoundExcetption;
import io.lucci.potlatch.server.service.exception.GiftServiceException;
import io.lucci.potlatch.server.web.controller.exception.InternalServerErrorException;
import io.lucci.potlatch.server.web.controller.exception.ResourceNotFoundException;
import io.lucci.potlatch.server.web.controller.resolver.CurrentUser;
import io.lucci.potlatch.server.web.controller.resolver.PageReq;
import io.lucci.potlatch.server.web.model.Gift;
import io.lucci.potlatch.server.web.model.User;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;


@Controller
@RequestMapping(value="/api/v1")
public class GiftController {
	
	private static final Logger logger = LoggerFactory.getLogger(GiftController.class);
	
	@Autowired
	private GiftManager giftManager;
	

    @RequestMapping(value = "/gift/{id}", method = RequestMethod.GET)
    public @ResponseBody Gift findById(@PathVariable("id") final String id) throws ResourceNotFoundException, InternalServerErrorException {
    	try { 	

    		logger.info("Searching gift with id [{}]", id);
			Gift gift = giftManager.getGiftByUuid(id);
			logger.info("Found gift {}", gift);
			return gift;
			
		} catch (GiftServiceException e) {
			final String msg = new StringBuilder("Unable to find the gift with id [").append(id).append("]").toString();
			logger.error(msg, e);
			throw new InternalServerErrorException(msg, e);
		} catch (GiftNotFoundExcetption e) {
			logger.error(e.getMessage(), e);
			throw new ResourceNotFoundException(e.getMessage());
		}
    }
    
    @RequestMapping(value = "/gift", method = RequestMethod.GET)
    public @ResponseBody List<Gift> retrieveGifts(@CurrentUser User user, @PageReq Pageable p) throws InternalServerErrorException {
    	try {

    		logger.info("Retrieve gifts for user [{}]", user.getUsername());
    		List<Gift> gifts = giftManager.findAllGifts(user, p);
    		logger.info("Found [{}] gifts", gifts.size());
    		return gifts;
    		
		} catch (Exception e) {
			logger.error("Unable load the gifts", e);
			throw new InternalServerErrorException("Unable load the gifts", e);
		}
    }
    
    @RequestMapping(value = "/gift", method = RequestMethod.POST)
    public @ResponseBody Gift createGift(
    		@RequestBody Gift gift, 
    		@RequestParam( value="parentId", required = false) Long parentId, 
    		@CurrentUser User user) throws InternalServerErrorException 
    {
    	try {
    		Gift createdGift = giftManager.createGift(gift, parentId, user);
    		return createdGift;
		} catch (Exception e) {
			logger.error("Unable to create the gift", e);
			throw new InternalServerErrorException("Unable to create the gift", e);
		}
    }
    
    
    @RequestMapping(value = "/gift/{id}/data", method = RequestMethod.POST)
    public @ResponseBody Gift setGiftData(
    		@PathVariable("id") final String id, @RequestParam("file") final MultipartFile multiPart, @CurrentUser User user) 
    				throws InternalServerErrorException, ResourceNotFoundException 
    {
    	logger.info("Storing data for gift [{}]", id);
    	Gift gift = null;
    	try {
				gift = giftManager.setGiftData(id, multiPart.getInputStream());
			
    	} catch (IOException | GiftServiceException | StorageServiceException e) {
    		final String msg = new StringBuilder("Unable to set data for the gift [").append(id).append("]").toString();
    		logger.error(msg);
			throw new InternalServerErrorException(msg, e);
		} catch (GiftNotFoundExcetption e) {
			final String msg = new StringBuilder("Unable to find gift [").append(id).append("]").toString();
			logger.error(msg);
			throw new ResourceNotFoundException(msg, e);
		}
    	return gift;
    }
    
    @RequestMapping(value = "/gift/{id}/data", method = RequestMethod.GET)
    public void getGiftData(@PathVariable("id") final String id, HttpServletResponse response) throws InternalServerErrorException, ResourceNotFoundException {
    	logger.info("Retrieve data for gift [{}]", id);
    	
    	try (InputStream giftData = giftManager.getGiftData(id);) {
    		
			IOUtils.copy(giftData, response.getOutputStream());
			
		} catch (StorageServiceException | GiftServiceException | IOException e) {
			final String msg = new StringBuilder("Unable to get data for the gift [").append(id).append("]").toString();
    		logger.error(msg);
			throw new InternalServerErrorException(msg, e);		
		} catch (GiftNotFoundExcetption e) {
			final String msg = new StringBuilder("Unable to find gift [").append(id).append("]").toString();
			logger.error(msg);
			throw new ResourceNotFoundException(msg, e);
		} 
    }
    
}