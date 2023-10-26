package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {
	@Mock
	private ItemRequestRepository itemRequestRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private ItemRepository itemRepository;

	@InjectMocks
	private ItemRequestServiceImpl itemRequestService;

	private User user;
	private ItemRequestDto itemRequestDto;
	private ItemRequest itemRequest;
	private Item item;

	@BeforeEach
	public void beforeEach() {
		user = User.builder()
				.id(1)
				.name("user")
				.email("user@yandex.ru")
				.build();

		itemRequestDto = ItemRequestDto.builder()
				.description("Description")
				.build();

		itemRequest = ItemRequest.builder()
				.id(1)
				.description("Description")
				.requestor(user)
				.created(LocalDateTime.now())
				.build();

		item = Item.builder()
				.id(1)
				.name("item1")
				.description("Item description")
				.available(true)
				.owner(user)
				.request(itemRequest)
				.build();

		lenient().when(userRepository.findById(1)).thenReturn(Optional.of(user));

	}

	@Test
	public void shouldAddItemRequest() {
		when(itemRequestRepository.save(any(ItemRequest.class))).thenAnswer(answer -> {
			ItemRequest savedItemRequest = answer.getArgument(0);
			savedItemRequest.setId(1);
			return savedItemRequest;
		});

		ItemRequestDto newItemRequest = itemRequestService.addItemRequest(1, itemRequestDto);

		assertEquals(1, newItemRequest.getId());
		assertEquals("Description", newItemRequest.getDescription());
		assertEquals(1, newItemRequest.getRequestorId());
	}

	@Test
	public void shouldGetOwnerItemRequests() {
		when(itemRequestRepository.findAllByRequestor(any())).thenReturn(List.of(itemRequest));

		List<ItemRequestDto> ownerItemRequests = itemRequestService.getOwnerItemRequests(1);

		assertEquals(1, ownerItemRequests.size());
		ItemRequestDto itemRequestDto = ownerItemRequests.get(0);
		assertEquals(1, itemRequestDto.getId());
		assertEquals("Description", itemRequestDto.getDescription());
		assertTrue(itemRequestDto.getItems().isEmpty());
	}

	@Test
	public void shouldGetAllItemRequests() {
		when(itemRequestRepository.findAllByRequestorNotLike(any(), any()))
				.thenReturn(List.of(itemRequest));
		when(itemRepository.findbyItemRequests(any())).thenReturn(List.of(item));

		List<ItemRequestDto> allItemRequests = itemRequestService.getAllItemRequests(1, 0, 10);

		assertEquals(1, allItemRequests.size());
		ItemRequestDto itemRequestDto = allItemRequests.get(0);
		assertEquals(1, itemRequestDto.getId());
		assertEquals("Description", itemRequestDto.getDescription());
		assertEquals(1, itemRequestDto.getItems().size());
		ItemDto itemDto = itemRequestDto.getItems().get(0);
		assertEquals(1, itemDto.getId());
		assertEquals("item1", itemDto.getName());
		assertEquals("Item description", itemDto.getDescription());
		assertTrue(itemDto.getAvailable());
	}

	@Test
	public void shouldGetItemRequestById() {
		when(itemRequestRepository.findById(1)).thenReturn(Optional.of(itemRequest));
		when(itemRepository.findByRequest(any())).thenReturn(List.of(item));

		ItemRequestDto itemRequestDto = itemRequestService.getItemRequestById(1, 1);

		assertEquals(1, itemRequestDto.getId());
		assertEquals("Description", itemRequestDto.getDescription());
		assertEquals(1, itemRequestDto.getItems().size());
		ItemDto itemDto = itemRequestDto.getItems().get(0);
		assertEquals(1, itemDto.getId());
		assertEquals("item1", itemDto.getName());
		assertEquals("Item description", itemDto.getDescription());
		assertEquals(true, itemDto.getAvailable());
	}

}