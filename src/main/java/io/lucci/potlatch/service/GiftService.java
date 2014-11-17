package io.lucci.potlatch.service;
import io.lucci.potlatch.model.Gift;
import io.lucci.potlatch.model.User;

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

}
