package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Data
@AllArgsConstructor
@Builder
public class ItemDto {

	private int id;
	@NotBlank
	private String name;
	@NotBlank
	private String description;
	@NotNull
	private Boolean available;

	private BookingDto lastBooking;

	private BookingDto nextBooking;

	private List<CommentDto> comments;

	private Integer requestId;

}
