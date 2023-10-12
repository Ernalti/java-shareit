package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Data
@AllArgsConstructor
@Builder
public class ItemResponseDto {

	private Integer id;

	private String name;

	private String description;

	private Boolean available;

	private BookingDto lastBooking;

	private BookingDto nextBooking;

	private List<CommentDto> comments;

}
