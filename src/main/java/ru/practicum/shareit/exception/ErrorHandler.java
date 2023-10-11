package ru.practicum.shareit.exception;

import com.google.gson.Gson;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exception.exceptions.*;
import ru.practicum.shareit.exception.model.ErrorResponse;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NonUniqueResultException;
import javax.validation.ConstraintViolationException;
import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

	@ExceptionHandler
	@ResponseStatus(HttpStatus.CONFLICT)
	public ErrorResponse handleEntityAlreadyExistsException(EntityAlreadyExistsException e) {
		log.debug("Get status 409. CONFLICT {}", e.getMessage(), e);
		return new ErrorResponse(e.getMessage());
	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorResponse handleNotFound(final NotFoundException e) {
		log.debug("Get status 404 Not found {}", e.getMessage(), e);
		return new ErrorResponse(e.getMessage());
	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorResponse authorizationError(final AuthorizationErrorException e) {
		log.debug("Get status 404 Not found {}", e.getMessage(), e);
		return new ErrorResponse(e.getMessage());
	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse constraintViolationError(final ConstraintViolationException e) {
		log.debug("Get status 400 {}", e.getMessage(), e);
		return new ErrorResponse(e.getMessage());
	}


	@ExceptionHandler
	@ResponseStatus(HttpStatus.CONFLICT)
	public ErrorResponse dataIntegrityViolationError(final DataIntegrityViolationException e) {
		log.debug("Get status 409  {}", e.getMessage(), e);
		return new ErrorResponse(e.getMessage());
	}
	@ExceptionHandler
	@ResponseStatus(HttpStatus.CONFLICT)
	public ErrorResponse duplicateRequestError(final DuplicateRequestException e) {
		log.debug("Get status 409 {}", e.getMessage(), e);
		return new ErrorResponse(e.getMessage());
	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorResponse entityNotFoundError(final EntityNotFoundException e) {
		log.debug("Get status 404 {}", e.getMessage(), e);
		return new ErrorResponse(e.getMessage());
	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse dateTimeError(final DateTimeException e) {
		log.debug("Get status 400 {}", e.getMessage(), e);
		return new ErrorResponse(e.getMessage());
	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorResponse noSuchElementError(final NoSuchElementException e) {
		log.debug("Get status 404 {}", e.getMessage(), e);
		return new ErrorResponse(e.getMessage());
	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse notAvailableError(final NotAvailableException e) {
		log.debug("Get status 400 {}", e.getMessage(), e);
		return new ErrorResponse(e.getMessage());
	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse statusError(final StatusException e) {
		log.debug("Get status 400 {}", e.getMessage(), e);
		return new ErrorResponse(e.getMessage());
	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse commentError(final CommentException e) {
		log.debug("Get status 400 {}", e.getMessage(), e);
		return new ErrorResponse(e.getMessage());
	}

	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse nonUniqueResultError(final NonUniqueResultException e) {
		log.debug("Get status 400 {}", e.getMessage(), e);
		return new ErrorResponse(e.getMessage());
	}
}
