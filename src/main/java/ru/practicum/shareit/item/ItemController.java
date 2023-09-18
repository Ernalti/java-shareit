package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import static ru.practicum.shareit.item.mapper.ItemMapper.*;

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

	@Autowired
	public ItemController(ItemService itemService) {
		this.itemService = itemService;

	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ItemDto addItem(@RequestHeader("X-Sharer-User-Id") int userId,
	                       @Valid @RequestBody ItemDto itemDto) {
		Item item = toItem(itemDto);
		Item res =	itemService.addItem(userId, item);
		return toItemDto(res);
	}

	@PatchMapping("/{itemId}")
	public ItemDto editItem(@PathVariable int itemId,
	                        @RequestHeader("X-Sharer-User-Id") int userId,
	                        @Valid @RequestBody ItemDto itemDto) {
		Item editedItem = itemService.editItem(itemId, userId, toItem(itemDto));
		return toItemDto(editedItem);
	}

	@GetMapping("/{itemId}")
	public ItemDto getItemById(@PathVariable int itemId) {
		Item res = itemService.getItemById(itemId);
		return toItemDto(res);
	}

	@GetMapping
	public List<ItemDto> getOwnerItems(@RequestHeader("X-Sharer-User-Id") int userId) {
		List<Item> res = itemService.getOwnerItems(userId);
		return toListItemDto(res);
	}

	@GetMapping("/search")
	public List<ItemDto> searchItemsByText(@RequestParam("text") String text) {
		List<Item> res = itemService.searchItemsByText(text);
		return toDtoList(res);
	}

}
