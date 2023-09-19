package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

	Item addItem(int userId, Item item);

	Item updateItem(int itemId, int userId, Item updatedItem);

	Item getItemById(int itemId);

	List<Item> getOwnerItems(int userId);

	List<Item> searchItemsByText(String text);

	void clearItems();

}