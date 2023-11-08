package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import static ru.practicum.shareit.constants.Constant.USER_ID_HEADER;

/**
 * TODO Sprint add-item-requests.
 */

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {

	private final ItemRequestClient itemRequestClient;

	@PostMapping
	public ResponseEntity<Object> addItemRequest(@RequestHeader(USER_ID_HEADER) @Positive int userId,
	                                             @Valid @RequestBody ItemRequestDto itemRequestDto) {
		log.info("Add item requset {} from user {}", itemRequestDto, userId);
		return itemRequestClient.addItemRequest(userId, itemRequestDto);
	}

	@GetMapping
	public ResponseEntity<Object> getOwnerItemRequests(@RequestHeader(USER_ID_HEADER) @Positive int userId) {
		log.info("Find owner requsets from user {}", userId);
		return itemRequestClient.getOwnerItemRequests(userId);
	}

	@GetMapping("/all")
	public ResponseEntity<Object> getAllItemRequests(@RequestHeader(USER_ID_HEADER) @Positive int userId,
	                                               @RequestParam(value = "from", defaultValue = "0")  @PositiveOrZero int from,
	                                               @RequestParam(value = "size", defaultValue = "10") @Positive int size) {
		log.info("Find all requsets from {} size {}", from, size);
		return itemRequestClient.getAllItemRequests(userId, from, size);
	}

	@GetMapping("/{requestId}")
	public ResponseEntity<Object> getItemRequestById(@RequestHeader(USER_ID_HEADER) @Positive int userId,
	                                               @PathVariable @Positive int requestId) {
		log.info("Get requsets id {}", requestId);
		return itemRequestClient.getItemRequestById(userId, requestId);
	}

}
