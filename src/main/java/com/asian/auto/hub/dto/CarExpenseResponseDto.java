package com.asian.auto.hub.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.asian.auto.hub.enums.PaymentMode;
import com.asian.auto.hub.enums.PaymentType;

import lombok.Data;

@Data
public class CarExpenseResponseDto {
	
	 private Long id;
   private Double amount;
   private PaymentMode paymentMode;
   private PaymentType paymentType;
   private String purpose;
   private LocalDate expenseDate;
   private Long carPurchaseId;         
   private String carNumberPlate;      
   private String paidByFirstname;          
   private String createdBy;
   private String updatedBy;
   private LocalDateTime createdOn;
   private LocalDateTime updatedOn;

}
