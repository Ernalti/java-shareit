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
		Item item = itemMapper.toItem(itemDto);
		Item res = itemService.addItem(userId, item);
		return itemMapper.toItemDto(res);
	}

	@PatchMapping("/{itemId}")
	public ItemDto updateItem(@PathVariable int itemId,
	                          @RequestHeader("X-Sharer-User-Id") int userId,
	                          @RequestBody ItemDto itemDto) {
		Item editedItem = itemService.updateItem(itemId, userId, itemMapper.toItem(itemDto));
		return itemMapper.toItemDto(editedItem);
	}

	@GetMapping("/{itemId}")
	public ItemDto getItemById(@PathVariable int itemId) {
		Item res = itemService.getItemById(itemId);
		return itemMapper.toItemDto(res);
	}

	@GetMapping
	public List<ItemDto> getOwnerItems(@RequestHeader("X-Sharer-User-Id") int userId) {
		List<Item> res = itemService.getOwnerItems(userId);
		return itemMapper.toListItemDto(res);
	}

	@GetMapping("/search")
	public List<ItemDto> searchItemsByText(@RequestParam("text") String text) {
		List<Item> res = itemService.searchItemsByText(text);
		return itemMapper.toListItemDto(res);
	}

}
