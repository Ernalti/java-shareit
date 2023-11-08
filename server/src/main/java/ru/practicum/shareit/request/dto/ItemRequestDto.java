package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */

@Data
@Builder
public class ItemRequestDto {

	private int id;

	@NotBlank
	private String description;
	private int requestorId;
	private LocalDateTime created;
	private List<ItemDto> items;

}
