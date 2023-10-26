package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {
	@MockBean
	private ItemService itemService;

	@Autowired
	private MockMvc mvc;

	@Autowired
	private ObjectMapper objectMapper;
	private ItemDto item;
	private CommentDto comment;

	@BeforeEach
	public void beforeEach() {
		item = ItemDto.builder()
				.id(1)
				.name("Item")
				.description("A test item")
				.available(true)
				.build();

		comment = CommentDto.builder()
				.id(1)
				.text("A sample comment")
				.build();
	}

	@Test
	public void shouldAddItem() throws Exception {
		when(itemService.addItem(anyInt(), any())).thenReturn(item);

		mvc.perform(post("/items")
						.header("X-Sharer-User-Id", 1)
						.content(objectMapper.writeValueAsString(item))
						.characterEncoding(StandardCharsets.UTF_8)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(content().json(objectMapper.writeValueAsString(item)));
	}

	@Test
	public void shouldUpdateItem() throws Exception {
		when(itemService.updateItem(anyInt(), anyInt(), any(ItemDto.class)))
				.thenReturn(item);

		mvc.perform(patch("/items/1")
						.header("X-Sharer-User-Id", 1)
						.content(objectMapper.writeValueAsString(item))
						.characterEncoding(StandardCharsets.UTF_8)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().json(objectMapper.writeValueAsString(item)));
	}

	@Test
	public void shouldGetItemById() throws Exception {
		when(itemService.getItemById(anyInt(), anyInt()))
				.thenReturn(item);

		mvc.perform(get("/items/1")
						.header("X-Sharer-User-Id", 1)
						.characterEncoding(StandardCharsets.UTF_8)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().json(objectMapper.writeValueAsString(item)));
	}

	@Test
	public void shouldGetOwnerItems() throws Exception {
		when(itemService.getOwnerItems(anyInt()))
				.thenReturn(List.of(item));

		mvc.perform(get("/items")
						.header("X-Sharer-User-Id", 1)
						.characterEncoding(StandardCharsets.UTF_8)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().json(objectMapper.writeValueAsString(List.of(item))));
	}

	@Test
	public void shouldSearchItemsByText() throws Exception {
		when(itemService.searchItemsByText(anyString()))
				.thenReturn(List.of(item));

		mvc.perform(get("/items/search")
						.param("text", "sample")
						.characterEncoding(StandardCharsets.UTF_8)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().json(objectMapper.writeValueAsString(List.of(item))));
	}

	@Test
	public void shouldAddComment() throws Exception {
		when(itemService.addComment(anyInt(), anyInt(), any(CommentDto.class)))
				.thenReturn(comment);

		mvc.perform(post("/items/1/comment")
						.header("X-Sharer-User-Id", 1)
						.content(objectMapper.writeValueAsString(comment))
						.characterEncoding(StandardCharsets.UTF_8)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().json(objectMapper.writeValueAsString(comment)));
	}
}