package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {
	@MockBean
	private BookingService bookingService;

	@Autowired
	private MockMvc mvc;

	@Autowired
	private ObjectMapper objectMapper;

	private BookingDto booking;

	@BeforeEach
	void setUp() {
		booking = BookingDto.builder()
				.id(1)
				.start(LocalDateTime.of(2023, 10, 26, 12, 0))
				.end(LocalDateTime.of(2023, 10, 26, 14, 0))
				.itemId(2)
				.status(BookingStatus.APPROVED)
				.build();
	}

	@Test
	void shouldAddBooking() throws Exception {
		when(bookingService.addBooking(anyInt(), any(BookingDto.class))).thenReturn(booking);

		mvc.perform(post("/bookings")
						.header("X-Sharer-User-Id", 1)
						.content(objectMapper.writeValueAsString(booking))
						.characterEncoding(StandardCharsets.UTF_8)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(content().json(objectMapper.writeValueAsString(booking)));
	}

	@Test
	void shouldApproveBooking() throws Exception {
		when(bookingService.approveBooking(anyInt(), anyInt(), anyBoolean())).thenReturn(booking);

		mvc.perform(patch("/bookings/1?approved=true")
						.header("X-Sharer-User-Id", 1)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().json(objectMapper.writeValueAsString(booking)));
	}

	@Test
	void shouldGetBooking() throws Exception {
		when(bookingService.getBooking(anyInt(), anyInt())).thenReturn(booking);

		mvc.perform(get("/bookings/1")
						.header("X-Sharer-User-Id", 1)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().json(objectMapper.writeValueAsString(booking)));
	}

	@Test
	void shouldGetBookings() throws Exception {
		when(bookingService.getBookings(anyInt(), anyString(), anyInt(), anyInt()))
				.thenReturn(List.of(booking));

		mvc.perform(get("/bookings")
						.header("X-Sharer-User-Id", 1)
						.param("state", "ALL")
						.param("from", "0")
						.param("size", "10")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().json(objectMapper.writeValueAsString(List.of(booking))));
	}

	@Test
	void shouldGetOwnerBookings() throws Exception {
		when(bookingService.getOwnerBookings(anyInt(), anyString(), anyInt(), anyInt()))
				.thenReturn(List.of(booking));

		mvc.perform(get("/bookings/owner")
						.header("X-Sharer-User-Id", 1)
						.param("state", "ALL")
						.param("from", "0")
						.param("size", "10")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().json(objectMapper.writeValueAsString(List.of(booking))));
	}
}