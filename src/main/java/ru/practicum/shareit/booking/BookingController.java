package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.enums.BookingState;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@Validated
public class BookingController {

	private final BookingService bookingService;

	@Autowired
	public BookingController(BookingService bookingService) {
		this.bookingService = bookingService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public BookingResponseDto addBooking(@RequestHeader("X-Sharer-User-Id") Integer userId,
	                                     @Valid @RequestBody BookingRequestDto bookingRequestDto) {
		log.info("Add booking {}",bookingRequestDto);
		BookingResponseDto res = bookingService.addBooking(userId, bookingRequestDto);
		return  res;
	}

	@PatchMapping("/{id}")
	public BookingResponseDto approveBooking(@RequestHeader("X-Sharer-User-Id") Integer userId,
	                                         @PathVariable Integer id,
	                                         @RequestParam Boolean approved) {
		log.info("Change approve status in booking id {} to {}",id,approved);
		return bookingService.approveBooking(userId, id,approved);

	}

	@GetMapping("/{id}")
	public BookingResponseDto getBooking(@PathVariable Integer id) {
		log.info("Get booking {}",id);
		return bookingService.getBooking(id);
	}

	@GetMapping
	public List<BookingResponseDto> getBookings(@RequestHeader("X-Sharer-User-Id") Integer userId,
	                                               @RequestParam(defaultValue = "ALL") BookingState state) {
		return bookingService.getBookings(userId ,state);
	}

	@GetMapping("/owner")
	public List<BookingResponseDto> getOwnerBookings(@RequestHeader("X-Sharer-User-Id") Integer userId,

	                                                 @RequestParam(defaultValue = "ALL") BookingState state) {
		return bookingService.getOwnerBookings(userId,  state);
	}
}
