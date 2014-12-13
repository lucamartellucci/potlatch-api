package io.lucci.potlatch.server.service;

import io.lucci.potlatch.server.service.exception.GiftNotFoundExcetption;
import io.lucci.potlatch.server.service.exception.GiftServiceException;
import io.lucci.potlatch.server.web.model.Gift;
import io.lucci.potlatch.server.web.model.User;

import java.io.InputStream;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Pageable;

public interface GiftManager {

	@Transactional
	public Gift setGiftData(String uuid, InputStream data) throws GiftServiceException, GiftNotFoundExcetption, StorageServiceException;
	
	@Transactional
	public InputStream getGiftData(String uuid) throws GiftServiceException, GiftNotFoundExcetption, StorageServiceException;
	
	public Gift getGiftByUuid(String uuid) throws GiftServiceException, GiftNotFoundExcetption;
	
	public List<Gift> findAllGifts(User user, Pageable p) throws GiftServiceException;

	public Gift createGift(Gift gift, String parentUuid, User user) throws GiftServiceException, GiftNotFoundExcetption;
}
