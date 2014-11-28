package io.lucci.potlatch.web.model;

import java.io.Serializable;
import java.util.Date;

public class Gift implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public enum GiftStatus {active,ready_for_upload,upload_failed}
	
	private String uuid;
	
	private String title;
	private String description;
	private Date timestamp;
	private Boolean chainMaster;
	private String uri;
	private String status;
	private Long numberOfLikes;
	private User user;
	private Boolean likedByMe;
	private Boolean reportedByMe;
	
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public Boolean getChainMaster() {
		return chainMaster;
	}
	public void setChainMaster(Boolean chainMaster) {
		this.chainMaster = chainMaster;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Long getNumberOfLikes() {
		return numberOfLikes;
	}
	public void setNumberOfLikes(Long numberOfLikes) {
		this.numberOfLikes = numberOfLikes;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Boolean getLikedByMe() {
		return likedByMe;
	}
	public void setLikedByMe(Boolean likedByMe) {
		this.likedByMe = likedByMe;
	}
	public Boolean getReportedByMe() {
		return reportedByMe;
	}
	public void setReportedByMe(Boolean reportedByMe) {
		this.reportedByMe = reportedByMe;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Gift [uuid=");
		builder.append(uuid);
		builder.append(", title=");
		builder.append(title);
		builder.append(", description=");
		builder.append(description);
		builder.append(", timestamp=");
		builder.append(timestamp);
		builder.append(", chainMaster=");
		builder.append(chainMaster);
		builder.append(", uri=");
		builder.append(uri);
		builder.append(", status=");
		builder.append(status);
		builder.append(", numberOfLikes=");
		builder.append(numberOfLikes);
		builder.append(", user=");
		builder.append(user);
		builder.append(", likedByMe=");
		builder.append(likedByMe);
		builder.append(", reportedByMe=");
		builder.append(reportedByMe);
		builder.append("]");
		return builder.toString();
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((chainMaster == null) ? 0 : chainMaster.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result
				+ ((likedByMe == null) ? 0 : likedByMe.hashCode());
		result = prime * result
				+ ((numberOfLikes == null) ? 0 : numberOfLikes.hashCode());
		result = prime * result
				+ ((reportedByMe == null) ? 0 : reportedByMe.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result
				+ ((timestamp == null) ? 0 : timestamp.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((uri == null) ? 0 : uri.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
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
		Gift other = (Gift) obj;
		if (chainMaster == null) {
			if (other.chainMaster != null)
				return false;
		} else if (!chainMaster.equals(other.chainMaster))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (likedByMe == null) {
			if (other.likedByMe != null)
				return false;
		} else if (!likedByMe.equals(other.likedByMe))
			return false;
		if (numberOfLikes == null) {
			if (other.numberOfLikes != null)
				return false;
		} else if (!numberOfLikes.equals(other.numberOfLikes))
			return false;
		if (reportedByMe == null) {
			if (other.reportedByMe != null)
				return false;
		} else if (!reportedByMe.equals(other.reportedByMe))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (timestamp == null) {
			if (other.timestamp != null)
				return false;
		} else if (!timestamp.equals(other.timestamp))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (uri == null) {
			if (other.uri != null)
				return false;
		} else if (!uri.equals(other.uri))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;
		return true;
	}
	
	
	
}
