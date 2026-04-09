package com.asian.auto.hub.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class UserRolesDto {
	private String id;
	private String firstname;
	private String lastname;
	private String password;
	private String email;
	private String phone;
	private boolean deleted;
	private List<RoleDto> roles;

}
