package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {

	BookingDto addBooking(Integer userId, BookingDto bookingDto);

	BookingDto approveBooking(Integer userId, Integer id, Boolean approved);

	BookingDto getBooking(Integer userId, Integer id);

	List<BookingDto> getBookings(Integer userId, String state);

	List<BookingDto> getOwnerBookings(Integer userId, String state);

}
