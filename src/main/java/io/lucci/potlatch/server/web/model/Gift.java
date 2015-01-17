package io.lucci.potlatch.server.web.model;

import java.io.Serializable;
import java.util.Date;

public class Gift implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public enum GiftStatus {active,ready_for_upload,upload_failed}
	
	private Long id;
	private String title;
	private String description;
	private Date timestamp;
	private Boolean chainMaster;
	private String imageUrl;
	private String imageFilename;
	private String status;
	private Long numberOfLikes;
	private User user;
	private Boolean likedByMe;
	private Boolean reportedByMe;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getImageFilename() {
		return imageFilename;
	}
	public void setImageFilename(String imageFilename) {
		this.imageFilename = imageFilename;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((chainMaster == null) ? 0 : chainMaster.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((imageFilename == null) ? 0 : imageFilename.hashCode());
		result = prime * result
				+ ((imageUrl == null) ? 0 : imageUrl.hashCode());
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
		result = prime * result + ((user == null) ? 0 : user.hashCode());
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
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (imageFilename == null) {
			if (other.imageFilename != null)
				return false;
		} else if (!imageFilename.equals(other.imageFilename))
			return false;
		if (imageUrl == null) {
			if (other.imageUrl != null)
				return false;
		} else if (!imageUrl.equals(other.imageUrl))
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
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Gift [id=");
		builder.append(id);
		builder.append(", title=");
		builder.append(title);
		builder.append(", description=");
		builder.append(description);
		builder.append(", timestamp=");
		builder.append(timestamp);
		builder.append(", chainMaster=");
		builder.append(chainMaster);
		builder.append(", imageUrl=");
		builder.append(imageUrl);
		builder.append(", imageFilename=");
		builder.append(imageFilename);
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
	
}
