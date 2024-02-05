package org.springframework.samples.petclinic.rest.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.mapper.UserMapper;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.rest.api.UsersApi;
import org.springframework.samples.petclinic.rest.dto.UserDto;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(exposedHeaders = "errors, content-type")
@RequestMapping("api")
public class UserRestController implements UsersApi {

	private final UserService userService;
	private final UserMapper userMapper;

	public UserRestController(UserService userService, UserMapper userMapper) {
		this.userService = userService;
		this.userMapper = userMapper;
	}

	@PreAuthorize("hasRole(@roles.ADMIN)")
	@Override
	public ResponseEntity<UserDto> addUser(UserDto userDto) {
		HttpHeaders headers = new HttpHeaders();
		User user = userMapper.toUser(userDto);
		this.userService.saveUser(user);
		return new ResponseEntity<>(userMapper.toUserDto(user), headers, HttpStatus.CREATED);
	}

}
