package ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */

@Slf4j
@RestController
@RequestMapping(path = "/requests")
@Validated
public class ItemRequestController {

	private final ItemRequestService itemRequestService;

	@Autowired
	public ItemRequestController(ItemRequestService itemRequestService) {
		this.itemRequestService = itemRequestService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ItemRequestDto addItemRequest(@RequestHeader("X-Sharer-User-Id") int userId,
	                                     @Valid @RequestBody ItemRequestDto itemRequestDto) {
		log.info("Add item requset {} from user {}", itemRequestDto, userId);
		return itemRequestService.addItemRequest(userId, itemRequestDto);
	}

	@GetMapping
	public List<ItemRequestDto> getOwnerItemRequests(@RequestHeader("X-Sharer-User-Id") int userId) {
		log.info("Find owner requsets from user {}", userId);
		return itemRequestService.getOwnerItemRequests(userId);
	}

	@GetMapping("/all")
	public List<ItemRequestDto> getAllItemRequests(@RequestHeader("X-Sharer-User-Id") int userId,
	                                               @RequestParam(value = "from", defaultValue = "0")  @PositiveOrZero int from,
	                                               @RequestParam(value = "size", defaultValue = "10") @Positive int size) {
		log.info("Find all requsets from {} size {}", from, size);
		return itemRequestService.getAllItemRequests(userId, from, size);
	}

	@GetMapping("/{requestId}")
	public ItemRequestDto getItemRequestById(@RequestHeader("X-Sharer-User-Id") int userId,
	                                               @PathVariable int requestId) {
		log.info("Get requsets id {}", requestId);
		return itemRequestService.getItemRequestById(userId, requestId);
	}

}
