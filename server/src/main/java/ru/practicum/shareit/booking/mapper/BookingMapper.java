package ru.practicum.shareit.booking.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;


@UtilityClass
public class BookingMapper {

	public BookingDto toBookingDto(Booking booking) {
		if (booking != null) {
			return BookingDto.builder()
					.id(booking.getId())
					.start(booking.getStart())
					.end(booking.getEnd())
					.item(new BookingDto.ShortItem(booking.getItem().getId(), booking.getItem().getName()))
					.booker(new BookingDto.Booker(booking.getBooker().getId(), booking.getBooker().getName()))
					.status(booking.getStatus())
					.build();
		} else {
			return null;
		}
	}

	public Booking toBooking(BookingDto bookingDto, User user, Item item) {
		return new Booking(bookingDto.getId(),
				bookingDto.getStart(),
				bookingDto.getEnd(),
				item,
				user,
				bookingDto.getStatus());
	}

	public List<BookingDto> toListBookingDto(List<Booking> bookings) {
		return bookings.stream()
				.map(BookingMapper::toBookingDto)
				.collect(Collectors.toList());
	}


}
