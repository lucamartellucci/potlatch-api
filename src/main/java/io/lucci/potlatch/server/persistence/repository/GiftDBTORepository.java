package io.lucci.potlatch.server.persistence.repository;

import io.lucci.potlatch.server.persistence.model.GiftDBTO;

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

	@Query(name = "Gift.findAllByUserId")
    List<GiftDBTO> findAllByUserId(Long userId);
	
	@Query(name = "Gift.findAllByUserId", countName = "Gift.countByUserId")
	Page<GiftDBTO> findAllByUserId(Long userId, Pageable pageable);

	@Query(name = "Gift.findAllByUserIdAndTitle")
	List<GiftDBTO> findAllByUserIdAndTitle(Long userId, String title);
    
    @Query(name = "Gift.findAllByUserIdAndTitle", countName = "Gift.countByUserIdAndTitle")
    List<GiftDBTO> findAllByUserIdAndTitle(Long userId, String title, Pageable pageable);
    
	@Query(name = "Gift.findAllByUserIdAndInappropriate")
    List<GiftDBTO> findAllByUserIdRemoveInappropriate(Long userId);
	
    @Query(name = "Gift.findAllByUserIdAndInappropriate", countName = "Gift.countByUserIdAndInappropriate")
	Page<GiftDBTO> findAllByUserIdRemoveInappropriate(Long userId, Pageable pageable);
    
//    @Query(name = "Gift.findAllByUserIdAndInappropriateAndTitle")
//    List<GiftDBTO> findAllByUserIdAndTitleRemoveInappropriate(Long userId, String title);
//
//    @Query(name = "Gift.findAllByUserIdAndInappropriateAndTitle", countName = "Gift.countByUserIdAndInappropriateAndTitle")
//    Page<GiftDBTO> findAllByUserIdAndTitleRemoveInappropriate(Long userId, String title, Pageable pageable);
	
}