package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exception.exceptions.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class InMemoryItemStorageTest {

	private final ItemStorage itemStorage;
	private Item item1;
	private Item item2;

	private final UserStorage userStorage;
	User user1;
	User user2;


	@BeforeEach
	public void beforeEach() {
		itemStorage.clearItems();
		item1 = new Item(null, "item1", "Description 1", true, null, null);
		item2 = new Item(null, "item2", "Description 2", true, null, null);
		userStorage.clearUsers();
		user1 = new User(null, "user1", "user1@user1.ru");
		user2 = new User(null, "user2", "user2@user2.ru");
	}

	@Test
	public void shouldAddAndGetItem() {
		User user = userStorage.createUser(user1);
		item1.setOwner(user.getId());
		Item item = itemStorage.addItem(item1);
		item1.setId(item.getId());
		assertEquals(itemStorage.getItemById(item1.getId()), item1);
	}

	@Test
	public void shouldUpdateItem() {
		User user = userStorage.createUser(user1);
		item1.setOwner(user.getId());
		Item item = itemStorage.addItem(item1);
		item1.setId(item.getId());
		itemStorage.updateItem(item.getId(), item2);
	}

	@Test
	public void shouldNotGetItemWithWrongId() {
		assertThrows(NotFoundException.class, () ->
				itemStorage.getItemById(9876));
	}

	@Test
	public void shouldGetOwnerItems() {
		User user = userStorage.createUser(user1);
		item1.setOwner(user.getId());
		itemStorage.addItem(item1);
		item2.setOwner(user.getId());
		itemStorage.addItem(item2);
		List<Item> newItems = new ArrayList<>();
		newItems.add(item1);
		newItems.add(item2);
		assertArrayEquals(itemStorage.getOwnerItems(user.getId()).toArray(), newItems.toArray());
	}

	@Test
	public void shouldSearchItemsByText() {
		User user = userStorage.createUser(user1);
		item1.setOwner(user.getId());
		itemStorage.addItem(item1);
		item2.setOwner(user.getId());
		itemStorage.addItem(item2);
		List<Item> newItems = new ArrayList<>();
		newItems.add(item1);
		assertArrayEquals(itemStorage.searchItemsByText("em1").toArray(), newItems.toArray());
		newItems.add(item2);
		assertArrayEquals(itemStorage.searchItemsByText("ite").toArray(), newItems.toArray());
	}

	@Test
	public void shouldSearchItemsByEmptyText() {
		User user = userStorage.createUser(user1);
		item1.setOwner(user.getId());
		itemStorage.addItem(item1);
		item2.setOwner(user.getId());
		itemStorage.addItem(item2);
		assertEquals(itemStorage.searchItemsByText("").toArray().length, 0);
	}
}
