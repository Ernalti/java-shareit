package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
	User createUser(User userDto);

	User updateUser(Integer id, User userDto);

	User getUserById(Integer id);

	List<User> getAllUsers();

	void deleteUser(Integer id);

}
