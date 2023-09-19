package ru.practicum.shareit.user;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exception.exceptions.EntityAlreadyExistsException;
import ru.practicum.shareit.exception.exceptions.NotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class InMemoryUserStorageTest {

	private final UserStorage userStorage;
	User user1;
	User user2;

	@BeforeEach
	void clearUsersBeforeTest() {
		userStorage.clearUsers();
		user1 = new User(null, "user1", "user1@user1.ru");
		user2 = new User(null, "user2", "user2@user2.ru");
	}


	@Test
	void ShouldCreateAndGetUser() {
		assertEquals(userStorage.getAllUsers().size(), 0);
		User user = userStorage.createUser(user1);
		user1.setId(user.getId());
		assertEquals(userStorage.getAllUsers().size(), 1);
		assertNotNull(user.getId());
		assertEquals(userStorage.getUserById(user.getId()), user1);
	}

	@Test
	void shouldUpdateUser() {
		user1 = userStorage.createUser(user1);
		String newName = "newName";
		user1.setName(newName);
		User updatedUser = userStorage.updateUser(user1.getId(), user1);
		assertEquals(updatedUser.getName(), newName);
	}

	@Test
	void shouldDeleteUser() {
		user1 = userStorage.createUser(user1);
		userStorage.deleteUser(user1.getId());
		assertEquals(userStorage.getAllUsers().size(), 0);
	}

	@Test
	void shouldGetAllUsers() {
		userStorage.createUser(user1);
		userStorage.createUser(user2);
		List<User> allUsers = userStorage.getAllUsers();
		assertEquals(allUsers.size(), 2);
		assertTrue(allUsers.contains(user1));
		assertTrue(allUsers.contains(user2));
	}

	@Test
	void ShouldNotCreateUserWithEqualName() {
		userStorage.createUser(user1);
		User user3 = new User(null, user1.getName(), "user3@mail.ru");

		assertThrows(EntityAlreadyExistsException.class, () ->
				userStorage.createUser(user3));
	}

	@Test
	void ShouldNotCreateUserWithEqualEmail() {
		userStorage.createUser(user1);
		User user3 = new User(null, "user3", user1.getEmail());

		assertThrows(EntityAlreadyExistsException.class, () ->
				userStorage.createUser(user3));
	}


	@Test
	void ShouldNotUpdateUserWithWrongEmail() {
		User user = userStorage.createUser(user1);
		User user3 = new User(user.getId(), "user3", "user3");
		assertThrows(IllegalArgumentException.class, () ->
				userStorage.updateUser(user3.getId(), user3));
	}

	@Test
	void ShouldNotGetUserWithWrongId() {
		assertThrows(NotFoundException.class, () ->
				userStorage.getUserById(9876));
	}

	@Test
	void ShouldNotDeleteUserWithWrongId() {
		assertThrows(NotFoundException.class, () ->
				userStorage.deleteUser(9876));
	}

}
