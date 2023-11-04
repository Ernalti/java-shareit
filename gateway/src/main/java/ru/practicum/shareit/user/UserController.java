package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.ValidationGroups;

import javax.validation.Valid;

/**
 * TODO Sprint add-controllers.
 */
@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
	private final UserClient userClient;

	@PostMapping
	@Validated(ValidationGroups.CreateUser.class)
	public ResponseEntity<Object> createUser(@Valid @RequestBody UserDto userDto) {
		log.info("Create user {}", userDto);
		return userClient.createUser(userDto);
	}

	@PatchMapping("/{id}")
	@Validated(ValidationGroups.UpdateUser.class)
	public ResponseEntity<Object> updateUser(@PathVariable int id, @Valid @RequestBody UserDto userDto) {
		log.info("Update user {}", userDto);
		return userClient.updateUser(id, userDto);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Object> getUserById(@PathVariable int id) {
		log.info("Get user by id {}", id);
		return userClient.getUserById(id);
	}

	@GetMapping
	public ResponseEntity<Object> getAllUsers() {
		log.info("Get all users");
		return userClient.getAllUsers();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Object> deleteUser(@PathVariable int id) {
		log.info("Delete user with id {}", id);
		return userClient.deleteUser(id);
	}

}
