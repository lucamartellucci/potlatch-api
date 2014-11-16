package io.lucci.potlatch.security;

import io.lucci.potlatch.model.User;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

public class SimpleUserDetails implements UserDetails {

	private static final long serialVersionUID = 1L;

	private User user;
	private Collection<? extends GrantedAuthority> autorities = AuthorityUtils.NO_AUTHORITIES;
	
	public SimpleUserDetails(User user) {
		buildAutorities(user);
	}

	public SimpleUserDetails() {
		
	}
	
	public void setUserDBTO(User user) {
		buildAutorities(user);
	}

	public User getUser() {
		return user;
	}

	private void buildAutorities(User user) {
		this.user = user;
		if (!StringUtils.isEmpty(user.getRoles())) {
			this.autorities = AuthorityUtils.commaSeparatedStringToAuthorityList(user.getRoles());
		} else {
			this.autorities = AuthorityUtils.NO_AUTHORITIES;
		}
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return autorities;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SimpleUserDetail [user=");
		builder.append(user);
		builder.append(", autorities=");
		builder.append(autorities);
		builder.append("]");
		return builder.toString();
	}
	
}
