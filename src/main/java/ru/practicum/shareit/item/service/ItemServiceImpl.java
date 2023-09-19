package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.exceptions.AuthorizationErrorException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.InMemoryItemStorage;
import ru.practicum.shareit.user.storage.InMemoryUserStorage;

import java.util.List;

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
		userStorage.getUserById(userId);
		item.setOwner(userId);
		return itemStorage.addItem(item);
	}

	@Override
	public Item updateItem(int itemId, int userId, Item updatedItem) {
		if (userId != getItemById(itemId).getOwner()) {
			throw new AuthorizationErrorException("User " + userId + "is not the owner of the item");
		}
		return itemStorage.updateItem(itemId, updatedItem);
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
