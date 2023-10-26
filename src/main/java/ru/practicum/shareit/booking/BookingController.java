package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
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
	public BookingDto addBooking(@RequestHeader("X-Sharer-User-Id") int userId,
	                             @Valid @RequestBody BookingDto bookingDto) {
		log.info("Add booking {}", bookingDto);
		return bookingService.addBooking(userId, bookingDto);
	}

	@PatchMapping("/{id}")
	public BookingDto approveBooking(@RequestHeader("X-Sharer-User-Id") int userId,
	                                 @PathVariable int id,
	                                 @RequestParam boolean approved) {
		log.info("Change approve status in booking id {} to {}", id, approved);
		return bookingService.approveBooking(userId, id, approved);

	}

	@GetMapping("/{id}")
	public BookingDto getBooking(@RequestHeader("X-Sharer-User-Id") int userId, @PathVariable int id) {
		log.info("Get booking {}", id);
		return bookingService.getBooking(userId, id);
	}

	@GetMapping
	public List<BookingDto> getBookings(@RequestHeader("X-Sharer-User-Id") int userId,
	                                    @RequestParam(defaultValue = "ALL") String state,
	                                    @RequestParam(value = "from", defaultValue = "0")  @PositiveOrZero int from,
	                                    @RequestParam(value = "size", defaultValue = "10") @Positive int size) {
		log.info("Get bookings. User: {}; State: {}", userId, state);
		return bookingService.getBookings(userId, state, from, size);
	}

	@GetMapping("/owner")
	public List<BookingDto> getOwnerBookings(@RequestHeader("X-Sharer-User-Id") int userId,
	                                         @RequestParam(defaultValue = "ALL") String state,
	                                         @RequestParam(value = "from", defaultValue = "0")  @PositiveOrZero int from,
	                                         @RequestParam(value = "size", defaultValue = "10") @Positive int size) {
		log.info("Get owner bookings. User {}; State", userId, state, from, size);
		return bookingService.getOwnerBookings(userId, state, from, size);
	}
}
