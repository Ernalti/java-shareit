package ru.practicum.shareit.request.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class ItemRequestMapper {
	public ItemRequestDto toItemRequestDTO(ItemRequest itemRequest) {
		return ItemRequestDto.builder()
				.id(itemRequest.getId())
				.description(itemRequest.getDescription())
				.requestorId(itemRequest.getRequestor().getId())
				.created(itemRequest.getCreated())
				.build();
	}

	public ItemRequest toItemRequest(ItemRequestDto itemRequestDTO, User user) {
		return ItemRequest.builder()
				.id(itemRequestDTO.getId())
				.description(itemRequestDTO.getDescription())
				.requestor(user)
				.created(LocalDateTime.now())
				.build();
	}

	public List<ItemRequestDto> toListItemRequestDto(List<ItemRequest> itemRequests) {
		return itemRequests.stream()
				.map(ItemRequestMapper::toItemRequestDTO)
				.collect(Collectors.toList());
	}


}

