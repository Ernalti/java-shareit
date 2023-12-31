package ru.practicum.shareit.request.model;

import lombok.*;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Entity
@Table(name = "requests")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequest {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;


	private String description;

	@ManyToOne
	@JoinColumn(name = "requestor")
	private User requestor;

	private LocalDateTime created;
}
