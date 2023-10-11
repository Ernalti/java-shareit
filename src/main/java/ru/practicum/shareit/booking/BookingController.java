package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
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
	public BookingDto addBooking(@RequestHeader("X-Sharer-User-Id") Integer userId,
	                             @Valid @RequestBody BookingDto bookingDto) {
		log.info("Add booking {}", bookingDto);
		BookingDto res = bookingService.addBooking(userId, bookingDto);
		return res;
	}

	@PatchMapping("/{id}")
	public BookingDto approveBooking(@RequestHeader("X-Sharer-User-Id") Integer userId,
	                                 @PathVariable Integer id,
	                                 @RequestParam Boolean approved) {
		log.info("Change approve status in booking id {} to {}", id, approved);
		return bookingService.approveBooking(userId, id, approved);

	}

	@GetMapping("/{id}")
	public BookingDto getBooking(@RequestHeader("X-Sharer-User-Id") Integer userId, @PathVariable Integer id) {
		log.info("Get booking {}", id);
		return bookingService.getBooking(userId, id);
	}

	@GetMapping
	public List<BookingDto> getBookings(@RequestHeader("X-Sharer-User-Id") Integer userId,
	                                    @RequestParam(defaultValue = "ALL") String state) {
		return bookingService.getBookings(userId, state);
	}

	@GetMapping("/owner")
	public List<BookingDto> getOwnerBookings(@RequestHeader("X-Sharer-User-Id") Integer userId,

	                                         @RequestParam(defaultValue = "ALL") String state) {
		return bookingService.getOwnerBookings(userId, state);
	}
}
