package ru.practicum.shareit.item.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class ItemMapper {

	public ItemDto toItemDto(Item item) {
		return ItemDto.builder()
				.id(item.getId())
				.name(item.getName())
				.description(item.getDescription())
				.available(item.getAvailable())
				.requestId(item.getRequest() == null ? null : item.getRequest().getId())
				.build();
	}

	public Item toItem(ItemDto itemDto, User owner, ItemRequest itemRequest) {
		return Item.builder()
				.id(itemDto.getId())
				.name(itemDto.getName())
				.description(itemDto.getDescription())
				.available(itemDto.getAvailable())
				.owner(owner)
				.request(itemRequest)
				.build();
	}

	public List<ItemDto> toListItemDto(List<Item> items) {
		return items.stream()
				.map(ItemMapper::toItemDto)
				.collect(Collectors.toList());
	}

}
