package org.springframework.samples.petclinic.mapper;

import java.util.Collection;

import org.mapstruct.Mapper;
import org.springframework.samples.petclinic.model.Role;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.rest.dto.RoleDto;
import org.springframework.samples.petclinic.rest.dto.UserDto;

@Mapper
public interface UserMapper {
	Role toRole(RoleDto roleDto);

	RoleDto toRoleDto(Role role);

	Collection<RoleDto> toRoleDtos(Collection<Role> roles);

	User toUser(UserDto userDto);

	UserDto toUserDto(User user);

	Collection<Role> toRoles(Collection<RoleDto> roleDtos);

}
