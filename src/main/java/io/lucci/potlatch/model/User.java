package io.lucci.potlatch.model;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String email;
	private String name;
	private String password;
	private Date birthdate;
	private String gender;
	private Boolean blockInappropriate;
	private Long refreshInterval;
	private Long numberOflikes;
	
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public Long getNumberOflikes() {
		return numberOflikes;
	}
	public void setNumberOflikes(Long numberOflikes) {
		this.numberOflikes = numberOflikes;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("User [id=");
		builder.append(id);
		builder.append(", email=");
		builder.append(email);
		builder.append(", name=");
		builder.append(name);
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
		builder.append(", numberOflikes=");
		builder.append(numberOflikes);
		builder.append("]");
		return builder.toString();
	}
	
}
