package com.asian.auto.hub.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.asian.auto.hub.enums.PaymentMode;
import com.asian.auto.hub.enums.PaymentType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "car_expense")
@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class CarExpense {
	
	 @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

	 @Column(name = "expense_amount")
   private Double amount;

	 @Column(name = "payment_mode")
   @Enumerated(EnumType.STRING)
   private PaymentMode paymentMode;

	 @Column(name = "payment_type")
   @Enumerated(EnumType.STRING)
   private PaymentType paymentType;

	 @Column(name = "expense_purpose")
   private String purpose;
	 
	 @Column(name = "expense_date")
   private LocalDate expenseDate;
	 
	 @Column(name = "deleted")
		private boolean deleted;
	 

   @ManyToOne
   @JoinColumn(name = "car_id")
   private CarPurchase carPurchase;

   @ManyToOne
   @JoinColumn(name = "paid_by")
   private User paidBy;
   
 	@Column(name = "created_by")
  private String created_by;
  
  @Column(name = "updated_by")
  private String updated_by;
  
  @Column(name = "updated_on")
  private LocalDateTime updatedOn;

  @Column(name = "created_on")
  private LocalDateTime createdOn;


}
