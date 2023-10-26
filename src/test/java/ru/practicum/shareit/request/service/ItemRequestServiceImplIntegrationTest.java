package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest(properties = {"db.name=test"})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestServiceImplIntegrationTest {
	private final ItemRequestService itemRequestService;
	private final UserRepository userRepository;
	private final ItemRepository itemRepository;
	private final ItemRequestRepository itemRequestRepository;

	private User user;
	private User user2;

	private ItemRequest request;
	private Item item;

	@BeforeEach
	void create() {
		user = User.builder()
				.name("user")
				.email("user@yandex.ru")
				.build();
		user = userRepository.save(user);
		request = ItemRequest.builder()
				.description("description1")
				.requestor(user)
				.created(LocalDateTime.now())
				.build();
		request = itemRequestRepository.save(request);
		user2 = User.builder()
				.name("user2")
				.email("user2@yandex.ru")
				.build();
		user2 = userRepository.save(user2);
		item = Item.builder()
				.name("item1")
				.description("Item1 description")
				.available(true)
				.owner(user2)
				.request(request)
				.build();
		item = itemRepository.save(item);

	}

	@Test
	public void shouldGetAllItemRequests() {
		List<ItemRequestDto> requests = itemRequestService.getAllItemRequests(user2.getId(), 0, 10);
		assertEquals(1, requests.size());
		assertEquals(request.getId(), requests.get(0).getId());
		assertEquals(request.getDescription(), requests.get(0).getDescription());
		assertEquals(request.getRequestor().getId(), requests.get(0).getRequestorId());
		ItemDto getItem = requests.get(0).getItems().get(0);
		assertEquals(item.getId(), getItem.getId());
		assertEquals(item.getName(), getItem.getName());

	}

}
