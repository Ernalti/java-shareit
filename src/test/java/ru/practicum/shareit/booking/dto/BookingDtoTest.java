package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.enums.BookingStatus;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@JsonTest
class BookingDtoTest {
	@Autowired
	private JacksonTester<BookingDto> json;
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSS");
	@Test
	public void testBookingDto() throws IOException {
		LocalDateTime start = LocalDateTime.now();
		LocalDateTime end = start.plusHours(2);

		BookingDto.Booker booker = new BookingDto.Booker(1, "bookerName");
		BookingDto.ShortItem item = new BookingDto.ShortItem(2, "itemName");

		BookingDto bookingDto = BookingDto.builder()
				.id(1)
				.start(start)
				.end(end)
				.item(item)
				.booker(booker)
				.status(BookingStatus.APPROVED)
				.build();

		JsonContent<BookingDto> result = json.write(bookingDto);

		assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
		assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo(start.format(formatter));
		assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(start.format(formatter));
		assertThat(result).extractingJsonPathNumberValue("$.item.id").isEqualTo(2);
		assertThat(result).extractingJsonPathStringValue("$.item.name").isEqualTo("itemName");
		assertThat(result).extractingJsonPathNumberValue("$.booker.id").isEqualTo(1);
		assertThat(result).extractingJsonPathStringValue("$.booker.name").isEqualTo("bookerName");
		assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo("APPROVED");
	}


}