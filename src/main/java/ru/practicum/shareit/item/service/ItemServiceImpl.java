package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.exceptions.AuthorizationErrorException;
import ru.practicum.shareit.exception.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ItemServiceImpl implements ItemService {

	private final ItemRepository itemRepository;
	private final UserRepository userRepository;

	@Autowired
	public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository) {
		this.itemRepository = itemRepository;
		this.userRepository = userRepository;
	}

	@Override
	public ItemDto addItem(int userId, ItemDto itemDto) {
		try {
		User user = userRepository.findById(userId).get();
		Item item = ItemMapper.toItem(itemDto, user);
		log.info("Add item {}", item);
		item.setOwner(user);

		return ItemMapper.toItemDto(itemRepository.save(item));
		} catch (EntityNotFoundException e) {
			throw new NotFoundException("User " + userId + " not found");
		}
	}

	@Override
	public ItemDto updateItem(int itemId, int userId, ItemDto itemDto) {
		User user = userRepository.findById(userId).get();
		Item updatedItem = ItemMapper.toItem(itemDto, user);
		log.info("Update item with id {} to {}", itemId, updatedItem);
		Item item = itemRepository.findById(itemId).get();
		updatedItem.setId(itemId);
		if (userId != item.getOwner().getId()) {
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

		return ItemMapper.toItemDto(itemRepository.save(updatedItem));
	}

	@Override
	public ItemDto getItemById(int itemId) {
		log.info("Get item by Id {}", itemId);
		return ItemMapper.toItemDto(itemRepository.findById(itemId).get());
	}

	@Override
	public List<ItemDto> getOwnerItems(int userId) {
		log.info("Get user items. userId = {}", userId);
		User user = userRepository.findById(userId).get();
		return ItemMapper.toListItemDto(itemRepository.findByOwner(user));
	}

	@Override
	public List<ItemDto> searchItemsByText(String text) {
		if (!text.isBlank()) {
			log.info("Search item by text {}", text);
			return ItemMapper.toListItemDto(itemRepository.findByDescriptionContainingIgnoreCaseAndAvailableIsTrueOrNameContainingIgnoreCaseAndAvailableIsTrue(text, text));
		} else {
			return new ArrayList<>();
		}
	}

	@Override
	public void clearItems() {
		itemRepository.deleteAll();
	}

}
