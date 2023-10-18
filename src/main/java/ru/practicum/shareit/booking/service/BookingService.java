package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {

	BookingDto addBooking(int userId, BookingDto bookingDto);

	BookingDto approveBooking(int userId, int id, boolean approved);

	BookingDto getBooking(int userId, int id);

	List<BookingDto> getBookings(int userId, String state);

	List<BookingDto> getOwnerBookings(int userId, String state);

}
