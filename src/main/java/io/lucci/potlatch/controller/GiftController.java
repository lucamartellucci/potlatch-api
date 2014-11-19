package io.lucci.potlatch.controller;

import io.lucci.potlatch.controller.exception.ResourceNotFoundException;
import io.lucci.potlatch.controller.resolver.CurrentUser;
import io.lucci.potlatch.controller.resolver.PageReq;
import io.lucci.potlatch.model.Gift;
import io.lucci.potlatch.model.User;
import io.lucci.potlatch.service.GiftService;
import io.lucci.potlatch.service.exception.GiftNotFoundExcetption;
import io.lucci.potlatch.service.exception.GiftServiceException;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class GiftController {
	
	private static final Logger logger = LoggerFactory.getLogger(GiftController.class);

	@Autowired
	private GiftService giftService;
	

    @RequestMapping(value = "/gift/{id}", method = RequestMethod.GET)
    public @ResponseBody Gift findById(@PathVariable("id") final String id) throws ResourceNotFoundException {
    	try { 	

    		logger.info("Searching gift with id [{}]", id);
			Gift gift = giftService.getGiftByUuid(id);
			logger.info("Found gift {}", gift);
			return gift;
			
		} catch (GiftServiceException e) {
			logger.error("Unable to find the gift", e);
			throw new RuntimeException();
		} catch (GiftNotFoundExcetption e) {
			logger.error("Unable to find the gift", e);
			throw new ResourceNotFoundException();
		}
    }
    
    @RequestMapping(value = "/gift", method = RequestMethod.GET)
    public @ResponseBody List<Gift> retrieveGifts(@CurrentUser User user, @PageReq Pageable p) {
    	try {

    		logger.info("Retrieve gifts for user [{}]", user.getUsername());
    		List<Gift> gifts = giftService.findAllGifts(user, p);
    		logger.info("Found [{}] gifts", gifts.size());
    		return gifts;
    		
		} catch (Exception e) {
			logger.error("Unable load the gifts", e);
			throw new RuntimeException(e);
		}
    }
    
    @RequestMapping(value = "/gift", method = RequestMethod.POST)
    public @ResponseBody Gift createGift(Gift gift, @CurrentUser User user) {
    	try {
    		Gift createdGift = giftService.createGift(gift);
    		return createdGift;
		} catch (Exception e) {
			logger.error("Unable to create the gift", e);
			throw new RuntimeException();
		}
    	
    }
}
