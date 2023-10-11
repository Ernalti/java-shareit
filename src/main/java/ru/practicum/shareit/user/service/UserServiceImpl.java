package ru.practicum.shareit.user.service;

import com.sun.jdi.request.DuplicateRequestException;
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
		return UserMapper.toUserDto(userRepository.save(user));
	}


	@Override
	public UserDto updateUser(Integer id, UserDto userDto) {
		uniqueEmail(id,userDto.getEmail());
		User user = userRepository.findById(id).orElseThrow();
		if (userDto.getName()!=null) {
			user.setName(userDto.getName());
		}
		if (userDto.getEmail()!=null) {
			user.setEmail(userDto.getEmail());
		}
		log.info("Update user {}", user);
		return UserMapper.toUserDto(userRepository.save(user));
	}

	@Override
	public UserDto getUserById(Integer id) {
		log.info("Get user by id {}", id);
		return UserMapper.toUserDto(userRepository.findById(id).orElseThrow());
	}

	@Override
	public List<UserDto> getAllUsers() {
		log.info("Get all users");
		return UserMapper.toListUserDto(userRepository.findAll());
	}

	@Override
	public void deleteUser(Integer id) {
		log.info("Delete uther with id {} ", id);
		userRepository.deleteById(id);
	}

	@Override
	public void clearUsers() {
		log.info("Clear all users");
		userRepository.deleteAll();
	}

	private void uniqueEmail(Integer id, String email) {
		List<User> users = userRepository.findByEmailIgnoreCase(email);
		if (users!=null && (users.size()>1 || (users.size()==1 && !users.get(0).getId().equals(id)))) {
			throw new DuplicateRequestException("Email already exists: " + email);
		}
	}

}
