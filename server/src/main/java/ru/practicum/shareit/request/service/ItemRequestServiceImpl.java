package ru.practicum.shareit.request.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {

	private final ItemRequestRepository itemRequestRepository;
	private final UserRepository userRepository;
	private final ItemRepository itemRepository;

	@Autowired
	public ItemRequestServiceImpl(ItemRequestRepository itemRequestRepository, UserRepository userRepository, ItemRepository itemRepository) {
		this.itemRequestRepository = itemRequestRepository;
		this.userRepository = userRepository;
		this.itemRepository = itemRepository;
	}

	@Override
	@Transactional
	public ItemRequestDto addItemRequest(int userId, ItemRequestDto itemRequestDto) {
		User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User " + userId + " not found"));
		ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto, user);
		log.info("Add item request {} from user {}",itemRequest,user);
		return ItemRequestMapper.toItemRequestDTO(itemRequestRepository.save(itemRequest));
	}

	@Override
	public List<ItemRequestDto> getOwnerItemRequests(int userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User " + userId + " not found"));
		List<ItemRequest> itemRequests = itemRequestRepository
				.findAllByRequestor(user);
		return itemRequestsDtoWithItems(itemRequests);
	}

	@Override
	public List<ItemRequestDto> getAllItemRequests(int userId, int from, int size) {
		User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User " + userId + " not found"));
		Pageable page = PageRequest.of(from / size, size);
		List<ItemRequest> itemRequests = itemRequestRepository

				.findByRequestorNot(user, page).toList();

		return itemRequestsDtoWithItems(itemRequests);
	}

	@Override
	public ItemRequestDto getItemRequestById(int userId, int requestId) {
		userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User " + userId + " not found"));
		log.info("Get item request by id {}",requestId);
		ItemRequest itemRequest = itemRequestRepository.findById(requestId).orElseThrow();
		return itemRequestDtoWithItems(itemRequest);
	}

	private ItemRequestDto itemRequestDtoWithItems(ItemRequest itemRequest) {
		List<Item> items = itemRepository.findByRequest(itemRequest);
		List<Item> itemsByRequest = items.stream()
				.filter(item -> item.getRequest().getId() == itemRequest.getId())
				.collect(Collectors.toList());
		List<ItemDto> itemsDtoByRequest = ItemMapper.toListItemDto(itemsByRequest);
		if (itemsDtoByRequest == null) {
			itemsDtoByRequest = new ArrayList<>();
		}
		ItemRequestDto itemRequestDto = ItemRequestMapper.toItemRequestDTO(itemRequest);
		itemRequestDto.setItems(itemsDtoByRequest);
		return itemRequestDto;
	}

	private List<ItemRequestDto> itemRequestsDtoWithItems(List<ItemRequest> itemRequests) {
		List<ItemRequestDto>  itemRequestsDto = ItemRequestMapper.toListItemRequestDto(itemRequests);
		List<Item> items = itemRepository.findbyItemRequests(itemRequests);
		List<ItemDto> itemsDto = ItemMapper.toListItemDto(items);
		for (ItemRequestDto itemRequestDto :itemRequestsDto) {
			List<ItemDto> itemsByRequest = itemsDto.stream()
					.filter(item -> item.getRequestId() == itemRequestDto.getId())
					.collect(Collectors.toList());
			if (itemsByRequest == null) {
				itemsByRequest = new ArrayList<>();
			}
			itemRequestDto.setItems(itemsByRequest);
		}
		return itemRequestsDto;
	}

}