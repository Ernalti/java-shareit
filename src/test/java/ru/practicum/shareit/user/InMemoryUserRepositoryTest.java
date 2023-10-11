package ru.practicum.shareit.user;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exception.exceptions.EntityAlreadyExistsException;
import ru.practicum.shareit.exception.exceptions.NotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class InMemoryUserRepositoryTest {

	private final UserRepository userRepository;
	User user1;
	User user2;
//
//	@BeforeEach
//	public void beforeEach() {
//		userRepository.clearUsers();
//		user1 = new User(null, "user1", "user1@user1.ru");
//		user2 = new User(null, "user2", "user2@user2.ru");
//	}
//
//
//	@Test
//	public void shouldCreateAndGetUser() {
//		assertEquals(userRepository.getAllUsers().size(), 0);
//		User user = userRepository.createUser(user1);
//		user1.setId(user.getId());
//		assertEquals(userRepository.getAllUsers().size(), 1);
//		assertNotNull(user.getId());
//		assertEquals(userRepository.getUserById(user.getId()), user1);
//	}
//
//	@Test
//	public void shouldUpdateUser() {
//		user1 = userRepository.createUser(user1);
//		String newName = "newName";
//		user1.setName(newName);
//		User updatedUser = userRepository.updateUser(user1.getId(), user1);
//		assertEquals(updatedUser.getName(), newName);
//	}
//
//	@Test
//	public void shouldDeleteUser() {
//		user1 = userRepository.createUser(user1);
//		userRepository.deleteUser(user1.getId());
//		assertEquals(userRepository.getAllUsers().size(), 0);
//	}
//
//	@Test
//	public void shouldGetAllUsers() {
//		userRepository.createUser(user1);
//		userRepository.createUser(user2);
//		List<User> allUsers = userRepository.getAllUsers();
//		assertEquals(allUsers.size(), 2);
//		assertTrue(allUsers.contains(user1));
//		assertTrue(allUsers.contains(user2));
//	}
//
//	@Test
//	public void shouldNotCreateUserWithEqualName() {
//		userRepository.createUser(user1);
//		User user3 = new User(null, user1.getName(), "user3@mail.ru");
//
//		assertThrows(EntityAlreadyExistsException.class, () ->
//				userRepository.createUser(user3));
//	}
//
//	@Test
//	public void shouldNotCreateUserWithEqualEmail() {
//		userRepository.createUser(user1);
//		User user3 = new User(null, "user3", user1.getEmail());
//
//		assertThrows(EntityAlreadyExistsException.class, () ->
//				userRepository.createUser(user3));
//	}
//
//	@Test
//	public void shouldNotGetUserWithWrongId() {
//		assertThrows(NotFoundException.class, () ->
//				userRepository.getUserById(9876));
//	}
//
//	@Test
//	public void shouldNotDeleteUserWithWrongId() {
//		assertThrows(NotFoundException.class, () ->
//				userRepository.deleteUser(9876));
//	}

}
