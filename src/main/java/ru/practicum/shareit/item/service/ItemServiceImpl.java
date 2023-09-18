package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorageInMemory;

import java.util.List;

@Service
public class ItemServiceImpl  implements ItemService{

	private final ItemStorageInMemory itemStorage;

	@Autowired
	public ItemServiceImpl(ItemStorageInMemory itemStorage) {
		this.itemStorage = itemStorage;
	}

	@Override
	public Item addItem(int userId, Item item) {
		return itemStorage.addItem(item);
	}

	@Override
	public Item editItem(int itemId, int userId, Item item) {
		return itemStorage.updateItem(item);
	}

	@Override
	public Item getItemById(int itemId) {
		return itemStorage.getItemById(itemId);
	}

	@Override
	public List<Item> getOwnerItems(int userId) {
		return itemStorage.getOwnerItems(userId);
	}

	@Override
	public List<Item> searchItemsByText(String text) {
		return itemStorage.searchItemsByText(text);
	}

}
