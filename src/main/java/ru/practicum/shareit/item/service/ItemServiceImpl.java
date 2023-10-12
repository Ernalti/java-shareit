package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.mapper.BookingMapper;
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
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ItemServiceImpl implements ItemService {

	private final ItemRepository itemRepository;
	private final UserRepository userRepository;
	private final BookingRepository bookingRepository;
	private final CommentRepository commentRepository;

	@Autowired
	public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository, BookingRepository bookingRepository, CommentRepository commentRepository) {
		this.itemRepository = itemRepository;
		this.userRepository = userRepository;
		this.bookingRepository = bookingRepository;
		this.commentRepository = commentRepository;
	}

	@Override
	public ItemDto addItem(Integer userId, ItemDto itemDto) {
		try {
			User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User " + userId + " not found"));
			Item item = ItemMapper.toItem(itemDto, user);
			user.getId();
			log.info("Add item {}", item);
			item.setOwner(user);
			return ItemMapper.toItemDto(itemRepository.save(item));
		} catch (EntityNotFoundException e) {
			throw new NotFoundException("User " + userId + " not found");
		}
	}

	@Override
	public ItemDto updateItem(Integer itemId, Integer userId, ItemDto itemDto) {
		try {
			User user = userRepository.findById(userId).orElseThrow();
			Item updatedItem = ItemMapper.toItem(itemDto, user);
			log.info("Update item with id {} to {}", itemId, updatedItem);
			Item item = itemRepository.findById(itemId).orElseThrow();
			updatedItem.setId(itemId);
			if (!userId.equals(item.getOwner().getId())) {
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
		} catch (Exception e) {
			throw new NotFoundException(e.getMessage());
		}
	}

	@Override
	public ItemDto getItemById(Integer itemId, Integer userId) {
		log.info("Get item by Id {}", itemId);
		Item item = itemRepository.findById(itemId).orElseThrow();
		return getItemDtoWithBookings(item, userId);
	}

	@Override
	public List<ItemDto> getOwnerItems(Integer userId) {
		log.info("Get user items. userId = {}", userId);
		User user = userRepository.findById(userId).orElseThrow();
		List<Item> itemList = itemRepository.findByOwner(user);
		return itemList.stream()
				.map(item -> getItemDtoWithBookings(item, userId))
				.collect(Collectors.toList());
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

	@Override
	public CommentDto addComment(Integer itemId, Integer userId, CommentDto commentDto) {
		Item item = itemRepository.findById(itemId).orElseThrow();
		User user = userRepository.findById(userId).orElseThrow();
		Integer userBookingItem = bookingRepository.findByItemAndBookerAndStatusAndEndBefore(item, user, BookingStatus.APPROVED, LocalDateTime.now()).size();

		if (userBookingItem == 0) {
			throw new CommentException("User " + userId + " didn't book item " + itemId);
		}
		Comment comment = CommentMapper.toComment(commentDto, item, user);
		CommentDto res = CommentMapper.toCommentDto(commentRepository.save(comment));

		return res;
	}

	private ItemDto getItemDtoWithBookings(Item item, Integer userId) {
		ItemDto itemDto = ItemMapper.toItemDto(item);
		LocalDateTime time = LocalDateTime.now();
		BookingDto lastBooking = null;
		BookingDto nextBooking = null;
		if (userId.equals(item.getOwner().getId())) {
			lastBooking = BookingMapper.toBookingDto(bookingRepository.findFirstByItemAndStartBeforeAndStatusOrderByStartDesc(item, time, BookingStatus.APPROVED));
			nextBooking = BookingMapper.toBookingDto(bookingRepository.findFirstByItemAndStartAfterAndStatusOrderByStartAsc(item, time, BookingStatus.APPROVED));
			cropBookingDto(lastBooking);
			cropBookingDto(nextBooking);
		}
		List<CommentDto> comments = CommentMapper.toListCommentDto(commentRepository.findByItemId(item.getId()));
		itemDto.setLastBooking(lastBooking);
		itemDto.setNextBooking(nextBooking);
		itemDto.setComments(comments);
		return itemDto;
	}

	private void cropBookingDto(BookingDto bookingDto) {
		if (bookingDto != null) {
			bookingDto.setBookerId(bookingDto.getBooker().getId());
			bookingDto.setItem(null);
			bookingDto.setBooker(null);
		}
	}

}
