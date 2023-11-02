package ru.practicum.shareit.user.service;

import com.sun.jdi.request.DuplicateRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Slf4j
@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	@Autowired
	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}


	@Override
	@Transactional
	public UserDto createUser(UserDto userDto) {
		User user = UserMapper.toUser(userDto);

		log.info("Create user {}", user);
		return UserMapper.toUserDto(userRepository.save(user));
	}


	@Override
	@Transactional
	public UserDto updateUser(int id, UserDto userDto) {
		uniqueEmail(id,userDto.getEmail());
		User user = userRepository.findById(id).orElseThrow();
		if (userDto.getName() != null) {
			user.setName(userDto.getName());
		}
		if (userDto.getEmail() != null) {
			user.setEmail(userDto.getEmail());
		}
		log.info("Update user {}", user);
		return UserMapper.toUserDto(userRepository.save(user));
	}

	@Override
	public UserDto getUserById(int id) {
		log.info("Get user by id {}", id);
		return UserMapper.toUserDto(userRepository.findById(id).orElseThrow());
	}

	@Override
	public List<UserDto> getAllUsers() {
		log.info("Get all users");
		return UserMapper.toListUserDto(userRepository.findAll());
	}

	@Override
	@Transactional
	public void deleteUser(int id) {
		log.info("Delete user with id {} ", id);
		userRepository.deleteById(id);
	}

	private void uniqueEmail(int id, String email) {
		List<User> users = userRepository.findByEmailIgnoreCase(email);
		if (users != null && (users.size() > 1 || (users.size() == 1 && users.get(0).getId() != id))) {
			throw new DuplicateRequestException("Email already exists: " + email);
		}
	}

}
