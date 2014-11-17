package io.lucci.potlatch.persistence.repository;

import io.lucci.potlatch.persistence.model.GiftDBTO;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(propagation = Propagation.REQUIRED)
public interface GiftDBTORepository extends JpaRepository<GiftDBTO, Long>, JpaSpecificationExecutor<GiftDBTO> {

	public GiftDBTO findByUuid(String uuid);
	
	@Query(name = "Gift.findAllByUserId")
    List<GiftDBTO> findAllByUserId(Long userId);

    @Query(name = "Gift.findAllByUserId", countName = "Gift.countElements")
	Page<GiftDBTO> findAllByUserId(Long userId, Pageable pageable);
    
	@Query(name = "Gift.findAllByUserIdFilterInappropriate")
    List<GiftDBTO> findAllByUserIdFilterInappropriate(Long userId);

    @Query(name = "Gift.findAllByUserIdFilterInappropriate", countName = "Gift.countElementsFilterInappropriate")
	Page<GiftDBTO> findAllByUserIdFilterInappropriate(Long userId, Pageable pageable);
	
}