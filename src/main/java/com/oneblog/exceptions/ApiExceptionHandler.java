package com.oneblog.exceptions;

import com.oneblog.article.ArticleNotFoundException;
import com.oneblog.article.label.LabelNotFoundException;
import com.oneblog.auth.exception.EmailVerificationCodeNotFound;
import com.oneblog.auth.exception.InvalidVerificationCodeException;
import com.oneblog.user.UserNotFoundException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.security.GeneralSecurityException;

@ControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(value = {ApiRequestException.class})
	public ResponseEntity<Object> handleApiRequestException(
		ApiRequestException e) {
		HttpStatus badRequest = HttpStatus.BAD_REQUEST;

		ApiException apiException = ApiException.builder().message(e.getMessage()).httpStatus(badRequest).build();

		return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(apiException);
	}

	@ExceptionHandler(
		value = {LabelNotFoundException.class, ArticleNotFoundException.class, UserNotFoundException.class,
			PageNotFoundException.class})
	public ResponseEntity<Object> handleNotFoundException() {
		return ResponseEntity.notFound().build();
	}

	@ExceptionHandler(value = {ConflictException.class})
	public ResponseEntity<Object> handleConflictException(ConflictException e) {
		HttpStatus conflict = HttpStatus.CONFLICT;

		ApiException conflictException = ApiException.builder().message(e.getMessage()).httpStatus(conflict).build();

		return ResponseEntity.status(conflict).contentType(MediaType.APPLICATION_JSON)
		                     .body(conflictException);
	}


	@ExceptionHandler(value = {MethodArgumentNotValidException.class})
	public ResponseEntity<Object> handleArgumentNotValidException(
		MethodArgumentNotValidException e) {

		HttpStatus badRequest = HttpStatus.BAD_REQUEST;

		ApiException apiException = ApiException.builder().message(e.getFieldErrors().stream().map(
			                                                            validationError -> validationError.getField() + ": " + validationError.getDefaultMessage()).toList()
		                                                            .toString()).httpStatus(badRequest).build();

		return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(apiException);
	}

	@ExceptionHandler(value = {SignatureException.class})
	public ResponseEntity<Object> handleSignatureException(SignatureException e) {
		ApiException apiException =
			ApiException.builder().message("Invalid token").httpStatus(HttpStatus.UNAUTHORIZED).build();

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).contentType(MediaType.APPLICATION_JSON)
		                     .body(apiException);
	}

	@ExceptionHandler(value = {GeneralSecurityException.class})
	public ResponseEntity<Object> handleGeneralSecurityException(GeneralSecurityException gse) {
		ApiException apiException =
			ApiException.builder().message("Invalid token").httpStatus(HttpStatus.UNAUTHORIZED).build();

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).contentType(MediaType.APPLICATION_JSON)
		                     .body(apiException);
	}

	@ExceptionHandler(value = {EmailVerificationCodeNotFound.class})
	public ResponseEntity<Object> handleEmailVerificationCodeNotFound(EmailVerificationCodeNotFound e) {
		ApiException apiException =
			ApiException.builder().message(e.getMessage()).httpStatus(HttpStatus.NOT_FOUND).build();

		return ResponseEntity.status(apiException.getHttpStatus()).contentType(MediaType.APPLICATION_JSON)
		                     .body(apiException);
	}

	@ExceptionHandler(value = {InvalidVerificationCodeException.class})
	public ResponseEntity<Object> handleInvalidVerificationCodeException(InvalidVerificationCodeException e) {
		ApiException apiException =
			ApiException.builder().message(e.getMessage()).httpStatus(HttpStatus.UNAUTHORIZED).build();

		return ResponseEntity.status(apiException.getHttpStatus()).contentType(MediaType.APPLICATION_JSON)
		                     .body(apiException);
	}
}
