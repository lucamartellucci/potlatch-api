package io.lucci.potlatch.persistence.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class UserActionIdDBTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name="user_id")
	private Long userId;
	
	@Column(name="gift_id")
	private Long giftId;
	
	public UserActionIdDBTO(){
		
	}
	
	public UserActionIdDBTO(Long userId, Long giftId) {
		super();
		this.userId = userId;
		this.giftId = giftId;
	}

	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getGiftId() {
		return giftId;
	}
	public void setGiftId(Long giftId) {
		this.giftId = giftId;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((giftId == null) ? 0 : giftId.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserActionIdDBTO other = (UserActionIdDBTO) obj;
		if (giftId == null) {
			if (other.giftId != null)
				return false;
		} else if (!giftId.equals(other.giftId))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UserActionId [userId=");
		builder.append(userId);
		builder.append(", giftId=");
		builder.append(giftId);
		builder.append("]");
		return builder.toString();
	}
	
	

}
