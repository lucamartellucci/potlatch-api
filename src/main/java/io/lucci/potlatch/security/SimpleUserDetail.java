package io.lucci.potlatch.security;

import io.lucci.potlatch.persistence.model.UserDBTO;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

public class SimpleUserDetail implements UserDetails {

	private static final long serialVersionUID = 1L;

	private UserDBTO userDBTO;
	private Collection<? extends GrantedAuthority> autorities = AuthorityUtils.NO_AUTHORITIES;
	
	public SimpleUserDetail(UserDBTO userDBTO) {
		buildAutorities(userDBTO);
	}

	public SimpleUserDetail() {
		
	}
	
	public void setUserDBTO(UserDBTO userDBTO) {
		buildAutorities(userDBTO);
	}

	public UserDBTO getUserDBTO() {
		return userDBTO;
	}

	private void buildAutorities(UserDBTO userDBTO) {
		this.userDBTO = userDBTO;
		if (!StringUtils.isEmpty(userDBTO.getRoles())) {
			this.autorities = AuthorityUtils.commaSeparatedStringToAuthorityList(userDBTO.getRoles());
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
		return userDBTO.getPassword();
	}

	@Override
	public String getUsername() {
		return userDBTO.getUsername();
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

}
