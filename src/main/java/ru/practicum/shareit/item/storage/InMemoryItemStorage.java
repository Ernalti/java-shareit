package ru.practicum.shareit.item.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.exceptions.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryItemStorage implements ItemStorage {

	private final Map<Integer, Item> items = new HashMap<>();

	private int id;

	public InMemoryItemStorage() {
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
		Item item = getItemById(itemId);

		String updatedName = updatedItem.getName();

		if (updatedName != null && !updatedName.isBlank()) {
			item.setName(updatedName);
		}

		String updatedDescription = updatedItem.getDescription();
		if (updatedDescription != null && !updatedDescription.isBlank()) {
			item.setDescription(updatedDescription);
		}

		Boolean updatedAvailable = updatedItem.getAvailable();
		if (updatedAvailable != null) {
			item.setAvailable(updatedAvailable);
		}
		return item;
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
