package com.asian.auto.hub.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.asian.auto.hub.dto.UserDto;
import com.asian.auto.hub.dto.UserRolesDto;

@Service
public interface UserService {
	UserRolesDto createUser(UserDto user);

	//List<UserRolesDto> getAllUsers();

	UserRolesDto getUserById(Long id);

	UserRolesDto updateUser(Long id, UserDto user);

	void deleteUser(Long id);
}
