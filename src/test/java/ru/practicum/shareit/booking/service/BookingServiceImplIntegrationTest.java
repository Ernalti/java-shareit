package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest(properties = {"db.name=test"})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingServiceImplIntegrationTest {
	private final BookingService bookingService;
	private final UserRepository userRepository;
	private final ItemRepository itemRepository;
	private final BookingRepository bookingRepository;
	private User user;
	private User user2;
	private Item item;
	private Booking booking;
	private Booking booking2;

	@BeforeEach
	void setUp() {
		user = User.builder()
				.name("user1")
				.email("user1@yandex.ru")
				.build();
		user = userRepository.save(user);
		user2 = User.builder()
				.name("user2")
				.email("user2@yandex.ru")
				.build();
		user2 = userRepository.save(user2);
		item = Item.builder()
				.name("item1")
				.description("Item1 description")
				.available(true)
				.owner(user)
				.build();
		item = itemRepository.save(item);
		booking = new Booking(0, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2), item, user2, BookingStatus.APPROVED);
		booking = bookingRepository.save(booking);
		booking2 = new Booking(0, LocalDateTime.now().minusDays(3), LocalDateTime.now().minusDays(2), item, user2, BookingStatus.APPROVED);
		booking2 = bookingRepository.save(booking2);
	}

	@Test
	public void shouldGetOwnerBookings() {
		List<BookingDto> bookingDtoList = bookingService.getOwnerBookings(user.getId(), "ALL", 0, 10);

		assertEquals(2, bookingDtoList.size());
		assertEquals(booking.getId(), bookingDtoList.get(0).getId());
		assertEquals(booking2.getId(), bookingDtoList.get(1).getId());
		assertEquals(booking.getStart(), bookingDtoList.get(0).getStart());
		assertEquals(booking.getEnd(), bookingDtoList.get(0).getEnd());
		assertEquals(booking2.getStart(), bookingDtoList.get(1).getStart());
		assertEquals(booking2.getEnd(), bookingDtoList.get(1).getEnd());
	}


}