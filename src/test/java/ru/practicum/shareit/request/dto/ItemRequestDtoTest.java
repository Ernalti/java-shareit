package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDto;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class ItemRequestDtoTest {
	@Autowired
	private JacksonTester<ItemRequestDto> json;


	@Test
	void testSerializeItemRequestDto() throws IOException {
		ItemRequestDto itemRequestDto = ItemRequestDto.builder()
				.id(1)
				.description("Sample description")
				.requestorId(2)
				.created(LocalDateTime.of(2023, 10, 26, 12, 0))
				.items(List.of(
						ItemDto.builder()
								.id(3)
								.name("Item 1")
								.description("description1")
								.build(),
						ItemDto.builder()
								.id(4)
								.name("Item 2")
								.description("description2")
								.build()
				))
				.build();
		JsonContent<ItemRequestDto> result = json.write(itemRequestDto);

		assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
		assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("Sample description");
		assertThat(result).extractingJsonPathNumberValue("$.requestorId").isEqualTo(2);
		assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo("2023-10-26T12:00:00");
		assertThat(result).extractingJsonPathArrayValue("$.items").hasSize(2);
		assertThat(result).extractingJsonPathNumberValue("$.items[0].id").isEqualTo(3);
		assertThat(result).extractingJsonPathStringValue("$.items[0].name").isEqualTo("Item 1");
		assertThat(result).extractingJsonPathStringValue("$.items[0].description").isEqualTo("description1");
		assertThat(result).extractingJsonPathNumberValue("$.items[1].id").isEqualTo(4);
		assertThat(result).extractingJsonPathStringValue("$.items[1].name").isEqualTo("Item 2");
		assertThat(result).extractingJsonPathStringValue("$.items[1].description").isEqualTo("description2");
	}
}