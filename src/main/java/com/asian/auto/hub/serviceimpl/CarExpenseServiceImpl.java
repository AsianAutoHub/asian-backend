package com.asian.auto.hub.serviceimpl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.asian.auto.hub.dto.CarExpenseDto;
import com.asian.auto.hub.dto.CarExpenseResponseDto;
import com.asian.auto.hub.exception.ResourceNotFoundException;
import com.asian.auto.hub.mapper.CarExpenseMapper;
import com.asian.auto.hub.model.CarExpense;
import com.asian.auto.hub.model.CarPurchase;
import com.asian.auto.hub.model.User;
import com.asian.auto.hub.repository.CarExpenseRepository;
import com.asian.auto.hub.repository.CarPurchaseRepository;
import com.asian.auto.hub.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CarExpenseServiceImpl {

	private final CarExpenseRepository carExpenseRepository;
	private final CarPurchaseRepository carPurchaseRepository;
	private final UserRepository userRepository;
	private final CarExpenseMapper carExpenseMapper;

	public CarExpenseResponseDto createCarExpense(CarExpenseDto dto) {
		CarPurchase carPurchase = fetchCarPurchase(dto.getCarPurchaseId());
		User paidBy = fetchUser(dto.getPaidById());

		CarExpense expense = carExpenseMapper.toEntity(dto);
		expense.setCarPurchase(carPurchase);
		expense.setPaidBy(paidBy);
		expense.setCreated_by("system");
		expense.setCreatedOn(LocalDateTime.now());

		return carExpenseMapper.toResponseDTO(carExpenseRepository.save(expense));
	}

//	public List<CarExpenseResponseDto> getAllExpenses() {
//		return carExpenseRepository.findByDeletedFalse().stream().map(carExpenseMapper::toResponseDTO).toList();
//	}
	
	public Page<CarExpenseResponseDto> getAllExpenses(int page, int size, String search) {
    Pageable pageable = PageRequest.of(page, size, Sort.by("createdOn").descending());

    Page<CarExpense> result = (search != null && !search.trim().isEmpty())
            ? carExpenseRepository.searchPaged(search.trim(), pageable)
            : carExpenseRepository.findByDeletedFalse(pageable);

    return result.map(carExpenseMapper::toResponseDTO);
}

	public CarExpenseResponseDto getExpenseById(Long id) {
		return carExpenseMapper.toResponseDTO(fetchExpense(id));
	}

	public List<CarExpenseResponseDto> getExpensesByCarId(Long carId) {
		return carExpenseRepository.findByCarPurchaseIdAndDeletedFalse(carId).stream().map(carExpenseMapper::toResponseDTO).toList();
	}

	public List<CarExpenseResponseDto> getExpensesByUser(Long userId) {
		return carExpenseRepository.findByPaidByIdAndDeletedFalse(userId).stream().map(carExpenseMapper::toResponseDTO).toList();
	}

	public CarExpenseResponseDto updateCarExpense(Long id, CarExpenseDto dto) {
		CarExpense existing = fetchExpense(id);
		CarPurchase carPurchase = fetchCarPurchase(dto.getCarPurchaseId());
		User paidBy = fetchUser(dto.getPaidById());

		carExpenseMapper.updateEntityFromDTO(dto, existing);
		existing.setCarPurchase(carPurchase);
		existing.setPaidBy(paidBy);
		existing.setUpdated_by("system");
		existing.setUpdatedOn(LocalDateTime.now());

		return carExpenseMapper.toResponseDTO(carExpenseRepository.save(existing));
	}

	public void deleteCarExpense(Long id) {
		CarExpense fetchExpense = fetchExpense(id); 
		fetchExpense.setDeleted(true);
		carExpenseRepository.save(fetchExpense);
	}


	private CarExpense fetchExpense(Long id) {
		return carExpenseRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("CarExpense not found with id: " + id));
	}

	private CarPurchase fetchCarPurchase(Long id) {
		return carPurchaseRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("CarPurchase not found with id: " + id));

	}

	private User fetchUser(Long id) {
		return userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
	}
}
