package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

	ItemDto addItem(Integer userId, ItemDto itemDto);

	ItemDto updateItem(Integer itemId, Integer userId, ItemDto itemDto);

	ItemDto getItemById(Integer itemId);

	List<ItemDto> getOwnerItems(Integer userId);

	List<ItemDto> searchItemsByText(String text);

	void clearItems();

	CommentDto addComment(Integer itemId, Integer userId, CommentDto commentDto);
}