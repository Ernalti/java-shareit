package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.exceptions.AuthorizationErrorException;
import ru.practicum.shareit.exception.exceptions.CommentException;
import ru.practicum.shareit.exception.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {

	private final ItemRepository itemRepository;
	private final UserRepository userRepository;
	private final BookingRepository bookingRepository;
	private final CommentRepository commentRepository;
	private final ItemRequestRepository itemRequestRepository;

	@Autowired
	public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository, BookingRepository bookingRepository, CommentRepository commentRepository, ItemRequestRepository itemRequestRepository) {
		this.itemRepository = itemRepository;
		this.userRepository = userRepository;
		this.bookingRepository = bookingRepository;
		this.commentRepository = commentRepository;
		this.itemRequestRepository = itemRequestRepository;
	}

	@Override
	@Transactional
	public ItemDto addItem(int userId, ItemDto itemDto) {
		User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User " + userId + " not found"));
		ItemRequest itemRequest = null;
		if (itemDto.getRequestId() != null) {
			 itemRequest = itemRequestRepository.findById(itemDto.getRequestId())
					.orElseThrow(() -> new NotFoundException("Request " + itemDto.getRequestId() + " not found"));

		}		Item item = ItemMapper.toItem(itemDto, user, itemRequest);
		log.info("Add item {}", item);
		item.setOwner(user);
		return ItemMapper.toItemDto(itemRepository.save(item));
	}

	@Override
	@Transactional
	public ItemDto updateItem(int itemId, int userId, ItemDto itemDto) {
		User user = userRepository.findById(userId).orElseThrow();
		Item item = itemRepository.findById(itemId).orElseThrow();
		Item updatedItem = ItemMapper.toItem(itemDto, user, item.getRequest());
		log.info("Update item with id {} to {}", itemId, updatedItem);

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
		return getItemDtoWithBookings(itemRepository.save(updatedItem), userId);
	}

	@Override
	public ItemDto getItemById(int itemId, int userId) {
		log.info("Get item by Id {}", itemId);
		Item item = itemRepository.findById(itemId).orElseThrow();
		return getItemDtoWithBookings(item, userId);
	}

	@Override
	public List<ItemDto> getOwnerItems(int userId) {
		log.info("Get user items. userId = {}", userId);
		User user = userRepository.findById(userId).orElseThrow();
		List<Item> itemList = itemRepository.findByOwner(user);
		return getItemListDtoWithBookings(itemList, userId);
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
	@Transactional
	public void clearItems() {
		itemRepository.deleteAll();
	}

	@Override
	@Transactional
	public CommentDto addComment(int itemId, int userId, CommentDto commentDto) {
		Item item = itemRepository.findById(itemId).orElseThrow();
		User user = userRepository.findById(userId).orElseThrow();
		int userBookingItem = bookingRepository.findByItemAndBookerAndStatusAndEndBefore(item, user, BookingStatus.APPROVED, LocalDateTime.now()).size();

		if (userBookingItem == 0) {
			throw new CommentException("User " + userId + " didn't book item " + itemId);
		}

		Comment comment = CommentMapper.toComment(commentDto, item, user);
		log.info("Add comment to Item {} from user {}. Comment: {}", itemId, userId, comment);
		return CommentMapper.toCommentDto(commentRepository.save(comment));
	}

	private ItemDto getItemDtoWithBookings(Item item, int userId) {
		ItemDto itemDto = ItemMapper.toItemDto(item);
		LocalDateTime time = LocalDateTime.now();
		BookingDto lastBooking = null;
		BookingDto nextBooking = null;
		Sort asc = Sort.by("start").ascending();
		Sort desc = Sort.by("start").descending();

		if (userId == item.getOwner().getId()) {
			lastBooking = BookingMapper.toBookingDto(bookingRepository.findFirstByItemAndStartBeforeAndStatus(item, time, BookingStatus.APPROVED, desc).orElse(null));
			nextBooking = BookingMapper.toBookingDto(bookingRepository.findFirstByItemAndStartAfterAndStatus(item, time, BookingStatus.APPROVED, asc).orElse(null));
			cropBookingDto(lastBooking);
			cropBookingDto(nextBooking);
		}
		List<CommentDto> comments = CommentMapper.toListCommentDto(commentRepository.findByItemId(item.getId()));
		itemDto.setLastBooking(lastBooking);
		itemDto.setNextBooking(nextBooking);
		itemDto.setComments(comments);
		return itemDto;
	}

	private List<ItemDto> getItemListDtoWithBookings(List<Item> itemList, int userId) {
		List<Item> findItemList = itemList.stream()
				.filter(item -> item.getOwner().getId() == userId)
				.collect(Collectors.toList());
		Sort desc = Sort.by("start").descending();
		Sort asc = Sort.by("start").ascending();
		List<Booking> lastBookings = bookingRepository.findlastBookings(findItemList,LocalDateTime.now(),BookingStatus.APPROVED,desc);
		List<Booking> nextBookings = bookingRepository.findNextBookings(findItemList,LocalDateTime.now(),BookingStatus.APPROVED,asc);
		List<ItemDto> itemDtoList = new ArrayList<>();
		List<Comment> comments = commentRepository.findAllByItems(itemList);

		for (Item item : itemList) {
			ItemDto itemDto = ItemMapper.toItemDto(item);

			Booking lastBooking = lastBookings.stream()
					.filter(booking -> booking.getItem().getId() == item.getId())
					.findFirst()
					.orElse(null);

			itemDto.setLastBooking(cropBookingDto(BookingMapper.toBookingDto(lastBooking)));

			Booking nextBooking = nextBookings.stream()
					.filter(booking -> booking.getItem().getId() == item.getId())
					.findFirst()
					.orElse(null);
			itemDto.setNextBooking(cropBookingDto(BookingMapper.toBookingDto(nextBooking)));

			List<Comment> itemComments = comments.stream()
					.filter(comment -> comment.getItem().getId() == item.getId())
					.collect(Collectors.toList());
			itemDto.setComments(CommentMapper.toListCommentDto(itemComments));
			itemDtoList.add(itemDto);
		}
		return itemDtoList;
	}

	private BookingDto cropBookingDto(BookingDto bookingDto) {
		if (bookingDto != null) {
			bookingDto.setBookerId(bookingDto.getBooker().getId());
			bookingDto.setItem(null);
			bookingDto.setBooker(null);
		}
		return bookingDto;
	}

}
