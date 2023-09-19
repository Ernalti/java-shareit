package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class InMemoryItemStorage implements ItemStorage {

	private final Map<Integer, Item> items = new HashMap<>();

	private int id;

	public InMemoryItemStorage() {
		id = 1;
	}


	@Override
	public Item addItem(Item item) {
		item.setId(nextId());
		items.put(item.getId(), item);
		return item;
	}

	@Override
	public Item updateItem(int itemId, Item updatedItem) {
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
		return items.get(id);
	}

	@Override
	public List<Item> getOwnerItems(int ownerId) {
		return items.values().stream()
				.filter(item -> item.getOwner() == ownerId)
				.collect(Collectors.toList());
	}

	@Override
	public List<Item> searchItemsByText(String text) {
		String lowerText = text.toLowerCase();
		return (text != null && !text.isBlank()) ?
				items.values()
						.stream().filter(item -> item.getAvailable() == true &&
								(item.getName().toLowerCase().contains(lowerText) ||
										item.getDescription().toLowerCase().contains(lowerText)))
						.collect(Collectors.toList())
				: new ArrayList<>();
	}

	private int nextId() {
		return id++;
	}
}
