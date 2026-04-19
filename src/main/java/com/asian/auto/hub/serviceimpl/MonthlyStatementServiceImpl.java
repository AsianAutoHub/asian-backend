package com.asian.auto.hub.serviceimpl;


import com.asian.auto.hub.dto.MonthlyStatementResponseDto;
import com.asian.auto.hub.dto.MonthlyStatementResponseDto.CarPurchaseStatDto;
import com.asian.auto.hub.dto.MonthlyStatementResponseDto.UserMetricRangeDto;
import com.asian.auto.hub.model.CarExpense;
import com.asian.auto.hub.model.CarPurchase;
import com.asian.auto.hub.model.User;
import com.asian.auto.hub.repository.CarExpenseRepository;
import com.asian.auto.hub.repository.CarPurchaseRepository;
import com.asian.auto.hub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MonthlyStatementServiceImpl {

    private final CarPurchaseRepository carPurchaseRepository;
    private final CarExpenseRepository  carExpenseRepository;
    private final UserRepository        userRepository;

    public MonthlyStatementResponseDto generateStatement(
            LocalDate fromDate, LocalDate toDate) {

        // ── Fetch data in range ──
        List<CarPurchase> purchases = carPurchaseRepository
                .findByDateRange(fromDate, toDate);
        List<CarExpense>  expenses  = carExpenseRepository
                .findByDateRange(fromDate, toDate);
        List<User>        users     = userRepository.findByDeletedFalse();

        // ── Car Purchase summaries ──
        List<CarPurchaseStatDto> carStats = purchases.stream().map(p -> {

            // total expenses for this car in range
            double carExpenses = expenses.stream()
                    .filter(e -> e.getCarPurchase() != null
                              && e.getCarPurchase().getId().equals(p.getId()))
                    .mapToDouble(e -> "DR".equals(e.getPaymentType().name())
                            ? e.getAmount() : -e.getAmount())
                    .sum();

            double saleAmt     = p.getSaledAmount()    != null ? p.getSaledAmount()    : 0.0;
            double purchaseAmt = p.getPurchaseAmount()  != null ? p.getPurchaseAmount() : 0.0;
            double netProfit   = saleAmt - purchaseAmt - carExpenses;

            return new CarPurchaseStatDto(
                    p.getNumberPlate(),
                    p.getModel(),
                    p.getPurchaseFrom(),
                    p.getPurchaseAmount(),
                    p.getPurchaseDate()  != null ? p.getPurchaseDate().toString()  : "N/A",
                    p.getSaledAmount(),
                    p.getSaledDate()     != null ? p.getSaledDate().toString()     : "N/A",
                    p.getPurchasedBy()   != null
                        ? p.getPurchasedBy().getFirstname() + " "
                          + p.getPurchasedBy().getLastname() : "N/A",
                    carExpenses,
                    netProfit,
                    p.getSaledAmount() != null ? "SOLD" : "AVAILABLE"
            );
        }).toList();

        // ── Overall purchase totals ──
        double totalPurchaseAmt = purchases.stream()
                .mapToDouble(p -> p.getPurchaseAmount() != null ? p.getPurchaseAmount() : 0)
                .sum();
        double totalSaleAmt = purchases.stream()
                .filter(p -> p.getSaledAmount() != null)
                .mapToDouble(CarPurchase::getSaledAmount)
                .sum();
        double totalExpensesInRange = expenses.stream()
                .mapToDouble(e -> "DR".equals(e.getPaymentType().name())
                        ? e.getAmount() : -e.getAmount())
                .sum();
        double totalProfit = totalSaleAmt - totalPurchaseAmt - totalExpensesInRange;

        // ── DR / CR totals ──
        double totalDR = expenses.stream()
                .filter(e -> "DR".equals(e.getPaymentType().name()))
                .mapToDouble(CarExpense::getAmount).sum();
        double totalCR = expenses.stream()
                .filter(e -> "CR".equals(e.getPaymentType().name()))
                .mapToDouble(CarExpense::getAmount).sum();

        // ── Per-user metrics in range ──
        List<UserMetricRangeDto> userMetrics = users.stream().map(u -> {
            List<CarExpense> userExpenses = carExpenseRepository
                    .findByUserAndDateRange(u.getId(), fromDate, toDate);

            double dr = userExpenses.stream()
                    .filter(e -> "DR".equals(e.getPaymentType().name()))
                    .mapToDouble(CarExpense::getAmount).sum();
            double cr = userExpenses.stream()
                    .filter(e -> "CR".equals(e.getPaymentType().name()))
                    .mapToDouble(CarExpense::getAmount).sum();
            double net      = dr - cr;
            double invested = u.getAmountInvested() != null ? u.getAmountInvested() : 0.0;
            double balance  = invested - net;

            return new UserMetricRangeDto(
                    u.getFirstname() + " " + u.getLastname(),
                    u.getEmail(),
                    invested, dr, cr, net, balance
            );
        })
        // only include users who have activity in range
        .filter(u -> u.getTotalDr() > 0 || u.getTotalCr() > 0)
        .toList();

        return new MonthlyStatementResponseDto(
                fromDate.toString(),
                toDate.toString(),
                purchases.size(),
                (int) purchases.stream().filter(p -> p.getSaledAmount() != null).count(),
                totalPurchaseAmt,
                totalSaleAmt,
                totalProfit,
                totalExpensesInRange,
                totalDR,
                totalCR,
                carStats,
                userMetrics
        );
    }
}
