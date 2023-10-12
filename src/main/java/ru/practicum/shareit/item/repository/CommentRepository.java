package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
	List<Comment> findByItemId(int id);

	@Query("SELECT c FROM Comment c WHERE c.item IN :items")
	List<Comment> findAllByItems(List<Item> items);

}
