package ru.practicum.shareit.booking.dto;

import java.time.format.DateTimeFormatter;

public class CustomDateTimeFormatter {
	public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSS");

}
