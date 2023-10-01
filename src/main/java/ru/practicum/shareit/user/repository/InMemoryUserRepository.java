package ru.practicum.shareit.user.repository;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.exception.exceptions.EntityAlreadyExistsException;
import ru.practicum.shareit.exception.exceptions.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Slf4j
@Repository
@Validated
public class InMemoryUserRepository implements UserRepository {

	private final Map<Integer, User> users = new HashMap<>();
	private final Set<String> userEmails = new HashSet<>();
	private final Set<String> userNames = new HashSet<>();
	private Integer id;

	public InMemoryUserRepository() {
		this.id = 1;
	}

	@Override
	public User createUser(User user) {

		isUnique(user);

		user.setId(nextId());
		users.put(user.getId(), user);
		userNames.add(user.getName());
		userEmails.add(user.getEmail());
		log.info("User created in memory {}", user);
		return user;
	}

	@Override
	public User updateUser(Integer id, User user) {
		user.setId(id);
		User updateUser = getUserById(id);

		String name = user.getName();
		if (name != null && !name.isBlank() && !updateUser.getName().equals(user.getName())) {
			if (userNames.contains(user.getName())) {
				throw new EntityAlreadyExistsException("User with name " + user.getName() + " already exist");
			}
			userNames.remove(updateUser.getName());
			updateUser.setName(name);
			userNames.add(user.getName());
		}

		String email = user.getEmail();
		if (email != null && !email.isBlank() && !updateUser.getEmail().equals(user.getEmail())) {
			if (userEmails.contains(user.getEmail())) {
				throw new EntityAlreadyExistsException("User with email " + user.getName() + " already exist");
			}
			userEmails.remove(updateUser.getEmail());
			updateUser.setEmail(email);
			userEmails.add(user.getEmail());
		}
		log.info("User updated in memory {}", user);

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
		User user = getUserById(id);
		userNames.remove(user.getName());
		userEmails.remove(user.getEmail());
		log.info("User with id {} deleted", id);
		users.remove(id);
	}

	@Override
	public void clearUsers() {
		users.clear();
		userNames.clear();
		userEmails.clear();
		id = 1;
		log.info("Clear users");
	}

	private Integer nextId() {
		return id++;
	}

	private void isUnique(User user) {
		boolean nameNotUnique = userNames.contains(user.getName());
		boolean emailNotUnique = userEmails.contains(user.getEmail());

		if (nameNotUnique) {
			throw new EntityAlreadyExistsException("User with name " + user.getName() + " already exist");
		}
		if (emailNotUnique) {
			throw new EntityAlreadyExistsException("User with email " + user.getName() + " already exist");
		}
	}

}
