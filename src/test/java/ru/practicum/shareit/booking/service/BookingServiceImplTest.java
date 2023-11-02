package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.exceptions.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {
	@Mock
	private BookingRepository bookingRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private ItemRepository itemRepository;

	@InjectMocks
	private BookingServiceImpl bookingService;

	private User user1;
	private User user2;
	private Item item;
	private BookingDto bookingDto;
	private Booking booking;
	Page<Booking> bookingPage;

	@BeforeEach
	public void beforeEach() {
		user1 = User.builder()
				.id(1)
				.name("user")
				.email("user@yandex.ru")
				.build();
		user2 = User.builder()
				.id(2)
				.name("user2")
				.email("user2@yandex.ru")
				.build();
		item = Item.builder()
				.id(2)
				.name("item2")
				.description("Description2")
				.available(true)
				.owner(user1)
				.build();
		bookingDto = BookingDto.builder()
				.itemId(2)
				.start(LocalDateTime.now().plusMinutes(10))
				.end(LocalDateTime.now().plusHours(1))
				.status(BookingStatus.WAITING)
				.build();

		booking = new Booking(2, LocalDateTime.now().plusMinutes(10), LocalDateTime.now().plusHours(1), item, user1, BookingStatus.WAITING);
		List<Booking> bookings = List.of(booking);
		bookingPage = new PageImpl<>(bookings, PageRequest.of(0, 10), bookings.size());
		lenient().when(itemRepository.findById(2)).thenReturn(Optional.of(item));
		lenient().when(userRepository.findById(1)).thenReturn(Optional.of(user1));
		lenient().when(userRepository.findById(2)).thenReturn(Optional.of(user2));
		lenient().when(bookingRepository.save(any(Booking.class))).thenAnswer(answer -> {
			Booking savedBooking = answer.getArgument(0);
			savedBooking.setId(1);
			return savedBooking;
		});
		lenient().when(bookingRepository.findById(1)).thenReturn(Optional.of(booking));

	}

	@Test
	public void shouldAddBooking() {
		BookingDto newBooking = bookingService.addBooking(2, bookingDto);
		assertEquals(1, newBooking.getId());
		assertEquals(BookingStatus.WAITING, newBooking.getStatus());
		assertEquals(2, newBooking.getItem().getId());
		assertEquals(2, newBooking.getBooker().getId());
	}

	@Test
	public void shouldThrowNotFoundExceptionWhenAddingBookingForOwnItem() {
		item.setOwner(user1);
		assertThrows(NotFoundException.class, () -> bookingService.addBooking(1, bookingDto));
	}

	@Test
	public void shouldThrowNotAvailableExceptionWhenAddingBookingForUnavailableItem() {
		item.setAvailable(false);
		assertThrows(NotAvailableException.class, () -> bookingService.addBooking(2, bookingDto));
	}

	@Test
	public void shouldThrowDateTimeExceptionWhenStartIsAfterEnd() {
		bookingDto.setEnd(LocalDateTime.now().minusHours(1));
		assertThrows(DateTimeException.class, () -> bookingService.addBooking(2, bookingDto));
	}

	@Test
	public void shouldApproveBooking() {
		BookingDto updatedBooking = bookingService.approveBooking(1, 1, true);
		assertEquals(BookingStatus.APPROVED, updatedBooking.getStatus());
	}

	@Test
	public void shouldRejectBooking() {
		BookingDto updatedBooking = bookingService.approveBooking(1, 1, false);
		assertEquals(BookingStatus.REJECTED, updatedBooking.getStatus());
	}

	@Test
	public void shouldThrowAuthorizationErrorExceptionWhenApprovingBookingNotBelongingToUser() {
		assertThrows(AuthorizationErrorException.class, () -> bookingService.approveBooking(2, 1, true));
	}

	@Test
	public void shouldThrowStatusExceptionWhenChangingStatusAfterItHasBeenChanged() {
		booking.setStatus(BookingStatus.APPROVED);
		assertThrows(StatusException.class, () -> bookingService.approveBooking(1, 1, true));
	}

	@Test
	public void shouldGetBooking() {
		BookingDto retrievedBooking = bookingService.getBooking(1, 1);
		assertEquals(2, retrievedBooking.getId());
	}

	@Test
	public void shouldThrowAuthorizationErrorExceptionWhenGettingBookingNotBelongingToUser() {
		assertThrows(AuthorizationErrorException.class, () -> bookingService.getBooking(2, 1));
	}

	@Test
	public void shouldGetAllOwnerBookings() {

		when(bookingRepository.findByItemOwner(any(), any())).thenReturn(bookingPage);
		List<BookingDto> ownerBookings = bookingService.getOwnerBookings(1, "ALL", 0, 10);
		assertEquals(1, ownerBookings.size());
		assertEquals(2, ownerBookings.get(0).getId());
	}

	@Test
	public void shouldGetCurrentOwnerBookings() {
		when(bookingRepository.findByItemOwnerAndStartBeforeAndEndAfter(any(), any(), any(), any())).thenReturn(bookingPage);
		List<BookingDto> ownerBookings = bookingService.getOwnerBookings(1, "CURRENT", 0, 10);
		assertEquals(1, ownerBookings.size());
		assertEquals(2, ownerBookings.get(0).getId());
	}

	@Test
	public void shouldGetPastOwnerBookings() {
		when(bookingRepository.findByItemOwnerAndEndBeforeAndStatus(any(), any(), any(), any())).thenReturn(bookingPage);
		List<BookingDto> ownerBookings = bookingService.getOwnerBookings(1, "PAST", 0, 10);
		assertEquals(1, ownerBookings.size());
		assertEquals(2, ownerBookings.get(0).getId());
	}

	@Test
	public void shouldGetFutureOwnerBookings() {
		when(bookingRepository.findByItemOwnerAndEndAfter(any(), any(), any())).thenReturn(bookingPage);
		List<BookingDto> ownerBookings = bookingService.getOwnerBookings(1, "FUTURE", 0, 10);
		assertEquals(1, ownerBookings.size());
		assertEquals(2, ownerBookings.get(0).getId());
	}

	@Test
	public void shouldGetWaitingOwnerBookings() {
		when(bookingRepository.findByItemOwnerAndStatus(any(), any(), any())).thenReturn(bookingPage);
		List<BookingDto> ownerBookings = bookingService.getOwnerBookings(1, "WAITING", 0, 10);
		assertEquals(1, ownerBookings.size());
		assertEquals(2, ownerBookings.get(0).getId());
	}

	@Test
	public void shouldGetRejectedOwnerBookings() {
		when(bookingRepository.findByItemOwnerAndStatus(any(), any(), any())).thenReturn(bookingPage);
		List<BookingDto> ownerBookings = bookingService.getOwnerBookings(1, "REJECTED", 0, 10);
		assertEquals(1, ownerBookings.size());
		assertEquals(2, ownerBookings.get(0).getId());
	}

	@Test
	public void shouldThrowStatusExceptionForUnknownStateForGetOwnerBookings() {
		assertThrows(StatusException.class, () -> bookingService.getOwnerBookings(1, "UNKNOWN", 0, 10));
	}

	@Test
	public void shouldGetAllBookings() {
		when(bookingRepository.findByBooker(any(), any())).thenReturn(bookingPage);
		List<BookingDto> ownerBookings = bookingService.getBookings(1, "ALL", 0, 10);
		assertEquals(1, ownerBookings.size());
		assertEquals(2, ownerBookings.get(0).getId());
	}

	@Test
	public void shouldGetCurrentBookings() {
		when(bookingRepository.findByBookerAndStartBeforeAndEndAfter(any(), any(), any(), any())).thenReturn(bookingPage);
		List<BookingDto> ownerBookings = bookingService.getBookings(1, "CURRENT", 0, 10);
		assertEquals(1, ownerBookings.size());
		assertEquals(2, ownerBookings.get(0).getId());
	}

	@Test
	public void shouldGetPastBookings() {
		when(bookingRepository.findByBookerAndEndBeforeAndStatus(any(), any(), any(), any())).thenReturn(bookingPage);
		List<BookingDto> ownerBookings = bookingService.getBookings(1, "PAST", 0, 10);
		assertEquals(1, ownerBookings.size());
		assertEquals(2, ownerBookings.get(0).getId());
	}

	@Test
	public void shouldGetFutureBookings() {
		when(bookingRepository.findByBookerAndEndAfter(any(), any(), any())).thenReturn(bookingPage);
		List<BookingDto> ownerBookings = bookingService.getBookings(1, "FUTURE", 0, 10);
		assertEquals(1, ownerBookings.size());
		assertEquals(2, ownerBookings.get(0).getId());
	}

	@Test
	public void shouldGetWaitingBookings() {
		when(bookingRepository.findByBookerAndStatus(any(), any(), any())).thenReturn(bookingPage);
		List<BookingDto> ownerBookings = bookingService.getBookings(1, "WAITING", 0, 10);
		assertEquals(1, ownerBookings.size());
		assertEquals(2, ownerBookings.get(0).getId());
	}

	@Test
	public void shouldGetRejectedBookings() {
		when(bookingRepository.findByBookerAndStatus(any(), any(), any())).thenReturn(bookingPage);
		List<BookingDto> ownerBookings = bookingService.getBookings(1, "REJECTED", 0, 10);
		assertEquals(1, ownerBookings.size());
		assertEquals(2, ownerBookings.get(0).getId());
	}

	@Test
	public void shouldThrowStatusExceptionForUnknownStateForGetBookings() {
		assertThrows(StatusException.class, () -> bookingService.getOwnerBookings(1, "UNKNOWN", 0, 10));
	}
}