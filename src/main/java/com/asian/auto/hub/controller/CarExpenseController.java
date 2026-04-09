package com.asian.auto.hub.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.asian.auto.hub.apiresponse.ApiResponse;
import com.asian.auto.hub.dto.CarExpenseDto;
import com.asian.auto.hub.dto.CarExpenseResponseDto;
import com.asian.auto.hub.dto.PagedResponse;
import com.asian.auto.hub.serviceimpl.CarExpenseServiceImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/car-expenses")
@RequiredArgsConstructor
public class CarExpenseController {

	private final CarExpenseServiceImpl carExpenseService;

	@PostMapping("/add")
	public ResponseEntity<ApiResponse<CarExpenseResponseDto>> create(@Valid @RequestBody CarExpenseDto dto) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(ApiResponse.created("Car expense created successfully", carExpenseService.createCarExpense(dto)));
	}

//	@GetMapping("/get-all")
//	public ResponseEntity<ApiResponse<List<CarExpenseResponseDto>>> getAll() {
//		return ResponseEntity.ok(ApiResponse.success(carExpenseService.getAllExpenses()));
//	}

	@GetMapping("/get-all")
	public ResponseEntity<ApiResponse<PagedResponse<CarExpenseResponseDto>>> getAll(
	        @RequestParam(defaultValue = "0")  int page,
	        @RequestParam(defaultValue = "10") int size,
	        @RequestParam(defaultValue = "")   String search) {

	    Page<CarExpenseResponseDto> result =
	            carExpenseService.getAllExpenses(page, size, search);

	    return ResponseEntity.ok(ApiResponse.paged(result));
	}
	
	@GetMapping("/get/{id}")
	public ResponseEntity<ApiResponse<CarExpenseResponseDto>> getById(@PathVariable Long id) {
		return ResponseEntity.ok(ApiResponse.success(carExpenseService.getExpenseById(id)));
	}

	@GetMapping("/car/{carId}")
	public ResponseEntity<ApiResponse<List<CarExpenseResponseDto>>> getByCar(@PathVariable Long carId) {
		return ResponseEntity.ok(ApiResponse.success(carExpenseService.getExpensesByCarId(carId)));
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<ApiResponse<List<CarExpenseResponseDto>>> getByUser(@PathVariable Long userId) {
		return ResponseEntity.ok(ApiResponse.success(carExpenseService.getExpensesByUser(userId)));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<CarExpenseResponseDto>> update(@PathVariable Long id,
			@Valid @RequestBody CarExpenseDto dto) {
		return ResponseEntity
				.ok(ApiResponse.success("Car expense updated successfully", carExpenseService.updateCarExpense(id, dto)));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
		carExpenseService.deleteCarExpense(id);
		return ResponseEntity.ok(ApiResponse.deleted("Car expense deleted successfully"));
	}
}
