package io.lucci.potlatch.service.adapter;

import io.lucci.potlatch.model.Gift;
import io.lucci.potlatch.persistence.model.GiftDBTO;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GiftAdapter {
	
	@Autowired
	private UserAdapter userAdapter;

	public Gift dbtoToTo(GiftDBTO giftDBTO, boolean adaptUser) {
		Gift gift = new Gift();
		BeanUtils.copyProperties(giftDBTO, gift, "user");
		if (adaptUser) {
			gift.setUser(userAdapter.dbtoToTo(giftDBTO.getUser()));
		}
		return gift;
	}

}
