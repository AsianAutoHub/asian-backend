package com.asian.auto.hub.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.asian.auto.hub.enums.PaymentType;
import com.asian.auto.hub.model.CarPurchase;
import com.asian.auto.hub.model.User;

public interface CarPurchaseRepository extends JpaRepository<CarPurchase, Long> {

	List<CarPurchase> findByPurchasedByIdAndDeletedFalse(Long userId);

	boolean existsByNumberPlate(String numberPlate);
	
	List<CarPurchase> findByDeletedFalse();
	
	@Query("""
      SELECT c FROM CarPurchase c
      WHERE c.deleted = false
      AND (
             LOWER(c.numberPlate)  LIKE LOWER(CONCAT('%', :search, '%'))
          OR LOWER(c.model)        LIKE LOWER(CONCAT('%', :search, '%'))
          OR LOWER(c.purchaseFrom) LIKE LOWER(CONCAT('%', :search, '%'))
      )
  """)
  Page<CarPurchase> searchPaged(@Param("search") String search, Pageable pageable);

  Page<CarPurchase> findByDeletedFalse(Pageable pageable);
  
  @Query("""
      SELECT DISTINCT cp FROM CarPurchase cp
      LEFT JOIN FETCH cp.expenses e
      WHERE cp.id = :id AND e.paymentType = :type
  """)
  Optional<CarPurchase> findByIdWithFilteredExpenses(
      @Param("id") Long id,
      @Param("type") PaymentType type
  );
  
  
//✅ add to existing CarPurchaseRepository
@Query("""
   SELECT c FROM CarPurchase c
   WHERE c.deleted = false
   AND c.purchaseDate BETWEEN :fromDate AND :toDate
   ORDER BY c.purchaseDate ASC
""")
List<CarPurchase> findByDateRange(
   @Param("fromDate") LocalDate fromDate,
   @Param("toDate")   LocalDate toDate
);
}
