package com.asian.auto.hub.dto;


import lombok.Data;
import java.time.LocalDate;
import java.util.List;

import com.asian.auto.hub.enums.FuelType;

@Data
public class InvoiceResponseDto {


    private Long carPurchaseId;
    private String numberPlate;
    private String carModel;
    private Double purchaseAmount;
    private LocalDate purchaseDate;
    private String purchaseFrom;
    private Double saledAmount;
    private LocalDate saledDate;
    private String purchasedByName;
    private String colour;
    private FuelType fuelType;
    private Double kmps;
    private Integer manufacturedYear;


    private List<UserExpenseSummary> userExpenseSummaries;

    
    private Double totalExpenseAmount;
    private Double netProfit; 

    @Data
    public static class UserExpenseSummary {
        private String userName;
        private List<ExpenseDetail> expenses;
        private Double userTotalAmount;

        @Data
        public static class ExpenseDetail {
            private String purpose;
            private String paymentMode;
            private String paymentType;
            private Double amount;
            private LocalDate expenseDate;
        }
    }
}