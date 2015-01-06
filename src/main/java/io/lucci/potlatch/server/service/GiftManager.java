package io.lucci.potlatch.server.service;

import io.lucci.potlatch.server.service.exception.GiftNotChainExcetption;
import io.lucci.potlatch.server.service.exception.GiftNotFoundExcetption;
import io.lucci.potlatch.server.service.exception.GiftServiceException;
import io.lucci.potlatch.server.web.model.Gift;
import io.lucci.potlatch.server.web.model.PaginatorResult;
import io.lucci.potlatch.server.web.model.SimplePaginator;
import io.lucci.potlatch.server.web.model.User;

import java.io.InputStream;
import java.util.List;

import javax.transaction.Transactional;

public interface GiftManager {

	public Gift createGift(Gift gift, String parentUuid, User user) throws GiftServiceException, GiftNotFoundExcetption;
	
	@Transactional
	public Gift setGiftData(String uuid, InputStream data) throws GiftServiceException, GiftNotFoundExcetption, StorageServiceException;
	
	@Transactional
	public InputStream getGiftData(String uuid) throws GiftServiceException, GiftNotFoundExcetption, StorageServiceException;
	
	public Gift getGiftByUuid(String uuid) throws GiftServiceException, GiftNotFoundExcetption;
	
	public List<Gift> findAllGifts(User user) throws GiftServiceException;

	public List<Gift> findAllChainedGifts(String parentUuid, User user) throws GiftServiceException, GiftNotFoundExcetption, GiftNotChainExcetption;

	public PaginatorResult<Gift> findAllGifts(User user, SimplePaginator paginator) throws GiftServiceException;
	
	public PaginatorResult<Gift> findAllChainedGifts(String parentUuid, User user, SimplePaginator paginator) throws GiftServiceException, GiftNotFoundExcetption, GiftNotChainExcetption;
}
