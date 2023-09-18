package ru.practicum.shareit.item.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemMapper {

	public static ItemDto toItemDto(Item item) {
		return new ItemDto(
				item.getId(),
				item.getName(),
				item.getDescription(),
				item.isAvailable(),
				item.getOwner() != null ? item.getOwner().getId() : null,
				item.getRequest() != null ? item.getRequest().getId() : null);
	}

	public static Item toItem(ItemDto itemDto) {
		return new Item(
				itemDto.getId(),
				itemDto.getName(),
				itemDto.getDescription(),
				itemDto.isAvailable(),
				userService.getUserById()
				null
		);
//				itemDto.getRequest() != null ? userService.getUserById() : null);
	}

	public static List<ItemDto> toListItemDto(List<Item> items) {
		return items.stream()
				.map(ItemMapper::toItemDto)
				.collect(Collectors.toList());
	}

}
