package com.asian.auto.hub.dto;

import java.time.LocalDate;

import com.asian.auto.hub.enums.PaymentMode;
import com.asian.auto.hub.enums.PaymentType;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CarExpenseDto {    
    
    
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private Double amount;

    @NotNull(message = "Payment mode is required")
    private PaymentMode paymentMode;

    @NotNull(message = "Payment type is required")
    private PaymentType paymentType;

    @NotNull(message = "Purpose is required")
    private String purpose;

    @NotNull(message = "Expense date is required")
    private LocalDate expenseDate;

    @NotNull(message = "Car purchase ID is required")
    private Long carPurchaseId;

    @NotNull(message = "Paid by user ID is required")
    private Long paidById;
}