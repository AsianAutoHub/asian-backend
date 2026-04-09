package com.asian.auto.hub.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.asian.auto.hub.model.Role;
import java.util.List;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

	    Page<Role> findByDeletedFalse(Pageable pageable);
	    List<Role> findByDeletedFalse();
	    boolean existsByRole(String role);
	}

