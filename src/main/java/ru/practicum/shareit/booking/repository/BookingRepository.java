package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {


	List<Booking> findByBookerAndStartAfterAndEndBeforeOrderByStartDesc(User booker, LocalDateTime start, LocalDateTime end);

	List<Booking> findByBookerAndStartBeforeOrderByStartDesc(User booker, LocalDateTime currentDate);

	List<Booking> findByBookerAndEndAfterOrderByStartDesc(User booker, LocalDateTime currentDate);

	List<Booking> findByBookerAndStatusOrderByStartDesc(User booker, BookingStatus bookingStatus);

	List<Booking> findByBookerOrderByStartDesc(User booker);

	List<Booking> findByItemOwnerAndStartAfterAndEndBeforeOrderByStartDesc(User booker, LocalDateTime start, LocalDateTime end);

	List<Booking> findByItemOwnerAndStartBeforeOrderByStartDesc(User booker, LocalDateTime currentDate);

	List<Booking> findByItemOwnerAndEndAfterOrderByStartDesc(User booker, LocalDateTime currentDate);

	List<Booking> findByItemOwnerAndStatusOrderByStartDesc(User booker, BookingStatus bookingStatus);

	List<Booking> findByItemOwnerOrderByStartDesc(User booker);


}
