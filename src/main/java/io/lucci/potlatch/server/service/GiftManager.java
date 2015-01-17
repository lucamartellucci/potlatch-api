package io.lucci.potlatch.server.service;

import io.lucci.potlatch.server.service.exception.GiftNotChainExcetption;
import io.lucci.potlatch.server.service.exception.GiftNotFoundExcetption;
import io.lucci.potlatch.server.service.exception.GiftManagerException;
import io.lucci.potlatch.server.web.model.Gift;
import io.lucci.potlatch.server.web.model.PaginatorResult;
import io.lucci.potlatch.server.web.model.SimplePaginator;
import io.lucci.potlatch.server.web.model.User;
import io.lucci.potlatch.server.web.model.Gift.GiftStatus;

import java.io.InputStream;

import javax.transaction.Transactional;

public interface GiftManager {

	public Gift createGift(Gift gift, Long chainId, User user) throws GiftManagerException, GiftNotFoundExcetption;
	
	@Transactional
	public Gift setGiftData(Long id, InputStream data) throws GiftManagerException, GiftNotFoundExcetption, StorageServiceException;
	
	@Transactional
	public InputStream getGiftData(Long id) throws GiftManagerException, GiftNotFoundExcetption, StorageServiceException;
	
	public Gift getGiftById(Long id) throws GiftManagerException, GiftNotFoundExcetption;

	public PaginatorResult<Gift> getGifts(User user, SimplePaginator paginator) throws GiftManagerException, StorageServiceException;
	
	public PaginatorResult<Gift> getGiftChain(Long chainId, User user, SimplePaginator paginator) throws GiftManagerException, GiftNotFoundExcetption, GiftNotChainExcetption, StorageServiceException;

	@Transactional
	public Long likeGift(Long id, Long userId) throws GiftManagerException, GiftNotFoundExcetption;
	
	@Transactional
	public Long unlikeGift(Long id, Long userId) throws GiftManagerException, GiftNotFoundExcetption;

	public Gift updateGiftStatus(Long id, GiftStatus status) throws GiftManagerException, GiftNotFoundExcetption;
	
}
