package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemDtoTest {
	@Autowired
	private JacksonTester<ItemDto> json;

	@Test
	public void testUserDto() throws IOException {
		CommentDto commentDto = CommentDto.builder()
				.id(1)
				.text("text comment")
				.created(LocalDateTime.now())
				.build();
		ItemDto itemDto = ItemDto.builder()
				.id(1)
				.description("Description")
				.available(true)
				.requestId(6)
				.comments(List.of(commentDto))
				.build();

		JsonContent<ItemDto> result = json.write(itemDto);

		assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
		assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("Description");
		assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
		assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(6);
		assertThat(result).extractingJsonPathArrayValue("@.comments").extracting("text").isEqualTo(List.of("text comment"));
	}

}