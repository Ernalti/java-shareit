package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
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
public class ItemServiceImplIntegrationTest {
	private final ItemService itemService;
	private final ItemRepository itemRepository;
	private final UserRepository userRepository;
	private final BookingRepository bookingRepository;
	private final ItemRequestRepository itemRequestRepository;


	private User user;

	private Item item;

	ItemRequest itemRequest;

	@BeforeEach
	public void beforeEach() {
		user = User.builder()
				.name("user")
				.email("user@yandex.ru")
				.build();

		itemRequest = ItemRequest.builder()
				.id(5)
				.requestor(user)
				.created(LocalDateTime.now().minusDays(5))
				.description("Description")
				.build();

		item = Item.builder()
				.name("item")
				.description("Description")
				.available(true)
				.build();

	}

	@Test
	public void shouldGetOwnerItems() {
		User savedUser = userRepository.save(user);
		item.setOwner(savedUser);
		ItemRequest savedRequest = itemRequestRepository.save(itemRequest);
		item.setRequest(savedRequest);
		itemRepository.save(item);

		Booking lastBooking = new Booking(1, LocalDateTime.now().minusDays(1), LocalDateTime.now().minusMinutes(10), item, user, BookingStatus.APPROVED);
		Booking nextBooking = new Booking(2, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(4), item, user, BookingStatus.APPROVED);
		bookingRepository.save(lastBooking);
		bookingRepository.save(nextBooking);
		List<ItemDto> items = itemService.getOwnerItems(1);
		ItemDto getItem = items.get(0);
		assertEquals(item.getId(), getItem.getId());

	}

}
