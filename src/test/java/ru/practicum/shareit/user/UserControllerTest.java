package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {
	@MockBean
	private UserService userService;

	@Autowired
	private MockMvc mvc;

	@Autowired
	private ObjectMapper objectMapper;
	private UserDto user;

	@BeforeEach
	public void beforeEach() {
		user = UserDto.builder()
				.id(1)
				.name("user")
				.email("user@yandex.ru")
				.build();
	}

	@Test
	public void shouldCreateUser() throws Exception {
		when(userService.createUser(any()))
				.thenReturn(user);

		mvc.perform(post("/users")
						.content(objectMapper.writeValueAsString(user))
						.characterEncoding(StandardCharsets.UTF_8)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(content().json(objectMapper.writeValueAsString(user)));
	}

	@Test
	public void shouldUpdateUser() throws Exception {
		when(userService.updateUser(anyInt(), any()))
				.thenReturn(user);

		mvc.perform(patch("/users/1")
						.content(objectMapper.writeValueAsString(user))
						.characterEncoding(StandardCharsets.UTF_8)
						.contentType(MediaType.APPLICATION_JSON)
						.header("X-Sharer-User-Id", 1)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().json(objectMapper.writeValueAsString(user)));
	}

	@Test
	public void shouldGetUserById() throws Exception {
		when(userService.getUserById(anyInt()))
				.thenReturn(user);

		mvc.perform(get("/users/1")
						.characterEncoding(StandardCharsets.UTF_8)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().json(objectMapper.writeValueAsString(user)));
	}

	@Test
	public void shouldGetAllUsers() throws Exception {
		when(userService.getAllUsers())
				.thenReturn(List.of(user));

		mvc.perform(get("/users")
						.characterEncoding(StandardCharsets.UTF_8)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().json(objectMapper.writeValueAsString(List.of(user))));
	}

	@Test
	public void shouldDeleteUser() throws Exception {
		mvc.perform(delete("/users/1")
						.characterEncoding(StandardCharsets.UTF_8)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());
	}

	@Test
	public void shouldClearUsers() throws Exception {
		mvc.perform(delete("/users")
						.characterEncoding(StandardCharsets.UTF_8)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());
	}

}