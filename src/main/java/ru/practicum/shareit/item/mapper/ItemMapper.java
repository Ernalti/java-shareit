package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemMapper {

	public ItemDto toItemDto(Item item) {
		return new ItemDto(
				item.getId(),
				item.getName(),
				item.getDescription(),
				item.getAvailable(),
				item.getOwner(),
				item.getRequest());
	}

	public Item toItem(ItemDto itemDto) {
		return new Item(
				itemDto.getId(),
				itemDto.getName(),
				itemDto.getDescription(),
				itemDto.getAvailable(),
				itemDto.getOwner(),
				itemDto.getRequest());
	}

	public List<ItemDto> toListItemDto(List<Item> items) {
		return items.stream()
				.map(this::toItemDto)
				.collect(Collectors.toList());
	}

}
