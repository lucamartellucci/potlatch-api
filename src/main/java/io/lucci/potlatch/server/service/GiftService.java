package io.lucci.potlatch.server.service;
import io.lucci.potlatch.server.service.exception.GiftNotFoundExcetption;
import io.lucci.potlatch.server.service.exception.GiftServiceException;
import io.lucci.potlatch.server.web.model.Gift;
import io.lucci.potlatch.server.web.model.Gift.GiftStatus;
import io.lucci.potlatch.server.web.model.PaginatorResult;
import io.lucci.potlatch.server.web.model.SimplePaginator;
import io.lucci.potlatch.server.web.model.User;

import java.util.List;

import javax.transaction.Transactional;


public interface GiftService {
	
	@Transactional
	public Long likeGift(String uuid, Long userId) throws GiftServiceException, GiftNotFoundExcetption;
	
	@Transactional
	public Long unlikeGift(String uuid, Long userId) throws GiftServiceException, GiftNotFoundExcetption;

	public Gift getGiftByUuid(String uuid) throws GiftServiceException, GiftNotFoundExcetption;
	
	public PaginatorResult<Gift> findAllGifts(User user, SimplePaginator paginator) throws GiftServiceException;
	
	public List<Gift> findAllGifts(User user) throws GiftServiceException;

	public Gift createGift(Gift gift, String parentUuid, User user) throws GiftServiceException, GiftNotFoundExcetption;

	public Gift updateGiftStatus(String uuid, GiftStatus status) throws GiftServiceException, GiftNotFoundExcetption;

}
