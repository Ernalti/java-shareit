package ru.practicum.shareit.request.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class ItemRequestRepositoryTest {

	@Autowired
	private ItemRequestRepository itemRequestRepository;

	@Autowired
	private UserRepository userRepository;

	User user1;
	User user2;
	User user3;

	@BeforeEach
	public void beforeEach() {
		user1 = User.builder()
				.name("user1")
				.email("user1@yandex.ru")
				.build();
		user2 = User.builder()
				.name("user2")
				.email("user2@yandex.ru")
				.build();
		user3 = User.builder()
				.name("user3")
				.email("user3@yandex.ru")
				.build();

		user1 = userRepository.save(user1);
		user2 = userRepository.save(user2);
		user3 = userRepository.save(user3);

		ItemRequest request1 = ItemRequest.builder()
				.requestor(user1)
				.description("Request 1")
				.build();

		ItemRequest request2 = ItemRequest.builder()
				.requestor(user2)
				.description("Request 2")
				.build();

		ItemRequest request3 = ItemRequest.builder()
				.requestor(user3)
				.description("Request 3")
				.build();

		itemRequestRepository.save(request1);
		itemRequestRepository.save(request2);
		itemRequestRepository.save(request3);
	}

	@Test
	public void shouldFindAllByRequestorNotLike() {
		List<ItemRequest> requests = itemRequestRepository.findAllByRequestorNotLike(user1, PageRequest.of(0, 10)).toList();
		assertEquals(2, requests.size());
		assertEquals(user2.getId(),requests.get(0).getRequestor().getId());
		assertEquals(user3.getId(),requests.get(1).getRequestor().getId());
	}

	@Test
	public void shouldFindAllByRequestor() {
		List<ItemRequest> requests = itemRequestRepository.findAllByRequestor(user1);
		assertEquals(1, requests.size());
		assertEquals(user1.getId(),requests.get(0).getRequestor().getId());
	}
}