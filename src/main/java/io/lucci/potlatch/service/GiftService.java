package io.lucci.potlatch.service;
import io.lucci.potlatch.service.exception.GiftNotFoundExcetption;
import io.lucci.potlatch.service.exception.GiftServiceException;
import io.lucci.potlatch.web.model.Gift;
import io.lucci.potlatch.web.model.User;
import io.lucci.potlatch.web.model.Gift.GiftStatus;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Pageable;


public interface GiftService {
	
	@Transactional
	public Long likeGift(String uuid, Long userId) throws GiftServiceException, GiftNotFoundExcetption;
	
	@Transactional
	public Long unlikeGift(String uuid, Long userId) throws GiftServiceException, GiftNotFoundExcetption;

	public Gift getGiftByUuid(String uuid) throws GiftServiceException, GiftNotFoundExcetption;
	
	public List<Gift> findAllGifts(User user, Pageable p) throws GiftServiceException;

	public Gift createGift(Gift gift, Long parentId, User user) throws GiftServiceException;

	public Gift updateGiftStatus(String uuid, GiftStatus status) throws GiftServiceException, GiftNotFoundExcetption;

}
