package io.lucci.potlatch.server.web.model.adapter;

import io.lucci.potlatch.server.persistence.model.GiftDBTO;
import io.lucci.potlatch.server.web.model.Gift;
import io.lucci.potlatch.server.web.model.User;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GiftAdapter {
	
	@Autowired
	private UserAdapter userAdapter;

	public Gift dbtoToTo(GiftDBTO giftDBTO, boolean adaptUser) {
		Gift gift = new Gift();
		BeanUtils.copyProperties(giftDBTO, gift, "user", "chainMaster", "likedByMe", "reportedByMe", "imageUrl", "imageFilename");
		gift.setChainMaster(giftDBTO.getParentId() == null ? Boolean.TRUE: Boolean.FALSE);
		gift.setLikedByMe(giftDBTO.getLiked());
		gift.setReportedByMe(giftDBTO.getReported());
		gift.setImageFilename(buildImageFilename(giftDBTO.getUuid()));
		
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
		if (gift == null) {
			return null;
		}
		GiftDBTO result = new GiftDBTO();
		result.setTitle(gift.getTitle());
		result.setDescription(gift.getDescription());
		result.setStatus(gift.getStatus());
		result.setTimestamp(gift.getTimestamp());
		if (user != null) {
			result.setUser(userAdapter.toTOdbto(user));
		}
		return result;
	}
	
	public String buildImageFilename(String uuid) {
		return String.format("%s.jpg", uuid);
	}

}
