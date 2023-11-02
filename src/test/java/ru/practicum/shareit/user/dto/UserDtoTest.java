package ru.practicum.shareit.user.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class UserDtoTest {
	@Autowired
	private JacksonTester<UserDto> json;

	@Test
	public void testUserDto() throws IOException {
		UserDto user = UserDto.builder()
				.id(1)
				.name("user")
				.email("user@yandex.ru")
				.build();

		JsonContent<UserDto> result = json.write(user);

		assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
		assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("user");
		assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("user@yandex.ru");
	}

}