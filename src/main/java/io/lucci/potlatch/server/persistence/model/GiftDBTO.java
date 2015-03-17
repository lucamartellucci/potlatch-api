package io.lucci.potlatch.server.persistence.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.OneToMany;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="gift")
@NamedNativeQueries(
	{
		@NamedNativeQuery(
			name="Gift.findAllByUserId",
			query="select g.id, g.title, g.description, g.timestamp, g.parent_id, g.uuid, g.status, g.number_of_likes, g.user_id, ua.i_like_it, ua.inappropriate, u.username, u.email from gift g left join user_action ua on ua.gift_id = g.id and ua.user_id = ?1 join user u on g.user_id = u.id where g.parent_id is null order by g.timestamp desc", 
			resultSetMapping="giftExtended"
		),
		@NamedNativeQuery(
				name="Gift.countByUserId",
				query="select count(g.id) as num from gift g left join user_action ua on ua.gift_id = g.id and ua.user_id = ?1 where g.parent_id is null",
				resultSetMapping="giftCount"
		),
		@NamedNativeQuery(
				name="Gift.findAllByUserIdAndInappropriate",
				query="select g.id, g.title, g.description, g.timestamp, g.parent_id, g.uuid, g.status, g.number_of_likes, g.user_id, ua.i_like_it, ua.inappropriate, u.username, u.email from gift g left join user_action ua on ua.gift_id = g.id and ua.user_id = ?1 join user u on g.user_id = u.id where g.parent_id is null and (ua.inappropriate is null or ua.inappropriate <> 1) order by g.timestamp desc", 
				resultSetMapping="giftExtended"
		),
		@NamedNativeQuery(
				name="Gift.countByUserIdAndInappropriate",
				query="select count(g.id) as num from gift g left join user_action ua on ua.gift_id = g.id and ua.user_id = ?1 where g.parent_id is null and (ua.inappropriate is null or ua.inappropriate <> 1)",
				resultSetMapping="giftCount"
		),
		@NamedNativeQuery(
				name="Gift.findAllByUserIdAndTitle",
				query="select g.id, g.title, g.description, g.timestamp, g.parent_id, g.uuid, g.status, g.number_of_likes, g.user_id, ua.i_like_it, ua.inappropriate, u.username, u.email from gift g left join user_action ua on ua.gift_id = g.id and ua.user_id = ?1 join user u on g.user_id = u.id where g.title like '%?2%' and g.parent_id is null and (ua.inappropriate is null or ua.inappropriate <> 1) order by g.timestamp desc", 
				resultSetMapping="giftExtended"
		)
	}
)
@SqlResultSetMappings({
	@SqlResultSetMapping(name = "giftExtended", classes = {
			@ConstructorResult( 
					targetClass=GiftDBTO.class, 
					columns= {
						 @ColumnResult(name="id", type=java.lang.Long.class),
						 @ColumnResult(name="title", type=java.lang.String.class),
						 @ColumnResult(name="description", type=java.lang.String.class),
						 @ColumnResult(name="timestamp", type=java.util.Date.class),
						 @ColumnResult(name="parent_id", type=java.lang.Long.class),
						 @ColumnResult(name="uuid", type=java.lang.String.class),
						 @ColumnResult(name="status", type=java.lang.String.class),
						 @ColumnResult(name="number_of_likes", type=java.lang.Long.class),
						 @ColumnResult(name="i_like_it", type=java.lang.Boolean.class),
						 @ColumnResult(name="inappropriate", type=java.lang.Boolean.class),
						 @ColumnResult(name="user_id", type=java.lang.Long.class),
						 @ColumnResult(name="username", type=java.lang.String.class),
						 @ColumnResult(name="email", type=java.lang.String.class),
					}
				)
	}),
	@SqlResultSetMapping(name = "giftCount", classes = {
			@ConstructorResult( 
					targetClass=Long.class, 
					columns= {
						 @ColumnResult(name="num", type=java.lang.Long.class),
					}
				)
	})
})
public class GiftDBTO {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private String title;
	
	private String description;
	
	private Date timestamp;
	
	@Column(name="parent_id")
	private Long parentId;
	
	private String uuid;
	
	private String status;
	
	@Column(name="number_of_likes")
	private Long numberOfLikes;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="user_id")
	private UserDBTO user;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="id.giftId")
	private List<UserActionDBTO> userActions;
	
	@Transient
	private Boolean liked;
	
	@Transient
	private Boolean reported;

	
	public GiftDBTO(){
		
	}
	
	public GiftDBTO(Long id, String title, String description,
			Date timestamp, Long parentId, String uuid, String status,
			Long numberOfLikes, Boolean liked, Boolean reported, Long userId, String username, String email) {
		
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.timestamp = timestamp;
		this.parentId = parentId;
		this.uuid = uuid;
		this.status = status;
		this.numberOfLikes = numberOfLikes;
		this.liked = liked;
		this.reported = reported;
		this.user = new UserDBTO(userId,username,email);
	}

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
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public UserDBTO getUser() {
		return user;
	}
	public void setUser(UserDBTO user) {
		this.user = user;
	}
	public List<UserActionDBTO> getUserActions() {
		return userActions;
	}
	public void setUserActions(List<UserActionDBTO> userActions) {
		this.userActions = userActions;
	}
	public Long getNumberOfLikes() {
		return numberOfLikes;
	}
	public void setNumberOfLikes(Long numberOfLikes) {
		this.numberOfLikes = numberOfLikes;
	}
	public Boolean getLiked() {
		return liked;
	}
	public void setLiked(Boolean liked) {
		this.liked = liked;
	}
	public Boolean getReported() {
		return reported;
	}
	public void setReported(Boolean reported) {
		this.reported = reported;
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
		builder.append(", parentId=");
		builder.append(parentId);
		builder.append(", uuid=");
		builder.append(uuid);
		builder.append(", status=");
		builder.append(status);
		builder.append(", numberOfLikes=");
		builder.append(numberOfLikes);
		builder.append(", user=");
		builder.append(user);
		builder.append(", liked=");
		builder.append(liked);
		builder.append(", reported=");
		builder.append(reported);
		builder.append("]");
		return builder.toString();
	}
	
}
