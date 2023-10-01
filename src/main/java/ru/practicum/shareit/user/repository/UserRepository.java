package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {

	User createUser(User user);

	User updateUser(Integer id, User user);

	User getUserById(Integer id);

	List<User> getAllUsers();

	void deleteUser(Integer id);

	void clearUsers();
}
