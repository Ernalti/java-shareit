package ru.practicum.shareit.user.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class UserRepositoryTest {
	@Autowired
	private UserRepository userRepository;

	@Test
	public void shouldFindByEmailIgnoreCase() {
		User user = User.builder()
				.name("user")
				.email("user@yandex.ru")
				.build();
		userRepository.save(user);
		List<User> findUsers = userRepository.findByEmailIgnoreCase(user.getEmail());
		assertEquals(1, findUsers.size());
		assertEquals(user.getName(), findUsers.get(0).getName());
	}

}