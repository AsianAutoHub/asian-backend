package com.asian.auto.hub.serviceimpl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.asian.auto.hub.dto.CarPurchaseDto;
import com.asian.auto.hub.dto.CarPurchaseResponseDto;
import com.asian.auto.hub.exception.ResourceNotFoundException;
import com.asian.auto.hub.mapper.CarPurchaseMapper;
import com.asian.auto.hub.model.CarPurchase;
import com.asian.auto.hub.model.User;
import com.asian.auto.hub.repository.CarPurchaseRepository;
import com.asian.auto.hub.repository.UserRepository;
import com.asian.auto.hub.service.CarPurchaseService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CarPurchaseServiceImpl implements CarPurchaseService {

	private final CarPurchaseRepository carPurchaseRepository;
	private final UserRepository userRepository;
	private final CarPurchaseMapper carPurchaseMapper;

	public CarPurchaseResponseDto createCarPurchase(CarPurchaseDto dto) {
		User purchasedBy = fetchUser(dto.getPurchasedById());

		CarPurchase carPurchase = carPurchaseMapper.toEntity(dto);
		carPurchase.setPurchasedBy(purchasedBy);
		carPurchase.setCreated_by("system");
		carPurchase.setCreatedOn(LocalDateTime.now());

		return carPurchaseMapper.toResponseDTO(carPurchaseRepository.save(carPurchase));
	}

//	public List<CarPurchaseResponseDto> getAllCarPurchases() {
//		return carPurchaseRepository.findByDeletedFalse().stream().map(carPurchaseMapper::toResponseDTO).toList();
//	}

	public Page<CarPurchaseResponseDto> getAllCarPurchases(int page, int size, String search) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("createdOn").descending());

		Page<CarPurchase> result = (search != null && !search.trim().isEmpty())
				? carPurchaseRepository.searchPaged(search.trim(), pageable)
				: carPurchaseRepository.findByDeletedFalse(pageable);

		return result.map(carPurchaseMapper::toResponseDTO);
	}

	public CarPurchaseResponseDto getCarPurchaseById(Long id) {
		return carPurchaseMapper.toResponseDTO(fetchCarPurchase(id));
	}

	public List<CarPurchaseResponseDto> getCarPurchasesByUser(Long userId) {
		return carPurchaseRepository.findByPurchasedByIdAndDeletedFalse(userId).stream()
				.map(carPurchaseMapper::toResponseDTO).toList();
	}

	public CarPurchaseResponseDto updateCarPurchase(Long id, CarPurchaseDto dto) {
		CarPurchase existing = fetchCarPurchase(id);
		User purchasedBy = fetchUser(dto.getPurchasedById());

		carPurchaseMapper.updateEntityFromDTO(dto, existing);
		existing.setPurchasedBy(purchasedBy);
		existing.setUpdated_by("system");
		existing.setUpdatedOn(LocalDateTime.now());

		return carPurchaseMapper.toResponseDTO(carPurchaseRepository.save(existing));
	}

	public void deleteCarPurchase(Long id) {
		CarPurchase carPurchaseDetails = fetchCarPurchase(id);
		carPurchaseDetails.setDeleted(true);
		carPurchaseRepository.save(carPurchaseDetails);
	}

	private CarPurchase fetchCarPurchase(Long id) {
		return carPurchaseRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("CarPurchase not found with id: " + id));

	}

	private User fetchUser(Long userId) {
		return userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
	}

}
