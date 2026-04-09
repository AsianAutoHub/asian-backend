package com.asian.auto.hub.serviceimpl;

import java.time.LocalDateTime;
import java.util.HashSet;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.asian.auto.hub.dto.UserDto;
import com.asian.auto.hub.dto.UserRolesDto;
import com.asian.auto.hub.exception.InvalidDataException;
import com.asian.auto.hub.exception.ResourceConflictException;
import com.asian.auto.hub.exception.ResourceNotFoundException;
import com.asian.auto.hub.mapper.UserMapper;
import com.asian.auto.hub.model.User;
import com.asian.auto.hub.repository.RoleRepository;
import com.asian.auto.hub.repository.UserRepository;
import com.asian.auto.hub.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepo;
	private final UserMapper userMapper;
	private final RoleRepository roleRepo;

	@Override
	public UserRolesDto createUser(UserDto user) {

		if (user == null) {
			throw new InvalidDataException("Received User details as Null");
		}

		if (userRepo.existsByEmail(user.getEmail())) {
			throw new ResourceConflictException("User already exists");
		}

		User userDetails = userMapper.toEntity(user);

		userDetails.setUsername(user.getEmail());
		userDetails.setCreatedBy("system");
		userDetails.setUpdatedBy("system");
		userDetails.setCreatedOn(LocalDateTime.now());
		userDetails.setUpdatedOn(LocalDateTime.now());
		userDetails.setDeleted(false);

		if (user.getRoleIds() != null) {
			userDetails.setRoles(new HashSet<>(roleRepo.findAllById(user.getRoleIds())));
		}

		User userDetail = userRepo.save(userDetails);
		return userMapper.toDto(userDetail);
	}
	
	public Page<UserRolesDto> getAllUsers(int page, int size, String search) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("createdOn").descending());

		Page<User> users = (search != null && !search.trim().isEmpty())
				? userRepo.searchPaged(search.trim(), pageable)
				: userRepo.findByDeletedFalse(pageable);

		return users.map(userMapper::toResponseDTO);
	}


//	@Override
//	public List<UserRolesDto> getAllUsers() {
//
//		List<User> users = userRepo.findByDeletedFalse();
//
//		if (users.isEmpty()) {
//			throw new ResourceNotFoundException("No Users Found");
//		}
//
//		return userMapper.toDtoList(users);
//	}

	@Override
	public UserRolesDto getUserById(Long id) {

		User user = userRepo.findByIdAndDeletedFalse(id)
				.orElseThrow(() -> new ResourceNotFoundException("User Not Found With Given Id: " + id));

		return userMapper.toDto(user);
	}

	@Override
	public UserRolesDto updateUser(Long id, UserDto user) {

		User userDetails = userRepo.findByIdAndDeletedFalse(id)
				.orElseThrow(() -> new ResourceNotFoundException("User Not Found With Given Id: " + id));

		if (user.getEmail() != null && !userDetails.getEmail().equals(user.getEmail())
				&& userRepo.existsByEmail(user.getEmail())) {

			throw new ResourceConflictException("Email already exists");
		}

		userMapper.updateUserFromDto(user, userDetails);

		userDetails.setUpdatedOn(LocalDateTime.now());
		userDetails.setDeleted(false);

		if (user.getRoleIds() != null) {
			userDetails.setRoles(new HashSet<>(roleRepo.findAllById(user.getRoleIds())));
		}

		User updatedUser = userRepo.save(userDetails);

		return userMapper.toDto(updatedUser);
	}

	@Override
	public void deleteUser(Long id) {

		User user = userRepo.findByIdAndDeletedFalse(id)
				.orElseThrow(() -> new ResourceNotFoundException("User Not Found With Given Id: " + id));

		user.setDeleted(true);
		user.setUpdatedOn(LocalDateTime.now());
		user.setUpdatedBy("system");

		userRepo.save(user);
	}

}
