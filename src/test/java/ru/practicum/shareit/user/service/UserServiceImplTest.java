package ru.practicum.shareit.user.service;

import com.sun.jdi.request.DuplicateRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserServiceImpl userService;

	User user1;
	User user12;
	User user2;
	UserDto userDto1;
	UserDto userDto12;
	UserDto userDto2;

	@BeforeEach
	public void beforeEach() {
		user1 = User.builder()
				.id(1)
				.name("user")
				.email("user@yandex.ru")
				.build();
		userDto1 = UserDto.builder()
				.name("user")
				.email("user@yandex.ru")
				.build();
		user12 = User.builder()
				.id(1)
				.name("user12")
				.email("user12@yandex.ru")
				.build();
		userDto12 = UserDto.builder()
				.name("user12")
				.email("user12@yandex.ru")
				.build();
		user2 = User.builder()
				.id(2)
				.name("user2")
				.email("user2@yandex.ru")
				.build();
		userDto2 = UserDto.builder()
				.name("user2")
				.email("user2@yandex.ru")
				.build();
		lenient().when(userRepository.findById(1)).thenReturn(Optional.of(user1));
		lenient().when(userRepository.save(any(User.class))).thenAnswer(answer -> {
			User res = answer.getArgument(0);
			res.setId(1);
			return res;
		});
	}

	@Test
	public void shouldCreateUser() {
		UserDto newUser = userService.createUser(userDto1);
		assertEquals(1, newUser.getId());
		assertEquals(userDto1.getName(), newUser.getName());
		assertEquals(userDto1.getEmail(), newUser.getEmail());
		verify(userRepository, times(1)).save(any(User.class));
	}

	@Test
	public void shouldUpdateUser() {
		when(userRepository.findById(1)).thenReturn(Optional.of(user1));
		when(userRepository.save(any(User.class))).thenAnswer(answer -> answer.getArgument(0));
		UserDto newUser = userService.updateUser(1, userDto12);
		assertEquals(1, newUser.getId());
		assertEquals(userDto12.getName(), newUser.getName());
		assertEquals(userDto12.getEmail(), newUser.getEmail());
		verify(userRepository, times(1)).save(any(User.class));
	}

	@Test
	public void shouldUpdateUserWithEmptyName() {
		userDto12.setName(null);
		user12.setName(user1.getName());

		UserDto newUser = userService.updateUser(1, userDto12);
		assertEquals(1, newUser.getId());
		assertEquals(user12.getName(), newUser.getName());
		assertEquals(user12.getEmail(), newUser.getEmail());
		verify(userRepository, times(1)).save(any(User.class));
	}

	@Test
	public void shouldUpdateUserWithEmptyEmail() {
		userDto12.setEmail(null);
		user12.setEmail(user1.getEmail());
		when(userRepository.findById(1)).thenReturn(Optional.of(user1));
		when(userRepository.save(any(User.class))).thenAnswer(answer -> answer.getArgument(0));
		UserDto newUser = userService.updateUser(1, userDto12);
		assertEquals(1, newUser.getId());
		assertEquals(user12.getName(), newUser.getName());
		assertEquals(user12.getEmail(), newUser.getEmail());
		verify(userRepository, times(1)).save(any(User.class));
	}

	@Test
	public void shouldUpdateUserWithNonUniqueEmail() {
		user12.setId(7);
		List<User> listUser2 = new ArrayList<>();
		listUser2.add(user12);
		when(userRepository.findByEmailIgnoreCase(anyString())).thenReturn(listUser2);
		assertThrows(DuplicateRequestException.class, () -> userService.updateUser(2, userDto1));
	}

	@Test
	public void shouldUpdateUserWithWrongId() {
		user12.setId(7);
		List<User> listUser2 = new ArrayList<>();
		listUser2.add(user12);
		assertThrows(NoSuchElementException.class, () -> userService.updateUser(2, userDto1));
	}

	@Test
	public void ShouldGetUserById() {
		UserDto newUser = userService.getUserById(1);
		assertEquals(1, newUser.getId());
		assertEquals(user1.getName(), newUser.getName());
		assertEquals(user1.getEmail(), newUser.getEmail());
	}

	@Test
	public void shouldGetUserByIdWithWrongId() {
		assertThrows(NoSuchElementException.class, () -> userService.getUserById(2));
	}

	@Test
	public void ShouldGetAllUsers() {
		List<User> listUsers = new ArrayList<>();
		listUsers.add(user1);
		listUsers.add(user2);
		when(userRepository.findAll()).thenReturn(listUsers);
		List<UserDto> newUsers = userService.getAllUsers();
		assertEquals(1, newUsers.get(0).getId());
		assertEquals(user1.getName(), newUsers.get(0).getName());
		assertEquals(user1.getEmail(), newUsers.get(0).getEmail());
		assertEquals(2, newUsers.get(1).getId());
		assertEquals(user2.getName(), newUsers.get(1).getName());
		assertEquals(user2.getEmail(), newUsers.get(1).getEmail());
	}
}