package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

	ItemDto addItem(int userId, ItemDto itemDto);

	ItemDto updateItem(int itemId, int userId, ItemDto itemDto);

	ItemDto getItemById(int itemId, int userId);

	List<ItemDto> getOwnerItems(int userId);

	List<ItemDto> searchItemsByText(String text);

	CommentDto addComment(int itemId, int userId, CommentDto commentDto);
}