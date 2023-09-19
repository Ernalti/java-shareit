package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;


@Slf4j
@Service
public class UserServiceImpl implements UserService {

	private final UserStorage userStorage;

	@Autowired
	public UserServiceImpl(UserStorage userStorage) {
		this.userStorage = userStorage;
	}

	@Override
	public User createUser(User user) {
		log.info("Create user {}", user);
		return userStorage.createUser(user);
	}

	@Override
	public User updateUser(Integer id,User user) {
		log.info("Update user {}", user);
		return userStorage.updateUser(id, user);
	}

	@Override
	public User getUserById(Integer id) {
		log.info("Get user by id {}", id);
		return userStorage.getUserById(id);
	}

	@Override
	public List<User> getAllUsers() {
		log.info("Get all users");
		return userStorage.getAllUsers();
	}

	@Override
	public void deleteUser(Integer id) {
		log.info("Delete uther with id {} ", id);
		userStorage.deleteUser(id);
	}

	@Override
	public void clearUsers() {
		log.info("Clear all users");
		userStorage.clearUsers();
	}

}
