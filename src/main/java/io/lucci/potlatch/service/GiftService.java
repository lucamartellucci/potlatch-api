package io.lucci.potlatch.service;
import io.lucci.potlatch.persistence.model.GiftDBTO;

import javax.transaction.Transactional;


public interface GiftService {
	
	@Transactional
	public Long likeGift(Long giftId, Long userId) throws GiftServiceException;
	
	@Transactional
	public Long unlikeGift(Long giftId, Long userId) throws GiftServiceException;

	public GiftDBTO getGiftById(Long giftId) throws GiftServiceException;

}
