package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

public class ItemMapper {

	private ItemMapper() {
	}

	public static ItemDto toItemDto(Item item) {
		return ItemDto.builder()
				.id(item.getId())
				.name(item.getName())
				.description(item.getDescription())
				.available(item.getAvailable())
				.build();
	}

	public static Item toItem(ItemDto itemDto, User owner) {
		return new Item(
				itemDto.getId(),
				itemDto.getName(),
				itemDto.getDescription(),
				itemDto.getAvailable(),
				owner);
	}

	public static List<ItemDto> toListItemDto(List<Item> items) {
		return items.stream()
				.map(ItemMapper::toItemDto)
				.collect(Collectors.toList());
	}

}
