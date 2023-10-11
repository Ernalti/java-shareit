package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;

import java.util.List;

public interface ItemService {

	ItemResponseDto addItem(Integer userId, ItemDto itemDto);

	ItemResponseDto updateItem(Integer itemId, Integer userId, ItemDto itemDto);

	ItemResponseDto getItemById(Integer itemId);

	List<ItemResponseDto> getOwnerItems(Integer userId);

	List<ItemResponseDto> searchItemsByText(String text);

	void clearItems();

	CommentDto addComment(Integer itemId, Integer userId, CommentDto commentDto);
}