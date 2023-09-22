package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {

	Item addItem(Item item);

	Item updateItem(int itemId, Item updatedItem);

	Item getItemById(int id);

	List<Item> getOwnerItems(int ownerId);

	List<Item> searchItemsByText(String text);

	void clearItems();

}
