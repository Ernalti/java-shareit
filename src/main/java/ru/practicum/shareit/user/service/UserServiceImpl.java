package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;


@Slf4j
@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	@Autowired
	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDto createUser(UserDto userDto) {
		User user = UserMapper.toUser(userDto);
		log.info("Create user {}", user);
		return UserMapper.toUserDto(userRepository.createUser(user));
	}

	@Override
	public UserDto updateUser(Integer id, UserDto userDto) {
		User user = UserMapper.toUser(userDto);
		log.info("Update user {}", user);
		return UserMapper.toUserDto(userRepository.updateUser(id, user));
	}

	@Override
	public UserDto getUserById(Integer id) {
		log.info("Get user by id {}", id);
		return UserMapper.toUserDto(userRepository.getUserById(id));
	}

	@Override
	public List<UserDto> getAllUsers() {
		log.info("Get all users");
		return UserMapper.toListUserDto(userRepository.getAllUsers());
	}

	@Override
	public void deleteUser(Integer id) {
		log.info("Delete uther with id {} ", id);
		userRepository.deleteUser(id);
	}

	@Override
	public void clearUsers() {
		log.info("Clear all users");
		userRepository.clearUsers();
	}

}
