package ru.practicum.shareit.booking.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class BookingRepositoryTest {

	@Autowired
	private BookingRepository bookingRepository;

	@Autowired
	private ItemRepository itemRepository;

	@Autowired
	private UserRepository userRepository;

	User user1;
	User user2;
	Item item1;
	Item item2;

	@BeforeEach
	public void beforeEach() {
		user1 = User.builder()
				.name("user1")
				.email("user1@yandex.ru")
				.build();

		item1 = Item.builder()
				.name("item1")
				.description("description1")
				.available(true)
				.build();
		user1 = userRepository.save(user1);
		item1.setOwner(user1);
		item1 = itemRepository.save(item1);
//		booking1 = bookingRepository.save(booking1);
		user2 = User.builder()
				.name("user2")
				.email("user2@yandex.ru")
				.build();

		item2 = Item.builder()
				.name("item2")
				.description("description2")
				.available(true)
				.build();

		user2 = userRepository.save(user2);
		item2.setOwner(user2);
		item2 = itemRepository.save(item2);
//		booking2 = bookingRepository.save(booking2);

	}

	@Test
	public void shouldFindByItemAndBookerAndStatusAndEndBefore() {
		Booking booking = new Booking(0, LocalDateTime.now().minusHours(10), LocalDateTime.now().minusHours(2), item1, user1, BookingStatus.APPROVED);
		bookingRepository.save(booking);
		List<Booking> bookings = bookingRepository.findByItemAndBookerAndStatusAndEndBefore(item1, user1, BookingStatus.APPROVED, LocalDateTime.now());

		assertEquals(1, bookings.size());
		Booking newBooking = bookings.get(0);
		assertTrue(booking.getId() > 0);
		assertEquals(item1.getId(), newBooking.getItem().getId());
		assertEquals(user1.getId(), newBooking.getBooker().getId());
	}

	// Пример теста для метода findByItemOwnerAndStartBeforeAndEndAfter
	@Test
	public void shouldFindByItemOwnerAndStartBeforeAndEndAfter() {
		Booking booking = new Booking(0, LocalDateTime.now().minusMinutes(30), LocalDateTime.now().plusMinutes(30), item1, user1, BookingStatus.APPROVED);
		bookingRepository.save(booking);
		List<Booking> bookings = bookingRepository.findByItemOwnerAndStartBeforeAndEndAfter(user1, LocalDateTime.now(), LocalDateTime.now(), PageRequest.of(0, 10), Sort.by(Sort.Order.asc("id")));
		assertEquals(1, bookings.size());
		Booking newBooking = bookings.get(0);
		assertTrue(booking.getId() > 0);
		assertEquals(item1.getId(), newBooking.getItem().getId());
		assertEquals(user1.getId(), newBooking.getBooker().getId());
	}

	@Test
	public void shouldFindFirstByItemAndStartBeforeAndStatus() {
		LocalDateTime startTime = LocalDateTime.now().minusHours(1);

		Booking booking1 = new Booking(0, startTime.minusMinutes(30), startTime.plusMinutes(30), item1, user1, BookingStatus.APPROVED);
		bookingRepository.save(booking1);
		Booking booking2 = new Booking(0, startTime.minusMinutes(10), startTime.plusMinutes(10), item2, user2, BookingStatus.CANCELED);
		bookingRepository.save(booking2);

		Optional<Booking> result = bookingRepository.findFirstByItemAndStartBeforeAndStatus(item1, startTime, BookingStatus.APPROVED, Sort.by(Sort.Order.desc("id")));
		assertTrue(result.isPresent());
		Booking newBooking = result.get();
		assertTrue(newBooking.getId() > 0);
		assertEquals(item1.getId(), newBooking.getItem().getId());
		assertEquals(user1.getId(), newBooking.getBooker().getId());
	}

	@Test
	public void shouldFindlastBookings() {
		LocalDateTime time = LocalDateTime.now();
		Booking booking1 = new Booking(0, time.minusMinutes(10), time.plusMinutes(10), item1, user1, BookingStatus.APPROVED);
		bookingRepository.save(booking1);
		Booking booking2 = new Booking(0, time.minusMinutes(5), time.plusMinutes(5), item2, user2, BookingStatus.APPROVED);
		bookingRepository.save(booking2);

		List<Booking> bookings = bookingRepository.findlastBookings(List.of(item1, item2), time, BookingStatus.APPROVED, Sort.by(Sort.Order.asc("id")));
		assertEquals(2, bookings.size());
		Booking newBooking = bookings.get(0);
		assertTrue(newBooking.getId() > 0);
		assertEquals(item1.getId(), newBooking.getItem().getId());
		assertEquals(user1.getId(), newBooking.getBooker().getId());

		newBooking = bookings.get(1);
		assertTrue(newBooking.getId() > 0);
		assertEquals(item2.getId(), newBooking.getItem().getId());
		assertEquals(user2.getId(), newBooking.getBooker().getId());

	}

	@Test
	public void shouldFindNextBookings() {
		LocalDateTime time = LocalDateTime.now();
		Booking booking1 = new Booking(0, time.plusMinutes(10), time.plusMinutes(30), item1, user1, BookingStatus.APPROVED);
		bookingRepository.save(booking1);
		Booking booking2 = new Booking(0, time.plusMinutes(5), time.plusMinutes(20), item2, user2, BookingStatus.APPROVED);
		bookingRepository.save(booking2);

		List<Booking> bookings = bookingRepository.findNextBookings(List.of(item1, item2), time, BookingStatus.APPROVED, Sort.by(Sort.Order.asc("id")));
		assertEquals(2, bookings.size());
		Booking newBooking = bookings.get(0);
		assertTrue(newBooking.getId() > 0);
		assertEquals(item1.getId(), newBooking.getItem().getId());
		assertEquals(user1.getId(), newBooking.getBooker().getId());

		newBooking = bookings.get(1);
		assertTrue(newBooking.getId() > 0);
		assertEquals(item2.getId(), newBooking.getItem().getId());
		assertEquals(user2.getId(), newBooking.getBooker().getId());
	}

	@Test
	public void shouldFindByItemOwnerAndEndBeforeAndStatus() {
		Booking booking = new Booking(0, LocalDateTime.now().minusHours(10), LocalDateTime.now().minusHours(2), item1, user1, BookingStatus.APPROVED);
		bookingRepository.save(booking);

		List<Booking> bookings = bookingRepository.findByItemOwnerAndEndBeforeAndStatus(user1, LocalDateTime.now(), BookingStatus.APPROVED, PageRequest.of(0, 10), Sort.by(Sort.Order.asc("id")));
		assertEquals(1, bookings.size());
		Booking newBooking = bookings.get(0);
		assertTrue(newBooking.getId() > 0);
		assertEquals(item1.getId(), newBooking.getItem().getId());
		assertEquals(user1.getId(), newBooking.getBooker().getId());
	}

	@Test
	public void shouldFindByItemOwnerAndEndAfter() {
		Booking booking = new Booking(0, LocalDateTime.now().plusMinutes(10), LocalDateTime.now().plusHours(2), item1, user1, BookingStatus.APPROVED);
		bookingRepository.save(booking);
		List<Booking> bookings = bookingRepository.findByItemOwnerAndEndAfter(user1, LocalDateTime.now(), PageRequest.of(0, 10), Sort.by(Sort.Order.asc("id")));
		assertEquals(1, bookings.size());
		Booking newBooking = bookings.get(0);
		assertTrue(newBooking.getId() > 0);
		assertEquals(item1.getId(), newBooking.getItem().getId());
		assertEquals(user1.getId(), newBooking.getBooker().getId());
	}

	@Test
	public void shouldFindByItemOwnerAndStatus() {
		Booking booking = new Booking(0, LocalDateTime.now(), LocalDateTime.now().plusHours(2), item1, user1, BookingStatus.APPROVED);
		bookingRepository.save(booking);
		List<Booking> bookings = bookingRepository.findByItemOwnerAndStatus(user1, BookingStatus.APPROVED, PageRequest.of(0, 10), Sort.by(Sort.Order.asc("id")));
		assertEquals(1, bookings.size());
		Booking newBooking = bookings.get(0);
		assertTrue(newBooking.getId() > 0);
		assertEquals(item1.getId(), newBooking.getItem().getId());
		assertEquals(user1.getId(), newBooking.getBooker().getId());
	}

	@Test
	public void shouldFindByBooker() {
		Booking booking = new Booking(0, LocalDateTime.now(), LocalDateTime.now().plusHours(2), item1, user1, BookingStatus.APPROVED);
		bookingRepository.save(booking);

		List<Booking> bookings = bookingRepository.findByBooker(user1, PageRequest.of(0, 10), Sort.by(Sort.Order.asc("id")));
		assertEquals(1, bookings.size());
		Booking newBooking = bookings.get(0);
		assertTrue(newBooking.getId() > 0);
		assertEquals(item1.getId(), newBooking.getItem().getId());
		assertEquals(user1.getId(), newBooking.getBooker().getId());
	}

	@Test
	public void shouldFindByBookerAndStartBeforeAndEndAfter() {
		Booking booking = new Booking(0, LocalDateTime.now().minusMinutes(10), LocalDateTime.now().plusHours(2), item1, user1, BookingStatus.APPROVED);
		bookingRepository.save(booking);

		List<Booking> bookings = bookingRepository.findByBookerAndStartBeforeAndEndAfter(user1, LocalDateTime.now(), LocalDateTime.now(), PageRequest.of(0, 10), Sort.by(Sort.Order.asc("id")));
		assertEquals(1, bookings.size());
		Booking newBooking = bookings.get(0);
		assertTrue(newBooking.getId() > 0);
		assertEquals(item1.getId(), newBooking.getItem().getId());
		assertEquals(user1.getId(), newBooking.getBooker().getId());
	}

	@Test
	public void shouldFindByBookerAndEndBeforeAndStatus() {
		Booking booking = new Booking(0, LocalDateTime.MAX.minusMinutes(5), LocalDateTime.now().minusMinutes(1), item1, user1, BookingStatus.APPROVED);
		bookingRepository.save(booking);

		List<Booking> bookings = bookingRepository.findByBookerAndEndBeforeAndStatus(user1, LocalDateTime.now().plusMinutes(5), BookingStatus.APPROVED, PageRequest.of(0, 10), Sort.by(Sort.Order.asc("id")));
		assertEquals(1, bookings.size());
		Booking newBooking = bookings.get(0);
		assertTrue(newBooking.getId() > 0);
		assertEquals(item1.getId(), newBooking.getItem().getId());
		assertEquals(user1.getId(), newBooking.getBooker().getId());
	}

	@Test
	public void shouldFindByBookerAndEndAfter() {
		Booking booking1 = new Booking(0, LocalDateTime.now(), LocalDateTime.now().plusMinutes(10), item1, user1, BookingStatus.APPROVED);
		bookingRepository.save(booking1);

		List<Booking> bookings = bookingRepository.findByBookerAndEndAfter(user1, LocalDateTime.now(), PageRequest.of(0, 10), Sort.by(Sort.Order.asc("id")));
		assertEquals(1, bookings.size());
		Booking newBooking = bookings.get(0);
		assertTrue(newBooking.getId() > 0);
		assertEquals(item1.getId(), newBooking.getItem().getId());
		assertEquals(user1.getId(), newBooking.getBooker().getId());
	}

	@Test
	public void shouldFindByBookerAndStatus() {
		Booking booking = new Booking(0, LocalDateTime.now(), LocalDateTime.now().plusHours(2), item1, user1, BookingStatus.APPROVED);
		bookingRepository.save(booking);

		List<Booking> bookings = bookingRepository.findByBookerAndStatus(user1, BookingStatus.APPROVED, PageRequest.of(0, 10), Sort.by(Sort.Order.asc("id")));
		assertEquals(1, bookings.size());
		Booking newBooking = bookings.get(0);
		assertTrue(newBooking.getId() > 0);
		assertEquals(item1.getId(), newBooking.getItem().getId());
		assertEquals(user1.getId(), newBooking.getBooker().getId());
	}

	@Test
	public void shouldFindByItemOwner() {
		Booking booking = new Booking(0, LocalDateTime.now(), LocalDateTime.now().plusHours(2), item1, user1, BookingStatus.APPROVED);
		bookingRepository.save(booking);

		List<Booking> bookings = bookingRepository.findByItemOwner(user1, Sort.by(Sort.Order.asc("id")), PageRequest.of(0, 10));
		assertEquals(1, bookings.size());
		Booking newBooking = bookings.get(0);
		assertTrue(newBooking.getId() > 0);
		assertEquals(item1.getId(), newBooking.getItem().getId());
		assertEquals(user1.getId(), newBooking.getBooker().getId());
	}

	@Test
	public void shouldFindFirstByItemAndStartAfterAndStatus() {
		Booking booking = new Booking(0, LocalDateTime.now().plusMinutes(10), LocalDateTime.now().plusHours(2), item1, user1, BookingStatus.APPROVED);
		bookingRepository.save(booking);

		Optional<Booking> foundBooking = bookingRepository.findFirstByItemAndStartAfterAndStatus(item1, LocalDateTime.now(), BookingStatus.APPROVED, Sort.by(Sort.Order.asc("id")));
		assertTrue(foundBooking.isPresent());
		Booking newBooking = foundBooking.get();
		assertTrue(newBooking.getId() > 0);
		assertEquals(item1.getId(), newBooking.getItem().getId());
		assertEquals(user1.getId(), newBooking.getBooker().getId());
	}


}
