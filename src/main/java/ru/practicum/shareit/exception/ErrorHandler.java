package ru.practicum.shareit.exception;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exception.exceptions.AuthorizationErrorException;
import ru.practicum.shareit.exception.exceptions.EntityAlreadyExistsException;
import ru.practicum.shareit.exception.exceptions.NotFoundException;
import ru.practicum.shareit.exception.model.ErrorResponse;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

	Gson gson = new Gson();

	@ExceptionHandler
	@ResponseStatus(HttpStatus.CONFLICT)
	public ErrorResponse handleEntityAlreadyExistsException(EntityAlreadyExistsException e) {
		log.debug("Get status 409. CONFLICT {}", e.getMessage(), e);
		return new ErrorResponse(gson.toJson(e.getMessage()));
	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorResponse handleNotFound(final NotFoundException e) {
		log.debug("Получен статус 404 Not found {}", e.getMessage(), e);
		return new ErrorResponse(gson.toJson(e.getMessage()));
	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorResponse authorizationError(final AuthorizationErrorException e) {
		log.debug("Получен статус 404 Not found {}", e.getMessage(), e);
		return new ErrorResponse(gson.toJson(e.getMessage()));
	}
}
