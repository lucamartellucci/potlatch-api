package io.lucci.potlatch.controller;

import io.lucci.potlatch.model.Gift;
import io.lucci.potlatch.model.User;
import io.lucci.potlatch.security.CurrentUser;
import io.lucci.potlatch.service.GiftNotFoundExcetption;
import io.lucci.potlatch.service.GiftService;
import io.lucci.potlatch.service.GiftServiceException;

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
@RequestMapping(value = "/gift")
public class GiftController {
	
	private static final Logger logger = LoggerFactory.getLogger(GiftController.class);

	@Autowired
	private GiftService giftService;
	

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody Gift findById(@PathVariable("id") final String id) {
    	try { 	

    		logger.info("Searching gift with id [{}]", id);
			Gift gift = giftService.getGiftByUuid(id);
			logger.info("Found gift {}", gift);
			return gift;
			
		} catch (GiftServiceException e) {
			return null;
		} catch (GiftNotFoundExcetption e) {
			return null;
		}
    }
    
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public @ResponseBody List<Gift> retrieveGifts(final HttpServletResponse response, @CurrentUser User user, Pageable p) {
    	try {

    		logger.info("Retrieve gifts for user [{}]", user.getUsername());
    		List<Gift> gifts = giftService.findAllGifts(user, p);
    		logger.info("Found [{}] gifts", gifts.size());
    		return gifts;
    		
		} catch (Exception e) {
			// TODO: handle exception
		}
    	return null;
    }
}
