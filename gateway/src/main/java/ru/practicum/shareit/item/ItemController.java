package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

import static ru.practicum.shareit.constants.Constant.USER_ID_HEADER;


/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@Controller
@RequestMapping("/items")
public class ItemController {

	private final ItemClient itemClient;

	@Autowired
	public ItemController(ItemClient itemService) {
		this.itemClient = itemService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Object> addItem(@RequestHeader(USER_ID_HEADER) @Positive int userId,
	                                      @Valid @RequestBody ItemDto itemDto) {
		log.info("Add item {} to user {}", itemDto, userId);
		return itemClient.addItem(userId, itemDto);
	}

	@PatchMapping("/{itemId}")
	public ResponseEntity<Object> updateItem(@PathVariable @Positive int itemId,
	                                         @RequestHeader(USER_ID_HEADER) @Positive int userId,
	                                         @RequestBody ItemDto itemDto) {
		log.info("Update item {} to user {}", itemDto, userId);
		return itemClient.updateItem(itemId, userId, itemDto);
	}

	@GetMapping("/{itemId}")
	public ResponseEntity<Object> getItemById(@PathVariable @Positive int itemId, @RequestHeader(USER_ID_HEADER) @Positive int userId) {
		log.info("Get item by Id {}", itemId);
		return itemClient.getItemById(itemId, userId);
	}

	@GetMapping
	public ResponseEntity<Object> getOwnerItems(@RequestHeader(USER_ID_HEADER) @Positive int userId) {
		log.info("Get user items. userId = {}", userId);
		return itemClient.getOwnerItems(userId);
	}

	@GetMapping("/search")
	public ResponseEntity<Object> searchItemsByText(@RequestParam("text") String text) {
		log.info("Search item by text {}", text);
		return itemClient.searchItemsByText(text);
	}

	@PostMapping("/{itemId}/comment")
	public ResponseEntity<Object> addComment(@PathVariable @Positive int itemId,
	                                         @RequestHeader(USER_ID_HEADER) @Positive int userId,
	                                         @Valid @RequestBody CommentDto commentDto) {
		log.info("Add comment to Item {} from user {}. Comment: {}", itemId, userId, commentDto);
		return itemClient.addComment(itemId, userId, commentDto);
	}

}
