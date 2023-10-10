package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exception.exceptions.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class InMemoryItemRepositoryTest {

	private final ItemRepository itemRepository;
	private Item item1;
	private Item item2;

	private final UserRepository userRepository;
	User user1;
	User user2;


	@BeforeEach
	public void beforeEach() {
		itemRepository.clearItems();
		item1 = new Item(null, "item1", "Description 1", true,  null);
		item2 = new Item(null, "item2", "Description 2", true,  null);
		userRepository.clearUsers();
		user1 = new User(null, "user1", "user1@user1.ru");
		user2 = new User(null, "user2", "user2@user2.ru");
	}

	@Test
	public void shouldAddAndGetItem() {
		User user = userRepository.createUser(user1);
		item1.setOwner(user.getId());
		Item item = itemRepository.addItem(item1);
		item1.setId(item.getId());
		assertEquals(itemRepository.getItemById(item1.getId()), item1);
	}

	@Test
	public void shouldUpdateItem() {
		User user = userRepository.createUser(user1);
		item1.setOwner(user.getId());
		Item item = itemRepository.addItem(item1);
		item1.setId(item.getId());
		itemRepository.updateItem(item.getId(), item2);
	}

	@Test
	public void shouldNotGetItemWithWrongId() {
		assertThrows(NotFoundException.class, () ->
				itemRepository.getItemById(9876));
	}

	@Test
	public void shouldGetOwnerItems() {
		User user = userRepository.createUser(user1);
		item1.setOwner(user.getId());
		itemRepository.addItem(item1);
		item2.setOwner(user.getId());
		itemRepository.addItem(item2);
		List<Item> newItems = new ArrayList<>();
		newItems.add(item1);
		newItems.add(item2);
		assertArrayEquals(itemRepository.findByOwner(user.getId()).toArray(), newItems.toArray());
	}

	@Test
	public void shouldSearchItemsByText() {
		User user = userRepository.createUser(user1);
		item1.setOwner(user.getId());
		itemRepository.addItem(item1);
		item2.setOwner(user.getId());
		itemRepository.addItem(item2);
		List<Item> newItems = new ArrayList<>();
		newItems.add(item1);
		assertArrayEquals(itemRepository.findDescriptionContainingOrNameContainingIgnoreCase("em1").toArray(), newItems.toArray());
		newItems.add(item2);
		assertArrayEquals(itemRepository.findDescriptionContainingOrNameContainingIgnoreCase("ite").toArray(), newItems.toArray());
	}

	@Test
	public void shouldSearchItemsByEmptyText() {
		User user = userRepository.createUser(user1);
		item1.setOwner(user.getId());
		itemRepository.addItem(item1);
		item2.setOwner(user.getId());
		itemRepository.addItem(item2);
		assertEquals(itemRepository.findDescriptionContainingOrNameContainingIgnoreCase("").toArray().length, 0);
	}
}
