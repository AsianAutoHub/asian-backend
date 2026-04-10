package com.asian.auto.hub.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.asian.auto.hub.model.CarPurchase;
import com.asian.auto.hub.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);

	boolean existsByEmailAndDeletedFalse(String email);

	List<User> findByDeletedFalse();

	Optional<User> findByIdAndDeletedFalse(Long id);
	
	Page<User> findByDeletedFalse(Pageable pageable);
	
	@Query("""
      SELECT c FROM User c
      WHERE c.deleted = false
      AND (
             LOWER(c.firstname)  LIKE LOWER(CONCAT('%', :search, '%'))
          OR LOWER(c.email)        LIKE LOWER(CONCAT('%', :search, '%'))
          OR LOWER(c.phone) LIKE LOWER(CONCAT('%', :search, '%'))
      )
  """)
  Page<User> searchPaged(@Param("search") String search, Pageable pageable);

}
