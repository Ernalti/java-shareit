package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {
	@MockBean
	private ItemRequestService itemRequestService;

	@Autowired
	private MockMvc mvc;

	@Autowired
	private ObjectMapper objectMapper;

	private ItemRequestDto itemRequest;

	@BeforeEach
	void setUp() {
		itemRequest = ItemRequestDto.builder()
				.id(1)
				.description("A sample request")
				.requestorId(2)
				.build();
	}

	@Test
	void shouldAddItemRequest() throws Exception {
		when(itemRequestService.addItemRequest(anyInt(), any(ItemRequestDto.class))).thenReturn(itemRequest);

		mvc.perform(post("/requests")
						.header("X-Sharer-User-Id", 1)
						.content(objectMapper.writeValueAsString(itemRequest))
						.characterEncoding(StandardCharsets.UTF_8)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(content().json(objectMapper.writeValueAsString(itemRequest)));
	}

	@Test
	void shouldGetOwnerItemRequests() throws Exception {
		when(itemRequestService.getOwnerItemRequests(anyInt())).thenReturn(List.of(itemRequest));

		mvc.perform(get("/requests")
						.header("X-Sharer-User-Id", 1)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().json(objectMapper.writeValueAsString(List.of(itemRequest))));
	}

	@Test
	void shouldGetAllItemRequests() throws Exception {
		when(itemRequestService.getAllItemRequests(anyInt(), anyInt(), anyInt())).thenReturn(List.of(itemRequest));

		mvc.perform(get("/requests/all")
						.header("X-Sharer-User-Id", 1)
						.param("from", "0")
						.param("size", "10")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().json(objectMapper.writeValueAsString(List.of(itemRequest))));
	}

	@Test
	void shouldGetItemRequestById() throws Exception {
		when(itemRequestService.getItemRequestById(anyInt(), anyInt())).thenReturn(itemRequest);

		mvc.perform(get("/requests/1")
						.header("X-Sharer-User-Id", 1)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().json(objectMapper.writeValueAsString(itemRequest)));
	}

	@Test
	void shouldBadRequestWhenDescriptionIsBlank() throws Exception {
		itemRequest.setDescription("");

		mvc.perform(post("/requests")
						.header("X-Sharer-User-Id", 1)
						.content(objectMapper.writeValueAsString(itemRequest))
						.characterEncoding(StandardCharsets.UTF_8)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

}