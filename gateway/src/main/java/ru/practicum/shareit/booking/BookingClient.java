package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.exception.exceptions.StatusException;

import java.util.Map;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> getBookings(int userId, String stateStr, Integer from, Integer size) {
        BookingState state = strToBookingState(stateStr);
        Map<String, Object> parameters = Map.of(
                "state", state.name(),
                "from", from,
                "size", size
        );
        return get("?state={state}&from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> getBooking(int userId, Integer bookingId) {
        return get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> getOwnerBookings(int userId, String stateStr, int from, int size) {
        BookingState state = strToBookingState(stateStr);
        Map<String, Object> parameters = Map.of(
                "state", state.name(),
                "from", from,
                "size", size
        );
        return get("/owner?state={state}&from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> approveBooking(int userId, int id, boolean approved) {
        Map<String, Object> parameters = Map.of("approved", approved);
        return patch("/" + id + "?approved={approved}", userId, parameters, null);
    }

    public ResponseEntity<Object> addBooking(int userId, BookingDto bookingDto) {
        return post("", userId, bookingDto);
    }

    private BookingState strToBookingState(String stateStr) {
        try {
            return BookingState.valueOf(stateStr);
        } catch (IllegalArgumentException e) {
            throw new StatusException("Unknown state: " + stateStr);
        }
    }

}
