package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.exceptions.AuthorizationErrorException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.InMemoryItemRepository;
import ru.practicum.shareit.user.repository.InMemoryUserRepository;

import java.util.List;

@Slf4j
@Service
public class ItemServiceImpl implements ItemService {

	private final InMemoryItemRepository itemRepository;
	private final InMemoryUserRepository userRepository;

	@Autowired
	public ItemServiceImpl(InMemoryItemRepository itemRepository, InMemoryUserRepository userRepository) {
		this.itemRepository = itemRepository;
		this.userRepository = userRepository;
	}

	@Override
	public ItemDto addItem(int userId, ItemDto itemDto) {
		Item item = ItemMapper.toItem(itemDto, userId);
		log.info("Add item {}", item);
		userRepository.getUserById(userId);
		item.setOwner(userId);
		return ItemMapper.toItemDto(itemRepository.addItem(item));
	}

	@Override
	public ItemDto updateItem(int itemId, int userId, ItemDto itemDto) {
		Item updatedItem = ItemMapper.toItem(itemDto, userId);
		log.info("Update item with id {} to {}", itemId, updatedItem);
		Item item = itemRepository.getItemById(itemId);
		updatedItem.setId(itemId);
		if (userId != item.getOwner()) {
			throw new AuthorizationErrorException("User " + userId + "is not the owner of the item");
		}

		String name = updatedItem.getName();

		if (name == null || name.isBlank()) {
			updatedItem.setName(item.getName());
		}

		String description = updatedItem.getDescription();
		if (description == null || description.isBlank()) {
			updatedItem.setDescription(item.getDescription());
		}

		Boolean available = updatedItem.getAvailable();
		if (available == null) {
			updatedItem.setAvailable(item.getAvailable());
		}

		return ItemMapper.toItemDto(itemRepository.updateItem(itemId, updatedItem));
	}

	@Override
	public ItemDto getItemById(int itemId) {
		log.info("Get item by Id {}", itemId);
		return ItemMapper.toItemDto(itemRepository.getItemById(itemId));
	}

	@Override
	public List<ItemDto> getOwnerItems(int userId) {
		log.info("Get user items. userId = {}", userId);
		return ItemMapper.toListItemDto(itemRepository.getOwnerItems(userId));
	}

	@Override
	public List<ItemDto> searchItemsByText(String text) {
		log.info("Search item by text {}", text);
		return ItemMapper.toListItemDto(itemRepository.searchItemsByText(text));
	}

	@Override
	public void clearItems() {
		itemRepository.clearItems();
	}

}
