package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

	Item addItem(int userId, Item item);

	Item editItem(int itemId, int userId, Item item);

	Item getItemById(int itemId);

	List<Item> getOwnerItems(int userId);

	List<Item> searchItemsByText(String text);

	List<Item> getItemsByUserId(int userId);

}