package com.asian.auto.hub.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDto {

	private String firstname;
	private String lastname;
	private String password;
	private String email;
	private String phone;
	private boolean deleted;
	private List<Long> roleIds;

}
