package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {


	List<Booking> findByBookerAndStartBeforeAndEndAfterOrderByStartDesc(User booker, LocalDateTime start, LocalDateTime end);

	List<Booking> findByBookerAndEndBeforeAndStatusOrderByStartDesc(User booker, LocalDateTime currentDate, BookingStatus status);

	List<Booking> findByBookerAndEndAfterOrderByStartDesc(User booker, LocalDateTime currentDate);

	List<Booking> findByBookerAndStatusOrderByStartDesc(User booker, BookingStatus bookingStatus);

	List<Booking> findByBookerOrderByStartDesc(User booker);

	List<Booking> findByItemOwnerAndStartBeforeAndEndAfterOrderByStartDesc(User booker, LocalDateTime start, LocalDateTime end);

	List<Booking> findByItemOwnerAndEndBeforeAndStatusOrderByStartDesc(User booker, LocalDateTime currentDate, BookingStatus bookingStatus);

	List<Booking> findByItemOwnerAndEndAfterOrderByStartDesc(User booker, LocalDateTime currentDate);

	List<Booking> findByItemOwnerAndStatusOrderByStartDesc(User booker, BookingStatus bookingStatus);

	List<Booking> findByItemOwnerOrderByStartDesc(User booker);

	Booking findFirstByItemAndStartIsBeforeAndStatusIsOrderByStartDesc(Item item, LocalDateTime currentDate, BookingStatus status);

	Booking findFirstByItemAndStartAfterAndStatusOrderByStartAsc(Item item, LocalDateTime currentDate, BookingStatus status);

	Integer findCountByItemAndBookerAndStatusAndEndBefore(Item item, User user, BookingStatus bookingStatus, LocalDateTime now);

	List<Booking> findByItemAndBookerAndStatusAndEndBefore(Item item, User user, BookingStatus bookingStatus, LocalDateTime now);

	Booking findFirstByItemAndStartIsBeforeAndStatusIsOrderByStartAsc(Item item, LocalDateTime time, BookingStatus bookingStatus);

	Booking findFirstByItemAndEndIsBeforeAndStatusIsOrderByStartDesc(Item item, LocalDateTime time, BookingStatus bookingStatus);

	Booking findFirstByItemAndEndIsBeforeAndStatusIsOrderByStartAsc(Item item, LocalDateTime time, BookingStatus bookingStatus);

	Booking findFirstByItemAndStartIsAfterAndStatusIsOrderByStartDesc(Item item, LocalDateTime time, BookingStatus bookingStatus);

	Booking findFirstByItemAndStartIsAfterAndStatusIsOrderByStartAsc(Item item, LocalDateTime time, BookingStatus bookingStatus);

	Booking findFirstByItemAndEndIsAfterAndStatusIsOrderByStartDesc(Item item, LocalDateTime time, BookingStatus bookingStatus);

	Booking findFirstByItemAndEndIsAfterAndStatusIsOrderByStartAsc(Item item, LocalDateTime time, BookingStatus bookingStatus);

	Booking findFirstByItemAndStartBeforeAndStatusOrderByStartAsc(Item item, LocalDateTime time, BookingStatus bookingStatus);

	Booking findFirstByItemAndStartBeforeAndStatusOrderByStartDesc(Item item, LocalDateTime time, BookingStatus bookingStatus);

	Booking findFirstByItemAndEndBeforeAndStatusOrderByStartDesc(Item item, LocalDateTime time, BookingStatus bookingStatus);

	Booking findFirstByItemAndEndBeforeAndStatusOrderByStartAsc(Item item, LocalDateTime time, BookingStatus bookingStatus);

	Booking findFirstByItemAndStartAfterAndStatusOrderByStartDesc(Item item, LocalDateTime time, BookingStatus bookingStatus);

	Booking findFirstByItemAndEndAfterAndStatusOrderByStartDesc(Item item, LocalDateTime time, BookingStatus bookingStatus);

	Booking findFirstByItemAndEndAfterAndStatusOrderByStartAsc(Item item, LocalDateTime time, BookingStatus bookingStatus);
}
