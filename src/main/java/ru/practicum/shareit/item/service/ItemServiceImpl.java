package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.exceptions.AuthorizationErrorException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.InMemoryItemStorage;
import ru.practicum.shareit.user.storage.InMemoryUserStorage;

import java.util.List;

@Slf4j
@Service
public class ItemServiceImpl implements ItemService {

	private final InMemoryItemStorage itemStorage;
	private final InMemoryUserStorage userStorage;

	@Autowired
	public ItemServiceImpl(InMemoryItemStorage itemStorage, InMemoryUserStorage userStorage) {
		this.itemStorage = itemStorage;
		this.userStorage = userStorage;
	}

	@Override
	public Item addItem(int userId, Item item) {
		log.info("Add item {}", item);
		userStorage.getUserById(userId);
		item.setOwner(userId);
		return itemStorage.addItem(item);
	}

	@Override
	public Item updateItem(int itemId, int userId, Item updatedItem) {
		log.info("Update item with id {} to {}", itemId,updatedItem);
		if (userId != getItemById(itemId).getOwner()) {
			throw new AuthorizationErrorException("User " + userId + "is not the owner of the item");
		}
		return itemStorage.updateItem(itemId, updatedItem);
	}

	@Override
	public Item getItemById(int itemId) {
		log.info("Get item by Id {}", itemId);
		return itemStorage.getItemById(itemId);
	}

	@Override
	public List<Item> getOwnerItems(int userId) {
		log.info("Get user items. userId = {}", userId);
		return itemStorage.getOwnerItems(userId);
	}

	@Override
	public List<Item> searchItemsByText(String text) {
		log.info("Search item by text {}", text);
		return itemStorage.searchItemsByText(text);
	}

	@Override
	public void clearItems() {
		itemStorage.clearItems();
	}

}
