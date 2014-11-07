package io.lucci.potlatch.persistence.repository;

import io.lucci.potlatch.persistence.model.UserActionDBTO;
import io.lucci.potlatch.persistence.model.UserActionIdDBTO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserActionDBTORepository extends JpaRepository<UserActionDBTO, UserActionIdDBTO>, JpaSpecificationExecutor<UserActionDBTO> {

	

}