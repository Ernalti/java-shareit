package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {

	List<Item> findByOwner(User user);

	List<Item> findByDescriptionContainingIgnoreCaseAndAvailableIsTrueOrNameContainingIgnoreCaseAndAvailableIsTrue(String des, String nam);

	@Query("SELECT i FROM Item i " +
			"WHERE i.request IN :itemRequests ")
	List<Item> findbyItemRequests(List<ItemRequest> itemRequests);

	List<Item> findByRequest(ItemRequest itemRequest);
}
