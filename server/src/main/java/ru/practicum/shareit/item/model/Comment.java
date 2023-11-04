package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;


	private String text;

	@ManyToOne
	@JoinColumn(name = "item")
	private Item item;

	@ManyToOne
	@JoinColumn(name = "author")
	private User author;

	private LocalDateTime created;
}
