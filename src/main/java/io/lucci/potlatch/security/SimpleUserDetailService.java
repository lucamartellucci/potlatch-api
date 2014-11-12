package io.lucci.potlatch.security;

import io.lucci.potlatch.persistence.model.UserDBTO;
import io.lucci.potlatch.persistence.repository.UserDBTORepository;
import io.lucci.potlatch.service.adapter.UserAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SimpleUserDetailService implements UserDetailsService {
	
	final static Logger logger = LoggerFactory.getLogger(SimpleUserDetailService.class);
	
	@Autowired
	private UserDBTORepository userDBTORepository;
	
	@Autowired
	private UserAdapter userAdapter;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.info("Loading User by username {}", username);
		// load user
		UserDBTO userDBTO = userDBTORepository.findByUsername(username);
		if (userDBTO == null) {
			throw new UsernameNotFoundException(new StringBuilder("Unable to find a User with username ").append(username).toString());
		}
		
		UserDetails userDetails = userAdapter.dbtoToUserDetails(userDBTO);
		logger.debug("Found User {}", userDetails);
		return userDetails;
	}

}