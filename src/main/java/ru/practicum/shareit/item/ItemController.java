package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
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

	@Autowired
	public ItemController(ItemService itemService) {
		this.itemService = itemService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ItemDto addItem(@RequestHeader("X-Sharer-User-Id") int userId,
	                       @Valid @RequestBody ItemDto itemDto) {
		log.info("Add item {} to user {}", itemDto,userId);
		return itemService.addItem(userId, itemDto);
	}

	@PatchMapping("/{itemId}")
	public ItemDto updateItem(@PathVariable int itemId,
	                          @RequestHeader("X-Sharer-User-Id") int userId,
	                          @RequestBody ItemDto itemDto) {
		log.info("Update item {} to user {}", itemDto,userId);
		return itemService.updateItem(itemId, userId, itemDto);
	}

	@GetMapping("/{itemId}")
	public ItemDto getItemById(@PathVariable int itemId) {
		log.info("Get item by Id {}", itemId);
		return itemService.getItemById(itemId);
	}

	@GetMapping
	public List<ItemDto> getOwnerItems(@RequestHeader("X-Sharer-User-Id") int userId) {
		log.info("Get user items. userId = {}", userId);
		return itemService.getOwnerItems(userId);
	}

	@GetMapping("/search")
	public List<ItemDto> searchItemsByText(@RequestParam("text") String text) {
		log.info("Search item by text {}", text);
		return itemService.searchItemsByText(text);
	}

	@DeleteMapping
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void clearItems() {
		log.info("Delete all items");
		itemService.clearItems();
	}

}
