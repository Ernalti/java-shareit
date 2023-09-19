package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;


/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequestMapping("/items")
public class ItemController {

	private final ItemService itemService;
	private final ItemMapper itemMapper;

	@Autowired
	public ItemController(ItemService itemService, ItemMapper itemMapper) {
		this.itemService = itemService;

		this.itemMapper = itemMapper;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ItemDto addItem(@RequestHeader("X-Sharer-User-Id") int userId,
	                       @Valid @RequestBody ItemDto itemDto) {
		log.info("Add item {} to user {}", itemDto,userId);
		Item item = itemMapper.toItem(itemDto);
		Item res = itemService.addItem(userId, item);
		return itemMapper.toItemDto(res);
	}

	@PatchMapping("/{itemId}")
	public ItemDto updateItem(@PathVariable int itemId,
	                          @RequestHeader("X-Sharer-User-Id") int userId,
	                          @RequestBody ItemDto itemDto) {
		log.info("Update item {} to user {}", itemDto,userId);
		Item editedItem = itemService.updateItem(itemId, userId, itemMapper.toItem(itemDto));
		return itemMapper.toItemDto(editedItem);
	}

	@GetMapping("/{itemId}")
	public ItemDto getItemById(@PathVariable int itemId) {
		log.info("Get item by Id {}", itemId);
		Item res = itemService.getItemById(itemId);
		return itemMapper.toItemDto(res);
	}

	@GetMapping
	public List<ItemDto> getOwnerItems(@RequestHeader("X-Sharer-User-Id") int userId) {
		log.info("Get user items. userId = {}", userId);
		List<Item> res = itemService.getOwnerItems(userId);
		return itemMapper.toListItemDto(res);
	}

	@GetMapping("/search")
	public List<ItemDto> searchItemsByText(@RequestParam("text") String text) {
		log.info("Search item by text {}", text);
		List<Item> res = itemService.searchItemsByText(text);
		return itemMapper.toListItemDto(res);
	}

	@DeleteMapping
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void clearItems() {
		log.info("Delete all items");
		itemService.clearItems();
	}

}
