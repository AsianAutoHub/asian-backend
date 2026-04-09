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
import com.asian.auto.hub.dto.CarPurchaseDto;
import com.asian.auto.hub.dto.CarPurchaseResponseDto;
import com.asian.auto.hub.dto.PagedResponse;
import com.asian.auto.hub.serviceimpl.CarPurchaseServiceImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/car-purchases")
@RequiredArgsConstructor
public class CarPurchaseController {
	private final CarPurchaseServiceImpl carPurchaseService;

	@PostMapping("/add")
	public ResponseEntity<ApiResponse<CarPurchaseResponseDto>> create(@Valid @RequestBody CarPurchaseDto dto) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(ApiResponse.created("Car purchase created successfully", carPurchaseService.createCarPurchase(dto)));
	}

//	@GetMapping("/get-all")
//	public ResponseEntity<ApiResponse<List<CarPurchaseResponseDto>>> getAll() {
//		return ResponseEntity.ok(ApiResponse.success(carPurchaseService.getAllCarPurchases()));
//	}
	
	@GetMapping("/get-all")
	public ResponseEntity<ApiResponse<PagedResponse<CarPurchaseResponseDto>>> getAll(
	        @RequestParam(defaultValue = "0")  int page,
	        @RequestParam(defaultValue = "10") int size,
	        @RequestParam(defaultValue = "")   String search) {

	    Page<CarPurchaseResponseDto> result =
	            carPurchaseService.getAllCarPurchases(page, size, search);

	    return ResponseEntity.ok(ApiResponse.paged(result));
	}

	@GetMapping("/get/{id}")
	public ResponseEntity<ApiResponse<CarPurchaseResponseDto>> getById(@PathVariable Long id) {
		return ResponseEntity.ok(ApiResponse.success(carPurchaseService.getCarPurchaseById(id)));
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<ApiResponse<List<CarPurchaseResponseDto>>> getByUser(@PathVariable Long userId) {
		return ResponseEntity.ok(ApiResponse.success(carPurchaseService.getCarPurchasesByUser(userId)));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<CarPurchaseResponseDto>> update(@PathVariable Long id,
			@Valid @RequestBody CarPurchaseDto dto) {
		return ResponseEntity
				.ok(ApiResponse.success("Car purchase updated successfully", carPurchaseService.updateCarPurchase(id, dto)));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
		carPurchaseService.deleteCarPurchase(id);
		return ResponseEntity.ok(ApiResponse.deleted("Car purchase deleted successfully"));
	}

}
