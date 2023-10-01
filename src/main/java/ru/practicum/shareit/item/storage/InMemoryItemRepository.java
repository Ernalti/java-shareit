package ru.practicum.shareit.item.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.exceptions.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class InMemoryItemRepository implements ItemRepository {

	private final Map<Integer, Item> items = new HashMap<>();

	private int id;

	public InMemoryItemRepository() {
		id = 1;
	}


	@Override
	public Item addItem(Item item) {
		log.info("Add item {}", item);
		item.setId(nextId());
		items.put(item.getId(), item);
		return item;
	}

	@Override
	public Item updateItem(int itemId, Item updatedItem) {
		log.info("Update item with id {} to {}", itemId, updatedItem);
		items.put(itemId, updatedItem);
		return updatedItem;
	}

	@Override
	public Item getItemById(int id) {
		log.info("Get item by Id {}", id);
		Item item = items.get(id);
		if (item == null) {
			throw new NotFoundException("User with id " + id + " not found");
		}
		return item;
	}

	@Override
	public List<Item> getOwnerItems(int ownerId) {
		log.info("Get user items. userId = {}", ownerId);
		return items.values().stream()
				.filter(item -> item.getOwner() == ownerId)
				.collect(Collectors.toList());
	}

	@Override
	public List<Item> searchItemsByText(String text) {
		log.info("Search item by text {}", text);
		String lowerText = text.toLowerCase();
		return (text != null && !text.isBlank()) ?
				items.values()
						.stream().filter(item -> item.getAvailable() &&
								(item.getName().toLowerCase().contains(lowerText) ||
										item.getDescription().toLowerCase().contains(lowerText)))
						.collect(Collectors.toList())
				: new ArrayList<>();
	}

	@Override
	public void clearItems() {
		log.info("Clear all items");
		items.clear();
	}

	private int nextId() {
		return id++;
	}
}
