package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.constants.Constant;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import static ru.practicum.shareit.constants.Constant.USER_ID_HEADER;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {

	private final BookingClient bookingClient;

	@PostMapping
	public ResponseEntity<Object> addBooking(@RequestHeader(Constant.USER_ID_HEADER) @Positive int userId,
	                             @Valid @RequestBody BookingDto bookingDto) {
		log.info("Add booking {}", bookingDto);
		return bookingClient.addBooking(userId, bookingDto);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<Object> approveBooking(@RequestHeader(USER_ID_HEADER) @Positive int userId,
	                                 @PathVariable @Positive int id,
	                                 @RequestParam boolean approved) {
		log.info("Change approve status in booking id {} to {}", id, approved);
		return bookingClient.approveBooking(userId, id, approved);

	}

	@GetMapping("/{id}")
	public ResponseEntity<Object> getBooking(@RequestHeader(USER_ID_HEADER) @Positive int userId, @PathVariable @Positive int id) {
		log.info("Get booking {}", id);
		return bookingClient.getBooking(userId, id);
	}

	@GetMapping
	public ResponseEntity<Object> getBookings(@RequestHeader(USER_ID_HEADER) @Positive int userId,
	                                    @RequestParam(defaultValue = "ALL") String state,
	                                    @RequestParam(value = "from", defaultValue = "0")  @PositiveOrZero int from,
	                                    @RequestParam(value = "size", defaultValue = "10") @Positive int size) {
		log.info("Get bookings. User: {}; State {}; From {}; Size {}", userId, state, from, size);
		return bookingClient.getBookings(userId, state, from, size);
	}

	@GetMapping("/owner")
	public ResponseEntity<Object> getOwnerBookings(@RequestHeader(USER_ID_HEADER) @Positive int userId,
	                                         @RequestParam(defaultValue = "ALL") String state,
	                                         @RequestParam(value = "from", defaultValue = "0")  @PositiveOrZero int from,
	                                         @RequestParam(value = "size", defaultValue = "10") @Positive int size) {
		log.info("Get owner bookings. User {}; State {}; From {}; Size {}", userId, state, from, size);
		return bookingClient.getOwnerBookings(userId, state, from, size);
	}
}
