package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

public class BookingMapper {

	private BookingMapper() {}
	public static BookingResponseDto toBookingDto(Booking booking) {
		return new BookingResponseDto(booking.getId(),
				booking.getStart(),
				booking.getEnd(),
				booking.getItem(),
				booking.getBooker(),
				booking.getStatus());
	}

	public static Booking toBooking(BookingRequestDto bookingRequestDto, User user, Item item) {
		return new Booking(bookingRequestDto.getId(),
				bookingRequestDto.getStart(),
				bookingRequestDto.getEnd(),
				item,
				user,
				bookingRequestDto.getStatus());
	}

	public static List<BookingResponseDto> toListBookingDto(List<Booking> bookings) {
		return bookings.stream()
				.map(BookingMapper::toBookingDto)
				.collect(Collectors.toList());
	}
}
