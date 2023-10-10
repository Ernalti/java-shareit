package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.enums.BookingState;

import java.util.List;

public interface BookingService {

	BookingResponseDto addBooking(Integer userId, BookingRequestDto bookingRequestDto);

	BookingResponseDto approveBooking(Integer userId, Integer id, Boolean approved);

	BookingResponseDto getBooking(Integer id);

	List<BookingResponseDto> getBookings(Integer userId,  BookingState state);

	List<BookingResponseDto> getOwnerBookings(Integer userId, BookingState state);

}
