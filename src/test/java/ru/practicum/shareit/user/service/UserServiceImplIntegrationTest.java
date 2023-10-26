package ru.practicum.shareit.user.service;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@SpringBootTest(properties = {"db.name=test"})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceImplIntegrationTest {
	private final UserService userservice;

	private UserDto user1;
	private UserDto user2;

	@BeforeEach
	public void beforeEach() {
		user1 = UserDto.builder()
				.name("user1")
				.email("user1@yandex.ru")
				.build();
		user2 = UserDto.builder()
				.name("user2")
				.email("user2@yandex.ru")
				.build();
	}

	@Test
	public void shouldAddUser() {
		UserDto createdUser = userservice.createUser(user1);
		assertTrue(createdUser.getId() > 0);
		assertEquals(user1.getName(), createdUser.getName());
		assertEquals(user1.getEmail(), createdUser.getEmail());
	}

	@Test
	public void shouldGetAllUsers() {
		List<UserDto> createdUsers = new ArrayList<>();
		createdUsers.add(userservice.createUser(user1));
		createdUsers.add(userservice.createUser(user2));
		List<UserDto> getUsers = userservice.getAllUsers();
		assertEquals(2, getUsers.size());
		for (int i = 0; i < getUsers.size(); i++) {
			assertEquals(createdUsers.get(i), getUsers.get(i));
			assertEquals(createdUsers.get(i).getName(), getUsers.get(i).getName());
			assertEquals(createdUsers.get(i).getEmail(), getUsers.get(i).getEmail());
		}
	}


}
