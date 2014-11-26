package io.lucci.potlatch.service.adapter;

import java.util.ArrayList;
import java.util.List;

import io.lucci.potlatch.persistence.model.GiftDBTO;
import io.lucci.potlatch.web.model.Gift;
import io.lucci.potlatch.web.model.User;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GiftAdapter {
	
	@Autowired
	private UserAdapter userAdapter;

	public Gift dbtoToTo(GiftDBTO giftDBTO, boolean adaptUser) {
		Gift gift = new Gift();
		BeanUtils.copyProperties(giftDBTO, gift, "user", "chainMaster", "likedByMe", "reportedByMe");
		gift.setChainMaster(giftDBTO.getParentId() == null ? Boolean.TRUE: Boolean.FALSE);
		gift.setLikedByMe(giftDBTO.getLiked());
		gift.setReportedByMe(giftDBTO.getReported());
		
		if (adaptUser) {
			gift.setUser(userAdapter.dbtoToTo(giftDBTO.getUser()));
		}
		return gift;
	}

	public List<Gift> dbtoToTo(List<GiftDBTO> giftDBTOList, boolean adaptUser) {
		if (giftDBTOList == null) {
			return null;
		}
		List<Gift> gifts = new ArrayList<>();
		for (GiftDBTO giftDBTO : giftDBTOList) {
			gifts.add(dbtoToTo(giftDBTO, adaptUser));
		}
		return gifts;
	}

	public GiftDBTO toToDbto(Gift gift, User user) {
		// TODO Auto-generated method stub
		return null;
	}

}
