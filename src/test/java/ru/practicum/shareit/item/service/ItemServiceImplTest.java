package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.exceptions.CommentException;
import ru.practicum.shareit.exception.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {
	@Mock
	private ItemRepository itemRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private ItemRequestRepository itemRequestRepository;

	@Mock
	private BookingRepository bookingRepository;

	@Mock
	private CommentRepository commentRepository;

	@InjectMocks
	private ItemServiceImpl itemService;

	private User user;
	private ItemDto itemDto;
	private Item item;
	private Booking lastBooking;
	private Booking nextBooking;
	ItemRequest itemRequest;

	@BeforeEach
	public void beforeEach() {
		user = User.builder()
				.id(1)
				.name("user")
				.email("user@yandex.ru")
				.build();

		itemDto = ItemDto.builder()
				.name("item1")
				.description("Description")
				.available(true)
				.build();

		itemRequest = ItemRequest.builder()
				.id(5)
				.build();

		item = Item.builder()
				.id(2)
				.name("item2")
				.description("Description2")
				.available(true)
				.owner(user)
				.request(itemRequest)
				.build();

		lastBooking = new Booking(1, LocalDateTime.MIN, LocalDateTime.MAX, item, user, BookingStatus.APPROVED);
		nextBooking = new Booking(2, LocalDateTime.MIN, LocalDateTime.MAX, item, user, BookingStatus.APPROVED);


		lenient().when(itemRepository.findById(2)).thenReturn(Optional.of(item));
		lenient().when(itemRequestRepository.findById(anyInt())).thenReturn(Optional.of(itemRequest));
		lenient().when(userRepository.findById(1)).thenReturn(Optional.of(user));
		lenient().when(bookingRepository.findFirstByItemAndStartBeforeAndStatus(any(), any(), any(), any()))
				.thenReturn(Optional.of(lastBooking));
		lenient().when(bookingRepository.findFirstByItemAndStartAfterAndStatus(any(), any(), any(), any()))
				.thenReturn(Optional.of(nextBooking));
		lenient().when(itemRepository.save(any(Item.class))).thenAnswer(answer -> {
			Item savedItem = answer.getArgument(0);
			if (savedItem.getId() == 0) {
				savedItem.setId(1);
			}
			return savedItem;
		});
	}

	@Test
	public void shouldAddItem() {
		itemDto.setRequestId(5);

		ItemDto newItem = itemService.addItem(1, itemDto);
		assertEquals(1, newItem.getId());
		assertEquals("item1", newItem.getName());
		assertEquals("Description", newItem.getDescription());
		assertEquals(true, newItem.getAvailable());
		assertEquals(5, newItem.getRequestId());

	}

	@Test
	public void shouldUpdateItemName() {
		itemDto.setRequestId(5);

		ItemDto updatedItem = ItemDto.builder()
				.name("new Item")
				.build();

		ItemDto newItem = itemService.updateItem(2, 1, updatedItem);
		assertEquals(2, newItem.getId());
		assertEquals("new Item", newItem.getName());
		assertEquals("Description2", newItem.getDescription());
		assertEquals(true, newItem.getAvailable());
		assertEquals(5, newItem.getRequestId());
		assertEquals(1, newItem.getLastBooking().getId());
		assertEquals(2, newItem.getNextBooking().getId());
	}

	@Test
	public void shouldUpdateItemDescription() {

		ItemDto updatedItem = ItemDto.builder()
				.description("new description")
				.build();

		ItemDto newItem = itemService.updateItem(2, 1, updatedItem);
		assertEquals(2, newItem.getId());
		assertEquals("item2", newItem.getName());
		assertEquals("new description", newItem.getDescription());
		assertEquals(true, newItem.getAvailable());
		assertEquals(5, newItem.getRequestId());
		assertEquals(1, newItem.getLastBooking().getId());
		assertEquals(2, newItem.getNextBooking().getId());
	}

	@Test
	public void shouldUpdateItemAvailable() {
		itemDto.setRequestId(5);

		ItemDto updatedItem = ItemDto.builder()
				.available(false)
				.build();

		ItemDto newItem = itemService.updateItem(2, 1, updatedItem);
		assertEquals(2, newItem.getId());
		assertEquals("item2", newItem.getName());
		assertEquals("Description2", newItem.getDescription());
		assertEquals(false, newItem.getAvailable());
		assertEquals(5, newItem.getRequestId());
		assertEquals(1, newItem.getLastBooking().getId());
		assertEquals(2, newItem.getNextBooking().getId());
	}


	@Test
	public void shouldGetItemById() {
		ItemDto retrievedItem = itemService.getItemById(2, 1);

		assertEquals("item2", retrievedItem.getName());
		assertEquals("Description2", retrievedItem.getDescription());
		assertEquals(true, retrievedItem.getAvailable());
	}

	@Test
	public void shouldGetOwnerItems() {
		when(itemRepository.findByOwner(any())).thenReturn(List.of(item));
		when(bookingRepository.findlastBookings(any(), any(), any(), any())).thenReturn(List.of(lastBooking));
		when(bookingRepository.findNextBookings(any(), any(), any(), any())).thenReturn(List.of(nextBooking));
		List<ItemDto> items = itemService.getOwnerItems(1);
		ItemDto item = items.get(0);
		assertEquals(1, items.size());
		assertEquals("item2", item.getName());
		assertEquals("Description2", item.getDescription());
		assertEquals(true, item.getAvailable());
		assertEquals(5, item.getRequestId());
		assertEquals(1, item.getLastBooking().getId());
		assertEquals(2, item.getNextBooking().getId());
	}

	@Test
	public void shouldSearchItemsByText() {
		List<Item> items = List.of(item);

		when(itemRepository.findByDescriptionContainingIgnoreCaseAndAvailableIsTrueOrNameContainingIgnoreCaseAndAvailableIsTrue("Description", "Description"))
				.thenReturn(items);

		List<ItemDto> searchResults = itemService.searchItemsByText("Description");

		assertEquals(1, searchResults.size());
		assertEquals("item2", searchResults.get(0).getName());
	}

	@Test
	public void shouldSearchItemsByBlankText() {
		List<Item> items = List.of(item);

		List<ItemDto> searchResults = itemService.searchItemsByText("");

		assertEquals(0, searchResults.size());
	}

	@Test
	public void shouldAddComment() {
		item.setId(1);
		CommentDto comment = CommentDto.builder()
				.text("comment")
				.build();

		when(bookingRepository.findByItemAndBookerAndStatusAndEndBefore(any(), any(), any(), any()))
				.thenReturn(List.of(lastBooking));

		when(commentRepository.save(any())).thenAnswer(answer -> {
			Comment answerComment = answer.getArgument(0);
			answerComment.setId(1);
			return answerComment;
		});

		CommentDto addedComment = itemService.addComment(2, 1, comment);

		assertEquals(1, addedComment.getId());
		assertEquals("comment", addedComment.getText());
		assertEquals(user.getName(), addedComment.getAuthorName());
	}

	@Test
	void shouldThrowNotFoundExceptionWhenAddingItemWithNonexistentUser() {

		assertThrows(NotFoundException.class, () -> {
			itemService.addItem(999, itemDto); // User with ID 999 does not exist
		});
	}

	@Test
	void shouldThrowNotFoundExceptionWhenGettingItemsOfNonexistentUser() {
		assertThrows(NoSuchElementException.class, () -> {
			itemService.getOwnerItems(999); // User with ID 999 does not exist
		});
	}

	@Test
	void shouldThrowNotFoundExceptionWhenAddingCommentToNonexistentItem() {

		CommentDto comment = CommentDto.builder()
				.text("comment")
				.build();

		assertThrows(NoSuchElementException.class, () -> {
			itemService.addComment(999, 1, comment); // Item with ID 999 does not exist
		});
	}

	@Test
	void shouldThrowAuthorizationErrorExceptionWhenAddingCommentWithoutBooking() {

		CommentDto comment = CommentDto.builder()
				.text("comment")
				.build();

		assertThrows(CommentException.class, () -> {
			itemService.addComment(2, 1, comment); // User with ID 1 didn't book item 2
		});
	}

	@Test
	void shouldThrowCommentExceptionWhenAddingCommentWithoutBooking() {

		CommentDto comment = CommentDto.builder()
				.text("comment")
				.build();

		// Simulate the case where a user did not book the item but tries to add a comment
		when(bookingRepository.findByItemAndBookerAndStatusAndEndBefore(any(), any(), any(), any()))
				.thenReturn(List.of());

		assertThrows(CommentException.class, () -> {
			itemService.addComment(2, 1, comment); // User with ID 1 didn't book item 2
		});
	}
}