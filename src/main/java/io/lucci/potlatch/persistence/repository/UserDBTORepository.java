package io.lucci.potlatch.persistence.repository;

import io.lucci.potlatch.persistence.model.UserDBTO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserDBTORepository extends JpaRepository<UserDBTO, Long>, JpaSpecificationExecutor<UserDBTO> {

	public UserDBTO findByUsername(String username);
	
}