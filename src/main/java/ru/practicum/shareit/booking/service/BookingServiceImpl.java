package ru.practicum.shareit.booking.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.enums.BookingState;
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
	public BookingResponseDto addBooking(Integer userId, BookingRequestDto bookingRequestDto) {
		Item item = itemRepository.findById(bookingRequestDto.getItemId()).get();
		if (item.getOwner().getId().equals(userId)) {
			throw new NotFoundException("The user " + userId + " cannot booking his item " + item);
		}
		if (!item.getAvailable()) {
			throw new NotAvailableException("Item " + item.getId() + " not available");
		}
		User user = userRepository.findById(userId).get();
		Booking booking = BookingMapper.toBooking(bookingRequestDto, user, item);
		booking.setStatus(BookingStatus.WAITING);
		if (!booking.getEnd().isAfter(booking.getStart())) {
			throw new DateTimeException("End time can't be before start");
		}
		Booking res = bookingRepository.save(booking);
		log.info("Add booking: {}",res);
		return BookingMapper.toBookingDto(res);
	}

	@Override
	public BookingResponseDto approveBooking(Integer userId, Integer id, Boolean approved) {
		Booking booking = bookingRepository.findById(id).get();
		Item item = booking.getItem();
		if (!item.getOwner().getId().equals(userId)) {
			throw new AuthorizationErrorException("User " + userId + " is not the owner of the item");
		}
		BookingStatus status = approved ? BookingStatus.APPROVED : BookingStatus.REJECTED;
		booking.setStatus(status);
		Booking res = bookingRepository.save(booking);
		log.info("Booking approved: {} ",res);
		return BookingMapper.toBookingDto(res);
	}

	@Override
	public BookingResponseDto getBooking(Integer id) {
		Booking res = bookingRepository.findById(id).get();
		log.info("Get booking: {}",res);
		return BookingMapper.toBookingDto(res);
	}

	@Override
	public List<BookingResponseDto> getOwnerBookings(Integer userId, BookingState state) {
		User user = userRepository.findById(userId).get();
		LocalDateTime time = LocalDateTime.now();
		List<Booking> res = null;
		switch (state) {
			case ALL:
				res = bookingRepository.findByItemOwnerOrderByStartDesc(user);
				break;
			case CURRENT:
				res = bookingRepository.findByItemOwnerAndStartAfterAndEndBeforeOrderByStartDesc(user,time, time);
				break;
			case PAST:
				res = bookingRepository.findByItemOwnerAndStartBeforeOrderByStartDesc(user,time);
				break;
			case FUTURE:
				res = bookingRepository.findByItemOwnerAndEndAfterOrderByStartDesc(user,time);
				break;
			case WAITING:
				res = bookingRepository.findByItemOwnerAndStatusOrderByStartDesc(user, BookingStatus.WAITING);
				break;
			case REJECTED:
				res = bookingRepository.findByItemOwnerAndStatusOrderByStartDesc(user, BookingStatus.REJECTED);
				break;
			default:
				throw new UnsupportedStatusException("Unknown state: " + state);

		}
		return BookingMapper.toListBookingDto(res);
	}

	@Override
	public List<BookingResponseDto> getBookings(Integer userId, BookingState state) {
		User user = userRepository.findById(userId).get();
		LocalDateTime time = LocalDateTime.now();
		List<Booking> res = null;
		switch (state) {
			case ALL:
				res = bookingRepository.findByBookerOrderByStartDesc(user);
				break;
			case CURRENT:
				res = bookingRepository.findByBookerAndStartAfterAndEndBeforeOrderByStartDesc(user,time, time);
				break;
			case PAST:
				res = bookingRepository.findByBookerAndStartBeforeOrderByStartDesc(user,time);
				break;
			case FUTURE:
				res = bookingRepository.findByBookerAndEndAfterOrderByStartDesc(user,time);
				break;
			case WAITING:
				res = bookingRepository.findByBookerAndStatusOrderByStartDesc(user, BookingStatus.WAITING);
				break;
			case REJECTED:
				res = bookingRepository.findByBookerAndStatusOrderByStartDesc(user, BookingStatus.REJECTED);
				break;
			default:
				throw new UnsupportedStatusException("Unknown state: " + state);
		}
		return BookingMapper.toListBookingDto(res);
	}
}
