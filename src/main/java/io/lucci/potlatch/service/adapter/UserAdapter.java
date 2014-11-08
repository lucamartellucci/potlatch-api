package io.lucci.potlatch.service.adapter;

import io.lucci.potlatch.model.User;
import io.lucci.potlatch.persistence.model.UserDBTO;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class UserAdapter {

	public User dbtoToTo(UserDBTO userDBTO) {
		User user = new User();
		BeanUtils.copyProperties(userDBTO, user, "gifts");
		return user;
	}

}
