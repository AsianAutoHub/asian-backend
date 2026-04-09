package com.asian.auto.hub.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.asian.auto.hub.model.CarExpense;

@Repository
public interface CarExpenseRepository extends JpaRepository<CarExpense, Long> {
	List<CarExpense> findByCarPurchaseIdAndDeletedFalse(Long carPurchaseId);

	List<CarExpense> findByPaidByIdAndDeletedFalse(Long userId);

	List<CarExpense> findByDeletedFalse();

	Page<CarExpense> findByDeletedFalse(Pageable pageable);

	@Query("""
			    SELECT e FROM CarExpense e
			    WHERE e.deleted = false
			    AND (
			           LOWER(e.purpose)                 LIKE LOWER(CONCAT('%', :search, '%'))
			        OR LOWER(e.carPurchase.numberPlate)  LIKE LOWER(CONCAT('%', :search, '%'))
			    )
			""")
	Page<CarExpense> searchPaged(@Param("search") String search, Pageable pageable);
}