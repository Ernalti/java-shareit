package ru.practicum.shareit.user.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class UserMapper {

	public UserDto toUserDto(User user) {
		return new UserDto(
				user.getId(),
				user.getName(),
				user.getEmail()
		);
	}

	public User toUser(UserDto userDto) {
		return new User(
				userDto.getId(),
				userDto.getName(),
				userDto.getEmail()
		);
	}

	public List<UserDto> toListUserDto(List<User> users) {
		return users.stream()
				.map(UserMapper::toUserDto)
				.collect(Collectors.toList());
	}
}
