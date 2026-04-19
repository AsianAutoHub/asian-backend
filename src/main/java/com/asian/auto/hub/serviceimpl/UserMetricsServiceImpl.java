package com.asian.auto.hub.serviceimpl;

import com.asian.auto.hub.dto.UserMetricsResponseDto;
import com.asian.auto.hub.dto.UserMetricsResponseDto.ExpenseBreakdown;
import com.asian.auto.hub.exception.ResourceNotFoundException;
import com.asian.auto.hub.model.CarExpense;
import com.asian.auto.hub.model.User;
import com.asian.auto.hub.repository.CarExpenseRepository;
import com.asian.auto.hub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserMetricsServiceImpl {

    private final UserRepository       userRepository;
    private final CarExpenseRepository carExpenseRepository;

    public UserMetricsResponseDto getUserMetrics(Long userId) {

        // ── Fetch user ──
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + userId));

        // ── Fetch all expenses paid by this user ──
        List<CarExpense> expenses = carExpenseRepository
                .findByPaidByIdAndDeletedFalse(userId);

        // ── Calculate DR total ──
        double totalDR = expenses.stream()
                .filter(e -> "DR".equalsIgnoreCase(e.getPaymentType().name()))
                .mapToDouble(CarExpense::getAmount)
                .sum();

        // ── Calculate CR total ──
        double totalCR = expenses.stream()
                .filter(e -> "CR".equalsIgnoreCase(e.getPaymentType().name()))
                .mapToDouble(CarExpense::getAmount)
                .sum();

       // double netExpense     = totalDR - totalCR;
        double netExpense     = totalCR - totalDR;
        double amountInvested = user.getAmountInvested() != null
                                    ? user.getAmountInvested() : 0.0;
      //  double balanceAmount  = amountInvested - netExpense;
        double balanceAmount  = (amountInvested - totalDR) + totalCR;

//        // ── Build expense breakdown ──
//        List<ExpenseBreakdown> breakdown = expenses.stream()
//                .map(e -> new ExpenseBreakdown(
//                        e.getCarPurchase() != null
//                            ? e.getCarPurchase().getNumberPlate() : "N/A",
//                        e.getPurpose(),
//                        e.getPaymentMode().name(),
//                        e.getPaymentType().name(),
//                        e.getAmount(),
//                        e.getExpenseDate() != null
//                            ? e.getExpenseDate().toString() : "N/A"
//                ))
//                .toList();
        
        List<ExpenseBreakdown> breakdown= new ArrayList<UserMetricsResponseDto.ExpenseBreakdown>();

        return new UserMetricsResponseDto(
                user.getId(),
                user.getFirstname() + " " + user.getLastname(),
                user.getEmail(),
                amountInvested,
                totalDR,
                totalCR,
                netExpense,
                balanceAmount,
                breakdown
        );
    }

    // ── All users metrics summary (for overview) ──
    public List<UserMetricsResponseDto> getAllUsersMetrics() {
        return userRepository.findByDeletedFalse()
                .stream()
                .map(u -> getUserMetrics(u.getId()))
                .toList();
    }
}