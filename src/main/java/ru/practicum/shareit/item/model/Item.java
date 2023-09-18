package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */

@Data
@AllArgsConstructor
public class Item {
	private Integer id;
	@NotBlank
	private String name;
	@NotBlank
	private String description;
	@NotNull
	private boolean available;

	private Integer owner;

	private ItemRequest request;
}
