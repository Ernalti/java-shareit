package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequestMapping(path = "/users")
@Validated
public class UserController {
	private final UserService userService;
	private final UserMapper userMapper;

	@Autowired
	public UserController(UserService userService, UserMapper userMapper) {
		this.userService = userService;
		this.userMapper = userMapper;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public UserDto createUser(@Valid @RequestBody UserDto userDto) {
		log.info("Create user {}", userDto);

		User user = userMapper.toUser(userDto);
		User res = userService.createUser(user);
		return userMapper.toUserDto(res);
	}

	@PatchMapping("/{id}")
	public UserDto updateUser(@PathVariable Integer id, @RequestBody UserDto userDto) {
		log.info("Update user {}", userDto);
		User user = userMapper.toUser(userDto);
		User res = userService.updateUser(id,user);
		return userMapper.toUserDto(res);
	}

	@GetMapping("/{id}")
	public UserDto getUserById(@PathVariable Integer id) {
		log.info("Get user by id {}", id);
		User res = userService.getUserById(id);
		return userMapper.toUserDto(res);
	}

	@GetMapping
	public List<UserDto> getAllUsers() {
		log.info("Get all users");
		List<User> res = userService.getAllUsers();
		return userMapper.toListUserDto(res);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteUser(@PathVariable Integer id) {
		log.info("Delete user with id {}",id);
		userService.deleteUser(id);
	}
}
