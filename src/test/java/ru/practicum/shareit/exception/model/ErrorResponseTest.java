package ru.practicum.shareit.exception.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {

	@Test
	public void testErrorResponse() {
		ErrorResponse testError = new ErrorResponse("test");
		assertEquals("test", testError.getError());
	}

}