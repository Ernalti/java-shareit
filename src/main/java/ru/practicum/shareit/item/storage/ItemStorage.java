package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {

	Item addItem(Item item);

	Item updateItem(Item item);

	Item getItemById(int id);

	List<Item> getAllItems();

	List<Item> getOwnerItems(int ownerId);

	List<Item> searchItemsByText(String searchText);

}
