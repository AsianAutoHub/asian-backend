package com.asian.auto.hub.serviceimpl;

import com.asian.auto.hub.dto.InvoiceResponseDto;
import com.asian.auto.hub.dto.InvoiceResponseDto.UserExpenseSummary;
import com.asian.auto.hub.dto.InvoiceResponseDto.UserExpenseSummary.ExpenseDetail;
import com.asian.auto.hub.enums.PaymentType;
import com.asian.auto.hub.exception.ResourceNotFoundException;
import com.asian.auto.hub.model.CarExpense;
import com.asian.auto.hub.model.CarPurchase;
import com.asian.auto.hub.repository.CarPurchaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl {

	private final CarPurchaseRepository carPurchaseRepository;

	public InvoiceResponseDto buildInvoiceData(Long carPurchaseId) {
		CarPurchase car = carPurchaseRepository.findByIdWithFilteredExpenses(carPurchaseId,PaymentType.DR)
				.orElseThrow(() -> new ResourceNotFoundException("CarPurchase not found with id: " + carPurchaseId));

		InvoiceResponseDto invoice = new InvoiceResponseDto();

		// ── Section 1: Car Purchase Details ──
		invoice.setCarPurchaseId(car.getId());
		invoice.setNumberPlate(car.getNumberPlate());
		invoice.setCarModel(car.getModel());
		invoice.setPurchaseAmount(car.getPurchaseAmount());
		invoice.setPurchaseDate(car.getPurchaseDate());
		invoice.setPurchaseFrom(car.getPurchaseFrom());
		invoice.setSaledAmount(car.getSaledAmount());
		invoice.setSaledDate(car.getSaledDate());
		invoice.setPurchasedByName(car.getPurchasedBy() != null ? car.getPurchasedBy().getFirstname() : "N/A");
    invoice.setColour(car.getColour());
    invoice.setKmps(car.getKmps());
    invoice.setManufacturedYear(car.getManufacturedYear());
    invoice.setFuelType(car.getFuelType());
		
		// ── Section 2: Expenses grouped by user ──
		List<CarExpense> expenses = car.getExpenses();

		Map<String, List<CarExpense>> expensesByUser = expenses.stream()
				.collect(Collectors.groupingBy(e -> e.getPaidBy() != null ? e.getPaidBy().getFirstname() : "Unknown"));

		List<UserExpenseSummary> summaries = expensesByUser.entrySet().stream().map(entry -> {
			UserExpenseSummary summary = new UserExpenseSummary();
			summary.setUserName(entry.getKey());

			List<ExpenseDetail> details = entry.getValue().stream().map(e -> {
				ExpenseDetail detail = new ExpenseDetail();
				detail.setPurpose(e.getPurpose());
				detail.setPaymentMode(e.getPaymentMode().name());
				detail.setPaymentType(e.getPaymentType().name());
				detail.setExpenseDate(e.getExpenseDate());

				// CR → positive amount, DR → negative amount
				double signedAmount = e.getPaymentType() == PaymentType.CR ? e.getAmount() : e.getAmount();
				detail.setAmount(signedAmount);

				return detail;
			}).toList();

			summary.setExpenses(details);

			// Total: sum CR, deduct DR
			double userTotal = entry.getValue().stream()
					.mapToDouble(e -> e.getPaymentType() == PaymentType.CR ? e.getAmount() : e.getAmount()).sum();
			summary.setUserTotalAmount(userTotal);

			return summary;
		}).toList();

		invoice.setUserExpenseSummaries(summaries);

		double totalExpense = expenses.stream()
				.mapToDouble(e -> e.getPaymentType() == PaymentType.DR ? e.getAmount() : e.getAmount()) // CR reduces expense
				.sum();
		invoice.setTotalExpenseAmount(totalExpense);

		double saledAmount = car.getSaledAmount() != null ? car.getSaledAmount() : 0.0;
		double purchaseAmount = car.getPurchaseAmount() != null ? car.getPurchaseAmount() : 0.0;
	//invoice.setNetProfit(saledAmount - (purchaseAmount + totalExpense));
		invoice.setNetProfit(saledAmount - totalExpense);


		return invoice;
	}
}
