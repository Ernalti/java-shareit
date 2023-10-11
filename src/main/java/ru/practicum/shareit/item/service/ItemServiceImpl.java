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
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

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
	public ItemResponseDto addItem(Integer userId, ItemDto itemDto) {

//		return null;
			User user = userRepository.findById(userId).orElseThrow();
			Item item = ItemMapper.toItem(itemDto, user);
			log.info("Add item {}", item);
			item.setOwner(user);
			Item res = itemRepository.save(item);
//			return ItemMapper.toItemDto(itemRepository.save(item));
		return ItemMapper.toItemDto(item);
	}

	@Override
	public ItemResponseDto updateItem(Integer itemId, Integer userId, ItemDto itemDto) {
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

		return getItemDtoWithBookings(itemRepository.save(updatedItem));
	}

	@Override
	public ItemResponseDto getItemById(Integer itemId) {
		log.info("Get item by Id {}", itemId);
		Item item = itemRepository.findById(itemId).orElseThrow();
		return getItemDtoWithBookings(item);
	}

	@Override
	public List<ItemResponseDto> getOwnerItems(Integer userId) {
		log.info("Get user items. userId = {}", userId);
		User user = userRepository.findById(userId).orElseThrow();
		List<Item> itemList = itemRepository.findByOwner(user);
		return itemList.stream()
				.map(this::getItemDtoWithBookings)
				.collect(Collectors.toList());
	}

	@Override
	public List<ItemResponseDto> searchItemsByText(String text) {
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
//		commentRepository.findCountByIte

		Integer userBookingItem = bookingRepository.findByItemAndBookerAndStatusAndEndBefore(item, user, BookingStatus.APPROVED, LocalDateTime.now()).size();
		if (userBookingItem == null) {
			log.info("NULL");
			throw new CommentException("User " + userId + " didn't book item " + itemId);
		}
		if (userBookingItem == 0) {
			log.info("0");
			throw new CommentException("User " + userId + " didn't book item " + itemId);
		}
		log.info(userBookingItem.toString());
		Comment comment = CommentMapper.toComment(commentDto, item, user);
		CommentDto res = CommentMapper.toCommentDto(commentRepository.save(comment));

		return res;
	}

	private ItemResponseDto getItemDtoWithBookings(Item item) {
		ItemResponseDto itemDto = ItemMapper.toItemDto(item);
		LocalDateTime time = LocalDateTime.now();
		BookingDto lastBooking = BookingMapper.toBookingDto(bookingRepository.findFirstByItemAndStartBeforeAndStatusOrderByStartDesc(item, time, BookingStatus.APPROVED));
//		Booking boo;
//
//
//		boo = (bookingRepository.findFirstByItemAndStartBeforeAndStatusOrderByStartDesc(item, time, BookingStatus.APPROVED));
//		if (boo!=null) {log.info("1 "+boo.getId().toString());}
//		boo = (bookingRepository.findFirstByItemAndStartBeforeAndStatusOrderByStartAsc(item, time, BookingStatus.APPROVED));
//		if (boo!=null) {log.info("2 "+boo.getId().toString());}
//		boo = (bookingRepository.findFirstByItemAndEndBeforeAndStatusOrderByStartDesc(item, time, BookingStatus.APPROVED));
//		if (boo!=null) {log.info("3 "+boo.getId().toString());}
//		boo = (bookingRepository.findFirstByItemAndEndBeforeAndStatusOrderByStartAsc(item, time, BookingStatus.APPROVED));
//		if (boo!=null) {log.info("4 "+boo.getId().toString());}
//
//		boo = (bookingRepository.findFirstByItemAndStartAfterAndStatusOrderByStartDesc(item, time, BookingStatus.APPROVED));
//		if (boo!=null) {log.info("5 "+boo.getId().toString());}
//		boo = (bookingRepository.findFirstByItemAndStartAfterAndStatusOrderByStartAsc(item, time, BookingStatus.APPROVED));
//		if (boo!=null) {log.info("6 "+boo.getId().toString());}
//		boo = (bookingRepository.findFirstByItemAndEndAfterAndStatusOrderByStartDesc(item, time, BookingStatus.APPROVED));
//		if (boo!=null) {log.info("7 "+boo.getId().toString());}
//		boo = (bookingRepository.findFirstByItemAndEndAfterAndStatusOrderByStartAsc(item, time, BookingStatus.APPROVED));
//		if (boo!=null) {log.info("8 "+boo.getId().toString());}
		BookingDto nextBooking = BookingMapper.toBookingDto(bookingRepository.findFirstByItemAndStartAfterAndStatusOrderByStartAsc(item, time, BookingStatus.APPROVED));
		cropBookingDto(lastBooking);
		cropBookingDto(nextBooking);
		List<CommentDto> comments = CommentMapper.toListCommentDto(commentRepository.findByItem(item));
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
