package io.lucci.potlatch.web.model;

import java.io.Serializable;
import java.util.Date;

public class Gift implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
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
	
	
}
