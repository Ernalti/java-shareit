package ru.practicum.shareit.user.storage;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.exceptions.EntityAlreadyExistsException;
import ru.practicum.shareit.exception.exceptions.NotFoundException;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.Email;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
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

		@Email
		String email = user.getEmail();
		if (email != null && !email.isBlank()) {
			updateUser.setEmail(email);
		}


		return updateUser;
	}

	@Override
	public User getUserById(Integer id) {
		User user = users.get(id);
		if (user == null) {
			throw new NotFoundException("User with id " + id + " not found");
		}
		return user;
	}

	@Override
	public List<User> getAllUsers() {
		return new ArrayList<>(users.values());
	}

	@Override
	public void deleteUser(Integer id) {
		if (!users.containsKey(id)){
			throw new NotFoundException("User with id " + id + " not found");
		}
		users.remove(id);
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

	private void validEmail(@Email String Email) {

	}
}
