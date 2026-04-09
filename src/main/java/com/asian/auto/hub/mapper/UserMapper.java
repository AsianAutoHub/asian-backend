package com.asian.auto.hub.mapper;

import java.util.List;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.data.domain.Page;

import com.asian.auto.hub.dto.CarPurchaseResponseDto;
import com.asian.auto.hub.dto.RoleDto;
import com.asian.auto.hub.dto.UserDto;
import com.asian.auto.hub.dto.UserRolesDto;
import com.asian.auto.hub.model.CarPurchase;
import com.asian.auto.hub.model.Role;
import com.asian.auto.hub.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

	@Mapping(target = "roles", expression = "java(mapRoles(user.getRoles()))")
	UserRolesDto toDto(User user);

	//List<UserRolesDto> toDtoList(List<User> users);

	@Mapping(target = "roles", ignore = true)
	User toEntity(UserDto dto);

	@Mapping(target = "roles", ignore = true)
	void updateUserFromDto(UserDto dto, @MappingTarget User entity);
	
	UserRolesDto toResponseDTO(User user);
	

	default List<RoleDto> mapRoles(Set<Role> roles) {
		if (roles == null)
			return List.of();

		return roles.stream().map(r -> new RoleDto(r.getId(), r.getRole())).toList();
	}
}