package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
	UserDto createUser(UserDto userDto);

	UserDto updateUser(Integer id, UserDto userDto);

	UserDto getUserById(Integer id);

	List<UserDto> getAllUsers();

	void deleteUser(Integer id);

	void clearUsers();
}
