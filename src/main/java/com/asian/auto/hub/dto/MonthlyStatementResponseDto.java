package com.asian.auto.hub.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyStatementResponseDto {

    private String fromDate;
    private String toDate;

    // ── Car Purchase Summary ──
    private int    totalCarsPurchased;
    private int    totalCarsSold;
    private Double totalPurchaseAmount;
    private Double totalSaleAmount;
    private Double totalProfit;

    // ── User Metrics in range ──
    private Double totalExpensesInRange;
    private Double totalDrInRange;
    private Double totalCrInRange;

    // ── Details ──
    private List<CarPurchaseStatDto> carPurchases;
    private List<UserMetricRangeDto> userMetrics;

    // ── Car Purchase detail ──
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CarPurchaseStatDto {
        private String numberPlate;
        private String carModel;
        private String purchaseFrom;
        private Double purchaseAmount;
        private String purchaseDate;
        private Double saledAmount;
        private String saledDate;
        private String purchasedByName;
        private Double totalExpenses;
        private Double netProfit;
        private String status;         // SOLD / AVAILABLE
    }

    // ── Per-user metric in range ──
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserMetricRangeDto {
        private String userName;
        private String email;
        private Double amountInvested;
        private Double totalDr;
        private Double totalCr;
        private Double netExpense;
        private Double balanceAmount;
    }
}