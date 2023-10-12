package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Entity
@Table(name = "booking")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "start_booking", nullable = false)
	private LocalDateTime start;

	@Column(name = "end_booking", nullable = false)
	private LocalDateTime end;

	@ManyToOne
	@JoinColumn(name = "item")
	private Item item;

	@ManyToOne
	@JoinColumn(name = "booker")
	private User booker;
	private BookingStatus status;

}
