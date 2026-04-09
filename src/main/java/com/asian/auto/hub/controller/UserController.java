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
import com.asian.auto.hub.dto.CarPurchaseResponseDto;
import com.asian.auto.hub.dto.PagedResponse;
import com.asian.auto.hub.dto.UserDto;
import com.asian.auto.hub.dto.UserRolesDto;
import com.asian.auto.hub.serviceimpl.UserServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

	private final UserServiceImpl userService;

	@PostMapping("/add")
	public ResponseEntity<ApiResponse<UserRolesDto>> createUser(@RequestBody UserDto user) {

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(ApiResponse.created("User created successfully", userService.createUser(user)));
	}

	@GetMapping("/get-all")
  public ResponseEntity<ApiResponse<PagedResponse<UserRolesDto>>> getAll(
          @RequestParam(defaultValue = "0")  int page,
          @RequestParam(defaultValue = "10") int size,
          @RequestParam(defaultValue = "")   String search) {
      Page<UserRolesDto> result = userService.getAllUsers(page, size, search);
      return ResponseEntity.ok(ApiResponse.paged(result));
  }
	
	
//	@GetMapping("/get-all")
//	public ResponseEntity<ApiResponse<List<UserRolesDto>>> getAllUsers() {
//		return ResponseEntity.ok(ApiResponse.success(userService.getAllUsers()));
//	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<UserRolesDto>> getUser(@PathVariable Long id) {
		return ResponseEntity.ok(ApiResponse.success(userService.getUserById(id)));
	}

	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<UserRolesDto>> updateUser(@PathVariable Long id, @RequestBody UserDto user) {
		return ResponseEntity.ok(ApiResponse.success("User updated successfully", userService.updateUser(id, user)));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
		userService.deleteUser(id);
		return ResponseEntity.ok(ApiResponse.deleted("User deleted successfully"));
	}

}
