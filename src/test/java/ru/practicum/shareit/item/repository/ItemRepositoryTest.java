package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class ItemRepositoryTest {
	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ItemRequestRepository itemRequestRepository;

	User user1;
	User user2;
	Item item1;
	Item item2;
	ItemRequest itemRequest1;
	ItemRequest itemRequest2;

	@BeforeEach
	public void beforeEach() {
		user1 = User.builder()
				.name("user1")
				.email("user1@yandex.ru")
				.build();

		itemRequest1 = ItemRequest.builder()
				.created(LocalDateTime.now().minusDays(5))
				.description("Description1")
				.build();

		item1 = Item.builder()
				.name("item1")
				.description("description1")
				.available(true)
				.build();

		user1 = userRepository.save(user1);
		itemRequest1.setRequestor(user1);
		item1.setOwner(user1);
		itemRequest1 = itemRequestRepository.save(itemRequest1);
		item1.setRequest(itemRequest1);
		item1 = itemRepository.save(item1);
		user2 = User.builder()
				.name("user2")
				.email("user2@yandex.ru")
				.build();

		itemRequest2 = ItemRequest.builder()
				.requestor(user2)
				.created(LocalDateTime.now().minusDays(5))
				.description("new description2")
				.build();

		item2 = Item.builder()
				.name("item2")
				.description("description2")
				.available(true)
				.build();

		user2 = userRepository.save(user2);
		itemRequest2.setRequestor(user2);
		item2.setOwner(user2);
		itemRequest2 = itemRequestRepository.save(itemRequest2);
		item2.setRequest(itemRequest2);
		item2 = itemRepository.save(item2);
	}

	@Test
	public void shouldFindByOwner() {

		List<Item> items = itemRepository.findByOwner(user1);
		assertEquals(1, items.size());
		assertEquals(item1.getId(), items.get(0).getId());
	}

	@Test
	public void shouldFindByText() {
		List<Item> items = itemRepository.findByDescriptionContainingIgnoreCaseAndAvailableIsTrueOrNameContainingIgnoreCaseAndAvailableIsTrue("Desc", "Desc");
		assertEquals(2, items.size());
		items = itemRepository.findByDescriptionContainingIgnoreCaseAndAvailableIsTrueOrNameContainingIgnoreCaseAndAvailableIsTrue("ion2", "ion2");
		assertEquals(1, items.size());
		assertEquals(item2.getId(), items.get(0).getId());
	}

	@Test
	public void shouldFindByRequests() {
		List<Item> items = itemRepository.findbyItemRequests(List.of(itemRequest1));
		assertEquals(1, items.size());
		assertEquals(item1.getId(), items.get(0).getId());
	}

	@Test
	public void shouldFindByRequest() {
		List<Item> items = itemRepository.findByRequest(itemRequest2);
		assertEquals(1, items.size());
		assertEquals(item2.getId(), items.get(0).getId());
	}

}