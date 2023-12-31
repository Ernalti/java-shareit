package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {

	ItemRequestDto addItemRequest(int userId, ItemRequestDto itemRequestDto);

	List<ItemRequestDto> getOwnerItemRequests(int userId);

	List<ItemRequestDto> getAllItemRequests(int userId, int from, int size);

	ItemRequestDto getItemRequestById(int userId, int requestId);



}
