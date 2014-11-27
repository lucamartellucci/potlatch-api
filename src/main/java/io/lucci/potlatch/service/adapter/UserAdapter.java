package io.lucci.potlatch.service.adapter;

import io.lucci.potlatch.persistence.model.UserDBTO;
import io.lucci.potlatch.web.model.User;
import io.lucci.potlatch.web.security.SimpleUserDetails;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class UserAdapter {
	
	private final static Logger logger = LoggerFactory.getLogger(UserAdapter.class);

	public User dbtoToTo(UserDBTO userDBTO) {
		logger.debug("userDBTO {}", userDBTO);
		if (userDBTO == null) {
			return null;
		}
		User user = new User();
		BeanUtils.copyProperties(userDBTO, user, "gifts", "birthdate");
		user.setBirthdate(userDBTO.getBirthdate());
		return user;
	}

	public UserDetails dbtoToUserDetails(UserDBTO userDBTO) {
		return new SimpleUserDetails(dbtoToTo(userDBTO));
	}

	public UserDBTO toTOdbto(User user) {
		logger.debug("user {}", user);
		if (user == null) {
			return null;
		}
		UserDBTO userDBTO = new UserDBTO();
		BeanUtils.copyProperties(user,userDBTO, "gifts", "birthdate");
		return userDBTO;
	}

}
