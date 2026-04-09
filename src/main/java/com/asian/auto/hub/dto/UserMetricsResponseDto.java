package com.asian.auto.hub.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserMetricsResponseDto {


	    // User info
	    private Long   userId;
	    private String userName;
	    private String email;

	    // Metrics
	    private Double amountInvested;   // from user table
	    private Double totalDrExpense;   // sum of DR expenses paid by user
	    private Double totalCrExpense;   // sum of CR expenses paid by user
	    private Double netExpense;       // DR - CR
	    private Double balanceAmount;    // amountInvested - netExpense

	    // Expense breakdown
	    private List<ExpenseBreakdown> expenses;

	    @Data
	    @NoArgsConstructor
	    @AllArgsConstructor
	    public static class ExpenseBreakdown {
	        private String carNumberPlate;
	        private String purpose;
	        private String paymentMode;
	        private String paymentType;
	        private Double amount;
	        private String expenseDate;
	    }
	}

