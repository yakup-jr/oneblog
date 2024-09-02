package com.oneblog.exceptions;

import com.oneblog.article.label.LabelNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(value = {ApiRequestException.class})
	public ResponseEntity<Object> handleApiRequestException(
		ApiRequestException e) {
		HttpStatus badRequest = HttpStatus.BAD_REQUEST;

		ApiException apiException = ApiException.builder().message(e.getMessage()).httpStatus(badRequest).build();

		return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(apiException);
	}

	@ExceptionHandler(value = {LabelNotFoundException.class})
	public ResponseEntity<Object> handleNotFoundException(NotFound e) {
		return ResponseEntity.notFound().build();
	}

	@ExceptionHandler(value = {MethodArgumentNotValidException.class})
	public ResponseEntity<Object> handleArgumentNotValidException(
		MethodArgumentNotValidException e) {

		HttpStatus badRequest = HttpStatus.BAD_REQUEST;

		ApiException apiException = ApiException.builder().message(e.getMessage()).httpStatus(badRequest).build();

		return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(apiException);
	}
}
