package ru.practicum.shareit.user.storage;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.exception.exceptions.EntityAlreadyExistsException;
import ru.practicum.shareit.exception.exceptions.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@Validated
public class InMemoryUserStorage implements UserStorage {

	private final Map<Integer, User> users = new HashMap<>();
	private Integer id;

	public InMemoryUserStorage() {
		this.id = 1;
	}

	@Override
	public User createUser(User user) {

		testUnique(user);

		user.setId(nextId());
		users.put(user.getId(), user);
		log.info("User created in memory {}",user);
		return user;
	}

	@Override
	public User updateUser(Integer id, User user) {
		user.setId(id);
		testUnique(user);
		User updateUser = getUserById(id);

		String name = user.getName();
		if (name != null && !name.isBlank()) {
			updateUser.setName(name);
		}

		String email = user.getEmail();
		if (email != null && !email.isBlank()) {
			validEmail(email);
			updateUser.setEmail(email);
		}
		log.info("User updated in memory {}",user);

		return updateUser;
	}

	@Override
	public User getUserById(Integer id) {
		User user = users.get(id);
		if (user == null) {
			throw new NotFoundException("User with id " + id + " not found");
		}
		log.info("Get user by id  in memory {}",user);
		return user;
	}

	@Override
	public List<User> getAllUsers() {
		log.info("Get all users in memory");
		return new ArrayList<>(users.values());
	}

	@Override
	public void deleteUser(Integer id) {
		if (!users.containsKey(id)) {
			throw new NotFoundException("User with id " + id + " not found");
		}
		log.info("User with id {} deleted",id);
		users.remove(id);
	}

	@Override
	public void clearUsers() {
		users.clear();
		id = 1;
		log.info("Clear users");
	}

	private Integer nextId() {
		return id++;
	}

	private void testUnique(User user) {
		boolean nameNotUnique = user.getName() != null &&
				users.values()
						.stream()
						.anyMatch(u -> u.getName().equals(user.getName()) && !u.getId().equals(user.getId()));

		boolean emailNotUnique = user.getEmail() != null &&
				users.values()
						.stream()
						.anyMatch(u -> u.getEmail().equals(user.getEmail()) && !u.getId().equals(user.getId()));

		if (nameNotUnique) {
			throw new EntityAlreadyExistsException("User with name " + user.getName() + " already exist");
		}
		if (emailNotUnique) {
			throw new EntityAlreadyExistsException("User with email " + user.getName() + " already exist");
		}
	}

	private void validEmail(String email) {
		if (email != null) {
			boolean isEmailValid = email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
			if (!isEmailValid) {
				throw new IllegalArgumentException("Invalid email format: " + email);
			}
		}
	}
}
