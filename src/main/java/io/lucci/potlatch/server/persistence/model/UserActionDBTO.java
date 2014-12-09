package io.lucci.potlatch.server.persistence.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="user_action")
public class UserActionDBTO {
	
	@EmbeddedId
	private UserActionIdDBTO id;
	
	@Column(name="i_like_it", columnDefinition = "TINYINT", length=1, nullable=true)
	private Boolean like;
	
	@Column(columnDefinition = "TINYINT", length=1, nullable=true)
	private Boolean inappropriate;

	public UserActionDBTO(){
		
	}
	
	public UserActionDBTO(Long userId, Long giftId, Boolean like, Boolean inappropriate) {
		this.like = like;
		this.inappropriate = inappropriate;
		this.id = new UserActionIdDBTO(userId, giftId);
	}

	public UserActionIdDBTO getId() {
		return id;
	}

	public void setId(UserActionIdDBTO id) {
		this.id = id;
	}

	public Boolean getLike() {
		return like;
	}

	public void setLike(Boolean like) {
		this.like = like;
	}

	public Boolean getInappropriate() {
		return inappropriate;
	}

	public void setInappropriate(Boolean inappropriate) {
		this.inappropriate = inappropriate;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UserAction [id=");
		builder.append(id);
		builder.append(", like=");
		builder.append(like);
		builder.append(", inappropriate=");
		builder.append(inappropriate);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((inappropriate == null) ? 0 : inappropriate.hashCode());
		result = prime * result + ((like == null) ? 0 : like.hashCode());
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
		UserActionDBTO other = (UserActionDBTO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (inappropriate == null) {
			if (other.inappropriate != null)
				return false;
		} else if (!inappropriate.equals(other.inappropriate))
			return false;
		if (like == null) {
			if (other.like != null)
				return false;
		} else if (!like.equals(other.like))
			return false;
		return true;
	}

	
}
