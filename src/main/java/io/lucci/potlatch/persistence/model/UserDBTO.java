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
	
	private String name;
	
	private String password;
	
	private Date birthdate;
	
	@Column(columnDefinition="char")
	private String gender;
	
	@OneToMany(mappedBy="user", cascade = { CascadeType.ALL, CascadeType.REMOVE }, orphanRemoval = true, fetch=FetchType.LAZY)
	private List<GiftDBTO> gifts;

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

	public List<GiftDBTO> getGifts() {
		return gifts;
	}

	public void setGifts(List<GiftDBTO> gifts) {
		this.gifts = gifts;
	}
	
	
}
