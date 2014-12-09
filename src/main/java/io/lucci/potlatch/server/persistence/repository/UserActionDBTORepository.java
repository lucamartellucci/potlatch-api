package io.lucci.potlatch.server.persistence.repository;

import io.lucci.potlatch.server.persistence.model.UserActionDBTO;
import io.lucci.potlatch.server.persistence.model.UserActionIdDBTO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserActionDBTORepository extends JpaRepository<UserActionDBTO, UserActionIdDBTO>, JpaSpecificationExecutor<UserActionDBTO> {

	

}