package ru.practicum.shareit.booking.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.exceptions.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {

	private final BookingRepository bookingRepository;
	private final UserRepository userRepository;
	private final ItemRepository itemRepository;

	@Autowired
	public BookingServiceImpl(BookingRepository bookingRepository, UserRepository userRepository, ItemRepository itemRepository) {
		this.bookingRepository = bookingRepository;
		this.userRepository = userRepository;
		this.itemRepository = itemRepository;
	}

	@Override
	@Transactional
	public BookingDto addBooking(int userId, BookingDto bookingDto) {
		Item item = itemRepository.findById(bookingDto.getItemId()).orElseThrow();
		if (item.getOwner().getId()==userId) {
			throw new NotFoundException("The user " + userId + " cannot booking his item " + item);
		}
		if (!item.getAvailable()) {
			throw new NotAvailableException("Item " + item.getId() + " not available");
		}
		User user = userRepository.findById(userId).orElseThrow();
		Booking booking = BookingMapper.toBooking(bookingDto, user, item);
		booking.setStatus(BookingStatus.WAITING);
		if (!booking.getEnd().isAfter(booking.getStart())) {
			throw new DateTimeException("End time can't be before start");
		}
		Booking res = bookingRepository.save(booking);
		log.info("Add booking: {}", res);
		return BookingMapper.toBookingDto(res);
	}

	@Override
	@Transactional
	public BookingDto approveBooking(int userId, int id, boolean approved) {
		Booking booking = bookingRepository.findById(id).orElseThrow();
		Item item = booking.getItem();
		if (item.getOwner().getId()!=userId) {
			throw new AuthorizationErrorException("User " + userId + " is not the owner of the item");
		}
		if (!booking.getStatus().equals(BookingStatus.WAITING)) {
			throw new StatusException("You can't change status after it has been changed");
		}
		BookingStatus status = approved ? BookingStatus.APPROVED : BookingStatus.REJECTED;
		booking.setStatus(status);
		Booking res = bookingRepository.save(booking);
		log.info("Booking approved: {} ", res);
		return BookingMapper.toBookingDto(res);
	}

	@Override
	public BookingDto getBooking(int userId, int id) {
		Booking booking = bookingRepository.findById(id).orElseThrow();
		if (userId!=booking.getBooker().getId() && userId!=booking.getItem().getOwner().getId()) {
			throw new AuthorizationErrorException("User " + userId + " can't view booking " + id);
		}
		log.info("Get booking: {}", booking);
		return BookingMapper.toBookingDto(booking);
	}

	@Override
	public List<BookingDto> getOwnerBookings(int userId, String state) {
		User user = userRepository.findById(userId).orElseThrow();
		LocalDateTime time = LocalDateTime.now();
		Sort sort = Sort.by("start").descending();
		List<Booking> res;
		switch (state) {
			case "ALL":
				res = bookingRepository.findByItemOwner(user, sort);
				break;
			case "CURRENT":
				res = bookingRepository.findByItemOwnerAndStartBeforeAndEndAfter(user, time, time, sort);
				break;
			case "PAST":
				res = bookingRepository.findByItemOwnerAndEndBeforeAndStatus(user, time, BookingStatus.APPROVED, sort);
				break;
			case "FUTURE":
				res = bookingRepository.findByItemOwnerAndEndAfter(user, time, sort);
				break;
			case "WAITING":
				res = bookingRepository.findByItemOwnerAndStatus(user, BookingStatus.WAITING, sort);
				break;
			case "REJECTED":
				res = bookingRepository.findByItemOwnerAndStatus(user, BookingStatus.REJECTED, sort);
				break;
			default:
				throw new StatusException("Unknown state: " + state);

		}
		log.info("Get owner bookings. User: {}; state: {}; result: {}",userId,state, res);
		return BookingMapper.toListBookingDto(res);
	}

	@Override
	public List<BookingDto> getBookings(int userId, String state) {
		User user = userRepository.findById(userId).orElseThrow();
		LocalDateTime time = LocalDateTime.now();
		Sort sort = Sort.by("start").descending();
		List<Booking> res;
		switch (state) {
			case "ALL":
				res = bookingRepository.findByBooker(user, sort);
				break;
			case "CURRENT":
				res = bookingRepository.findByBookerAndStartBeforeAndEndAfter(user, time, time, sort);
				break;
			case "PAST":
				res = bookingRepository.findByBookerAndEndBeforeAndStatus(user, time, BookingStatus.APPROVED, sort);
				break;
			case "FUTURE":
				res = bookingRepository.findByBookerAndEndAfter(user, time, sort);
				break;
			case "WAITING":
				res = bookingRepository.findByBookerAndStatus(user, BookingStatus.WAITING, sort);
				break;
			case "REJECTED":
				res = bookingRepository.findByBookerAndStatus(user, BookingStatus.REJECTED, sort);
				break;
			default:
				throw new StatusException("Unknown state: " + state);
		}
		log.info("Get owner bookings. User: {}; state: {}; result: {}",userId,state, res);
		return BookingMapper.toListBookingDto(res);
	}
}
