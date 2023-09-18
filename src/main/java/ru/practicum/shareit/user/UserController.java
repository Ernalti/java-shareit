package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import javax.validation.Valid;
import java.util.List;

import static ru.practicum.shareit.user.mapper.UserMapper.*;

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
	public UserDto createUser(@Valid @RequestBody UserDto userDto) {
		log.info("Create user {}", userDto);

		User user =toUser(userDto);
		User res = userService.createUser(user);
		return toUserDto(res);
	}

	@PatchMapping("/{id}")
	public UserDto updateUser(@PathVariable Integer id, @RequestBody UserDto userDto) {
		log.info("Update user {}", userDto);
		User user = toUser(userDto);
		User res = userService.updateUser(id,user);
		return toUserDto(res);
	}

	@GetMapping("/{id}")
	public UserDto getUserById(@PathVariable Integer id) {
		log.info("Get user by id {}", id);
		User res =userService.getUserById(id);
		return toUserDto(res);
	}

	@GetMapping
	public List<UserDto> getAllUsers() {
		log.info("Get all users");
		List<User> res = userService.getAllUsers();
		return toListUserDto(res);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteUser(@PathVariable Integer id) {
		log.info("Delete user with id {}",id);
		userService.deleteUser(id);
	}
}
