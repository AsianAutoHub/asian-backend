package com.asian.auto.hub.serviceimpl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.asian.auto.hub.dto.UserDto;
import com.asian.auto.hub.dto.UserRolesDto;
import com.asian.auto.hub.service.AuthService;
import com.asian.auto.hub.service.UserService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private  final PasswordEncoder passwordEncoder;


    @Override
    public UserRolesDto registerUser(UserDto userDto) {
        //logic
        //verify email
        //verify password
        //default roles
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));


        return userService.createUser(userDto);
    }
}
