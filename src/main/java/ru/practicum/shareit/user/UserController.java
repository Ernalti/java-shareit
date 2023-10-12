package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.ValidationGroups;
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

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Validated(ValidationGroups.CreateUser.class)
	public UserDto createUser(@Valid @RequestBody UserDto userDto) {
		log.info("Create user {}", userDto);
		return userService.createUser(userDto);
	}

	@PatchMapping("/{id}")
	@Validated(ValidationGroups.UpdateUser.class)
	public UserDto updateUser(@PathVariable int id, @Valid @RequestBody UserDto userDto) {
		log.info("Update user {}", userDto);
		return userService.updateUser(id, userDto);
	}

	@GetMapping("/{id}")
	public UserDto getUserById(@PathVariable int id) {
		log.info("Get user by id {}", id);
		return userService.getUserById(id);
	}

	@GetMapping
	public List<UserDto> getAllUsers() {
		log.info("Get all users");
		return userService.getAllUsers();
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteUser(@PathVariable int id) {
		log.info("Delete user with id {}",id);
		userService.deleteUser(id);
	}

	@DeleteMapping
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void clearUsers() {
		log.info("Delete all users");
		userService.clearUsers();
	}
}
