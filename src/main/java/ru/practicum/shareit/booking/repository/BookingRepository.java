package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Integer> {


	List<Booking> findByItemAndBookerAndStatusAndEndBefore(Item item, User user, BookingStatus bookingStatus, LocalDateTime now);

	List<Booking> findByItemOwnerAndStartBeforeAndEndAfter(User user, LocalDateTime time, LocalDateTime time1, Sort sort);

	List<Booking> findByItemOwnerAndEndBeforeAndStatus(User user, LocalDateTime time, BookingStatus bookingStatus, Sort sort);

	List<Booking> findByItemOwnerAndEndAfter(User user, LocalDateTime time, Sort sort);

	List<Booking> findByItemOwnerAndStatus(User user, BookingStatus bookingStatus, Sort sort);

	List<Booking> findByBooker(User user, Sort sort);

	List<Booking> findByBookerAndStartBeforeAndEndAfter(User user, LocalDateTime time, LocalDateTime time1, Sort sort);

	List<Booking> findByBookerAndEndBeforeAndStatus(User user, LocalDateTime time, BookingStatus bookingStatus, Sort sort);

	List<Booking> findByBookerAndEndAfter(User user, LocalDateTime time, Sort sort);

	List<Booking> findByBookerAndStatus(User user, BookingStatus bookingStatus, Sort sort);

	List<Booking> findByItemOwner(User user, Sort sort);

	Optional<Booking> findFirstByItemAndStartBeforeAndStatus(Item item, LocalDateTime time, BookingStatus bookingStatus, Sort desc);

	Optional<Booking> findFirstByItemAndStartAfterAndStatus(Item item, LocalDateTime time, BookingStatus bookingStatus, Sort asc);


	@Query("SELECT b FROM Booking b " +
			"JOIN b.item i " +
			"WHERE i IN :items " +
			"AND b.start < :time " +
			"AND b.status = :bookingStatus " +
			"GROUP BY i.id, b.start ")
		// Указание сортировки по убыванию
	List<Booking> findlastBookings(List<Item> items, LocalDateTime time, BookingStatus bookingStatus, Sort sort);

	@Query("SELECT b FROM Booking b " +
			"JOIN b.item i " +
			"WHERE i IN :items " +
			"AND b.start > :time " +
			"AND b.status = :bookingStatus " +
			"GROUP BY i.id, b.start ")
		// Указание сортировки по убыванию
	List<Booking> findNextBookings(List<Item> items, LocalDateTime time, BookingStatus bookingStatus, Sort sort);
}
