package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.Map;

@Component
public class ItemStorageInMemoryImpl implements ItemStorage {

	private final Map<Integer, Item> items = new HashMap<>();

	private int nextItemId;

	public ItemStorageInMemoryImpl(d) {
		this.nextItemId = 1;
	}


}
