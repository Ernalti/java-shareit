package ru.practicum.shareit.request.mapper;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ItemRequestMapper {
	public static ItemRequestDto toItemRequestDTO(ItemRequest itemRequest) {
		return ItemRequestDto.builder()
				.id(itemRequest.getId())
				.description(itemRequest.getDescription())
				.requestorId(itemRequest.getRequestor().getId())
				.created(itemRequest.getCreated())
				.build();
	}

	public static ItemRequest toItemRequest(ItemRequestDto itemRequestDTO, User user) {
		return ItemRequest.builder()
				.id(itemRequestDTO.getId())
				.description(itemRequestDTO.getDescription())
				.requestor(user)
				.created( LocalDateTime.now())
				.build();
	}

	public static List<ItemRequestDto> toListItemRequestDto(List<ItemRequest> itemRequests) {
		return itemRequests.stream()
				.map(ItemRequestMapper::toItemRequestDTO)
				.collect(Collectors.toList());
	}


}

