package com.asian.auto.hub.dto;

import lombok.*;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

	private String firstname;
	private String lastname;
	private String password;
	private String email;
	private String phone;
	private Boolean deleted;
	private List<Long> roleIds;
	private Double amountInvested;
}
