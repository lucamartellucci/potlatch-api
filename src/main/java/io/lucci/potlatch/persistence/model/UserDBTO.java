package io.lucci.potlatch.persistence.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="user")
public class UserDBTO {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private String email;
	
	@Column(unique=true)
	private String username;
	
	private String password;
	
	private Date birthdate;
	
	@Column(columnDefinition="char")
	private String gender;
	
	@Column(name="block_inappropriate", columnDefinition = "TINYINT", length=1, nullable=true)
	private Boolean blockInappropriate;
	
	@Column(name="refresh_interval")
	private Long refreshInterval;
	
	@OneToMany(mappedBy="user", cascade = { CascadeType.ALL, CascadeType.REMOVE }, orphanRemoval = true, fetch=FetchType.LAZY)
	private List<GiftDBTO> gifts;
	
	private String roles;

	
	public UserDBTO(){
		super();
	}
	
	public UserDBTO(Long id, String username, String email){
		super();
		this.id=id;
		this.username=username;
		this.email=email;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public List<GiftDBTO> getGifts() {
		return gifts;
	}

	public void setGifts(List<GiftDBTO> gifts) {
		this.gifts = gifts;
	}

	public Boolean getBlockInappropriate() {
		return blockInappropriate;
	}

	public void setBlockInappropriate(Boolean blockInappropriate) {
		this.blockInappropriate = blockInappropriate;
	}

	public Long getRefreshInterval() {
		return refreshInterval;
	}

	public void setRefreshInterval(Long refreshInterval) {
		this.refreshInterval = refreshInterval;
	}

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UserDBTO [id=");
		builder.append(id);
		builder.append(", email=");
		builder.append(email);
		builder.append(", username=");
		builder.append(username);
		builder.append(", password=");
		builder.append(password);
		builder.append(", birthdate=");
		builder.append(birthdate);
		builder.append(", gender=");
		builder.append(gender);
		builder.append(", blockInappropriate=");
		builder.append(blockInappropriate);
		builder.append(", refreshInterval=");
		builder.append(refreshInterval);
		builder.append(", roles=");
		builder.append(roles);
		builder.append("]");
		return builder.toString();
	}

}
