package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {


	List<Item> findByOwner(User user);

//	@Query("SELECT i FROM Item i WHERE i.name LIKE %:text% OR i.description LIKE %:text%")

	List<Item> findByDescriptionContainingIgnoreCaseAndAvailableIsTrueOrNameContainingIgnoreCaseAndAvailableIsTrue(String des, String nam);

}
