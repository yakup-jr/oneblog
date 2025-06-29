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

/**
 * The type Api exception handler.
 */
@ControllerAdvice
public class ApiExceptionHandler {

	/**
	 * Handle api request exception response entity.
	 *
	 * @param e the e
	 * @return the response entity
	 */
	@ExceptionHandler(value = {ApiRequestException.class})
	public ResponseEntity<Object> handleApiRequestException(
		ApiRequestException e) {
		HttpStatus badRequest = HttpStatus.BAD_REQUEST;

		ApiException apiException = ApiException.builder().message(e.getMessage()).httpStatus(badRequest).build();

		return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(apiException);
	}

	/**
	 * Handle not found exception response entity.
	 *
	 * @return the response entity
	 */
	@ExceptionHandler(
		value = {LabelNotFoundException.class, ArticleNotFoundException.class, UserNotFoundException.class,
			PageNotFoundException.class})
	public ResponseEntity<Object> handleNotFoundException() {
		return ResponseEntity.notFound().build();
	}

	/**
	 * Handle conflict exception response entity.
	 *
	 * @param e the e
	 * @return the response entity
	 */
	@ExceptionHandler(value = {ConflictException.class})
	public ResponseEntity<Object> handleConflictException(ConflictException e) {
		HttpStatus conflict = HttpStatus.CONFLICT;

		ApiException conflictException = ApiException.builder().message(e.getMessage()).httpStatus(conflict).build();

		return ResponseEntity.status(conflict).contentType(MediaType.APPLICATION_JSON)
		                     .body(conflictException);
	}


	/**
	 * Handle argument not valid exception response entity.
	 *
	 * @param e the e
	 * @return the response entity
	 */
	@ExceptionHandler(value = {MethodArgumentNotValidException.class})
	public ResponseEntity<Object> handleArgumentNotValidException(
		MethodArgumentNotValidException e) {

		HttpStatus badRequest = HttpStatus.BAD_REQUEST;

		ApiException apiException = ApiException.builder().message(e.getFieldErrors().stream().map(
			                                                            validationError -> validationError.getField() + ": " + validationError.getDefaultMessage()).toList()
		                                                            .toString()).httpStatus(badRequest).build();

		return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(apiException);
	}

	/**
	 * Handle signature exception response entity.
	 *
	 * @return the response entity
	 */
	@ExceptionHandler(value = {SignatureException.class})
	public ResponseEntity<Object> handleSignatureException() {
		ApiException apiException =
			ApiException.builder().message("Invalid token").httpStatus(HttpStatus.UNAUTHORIZED).build();

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).contentType(MediaType.APPLICATION_JSON)
		                     .body(apiException);
	}

	/**
	 * Handle general security exception response entity.
	 *
	 * @return the response entity
	 */
	@ExceptionHandler(value = {GeneralSecurityException.class})
	public ResponseEntity<Object> handleGeneralSecurityException() {
		ApiException apiException =
			ApiException.builder().message("Invalid token").httpStatus(HttpStatus.UNAUTHORIZED).build();

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).contentType(MediaType.APPLICATION_JSON)
		                     .body(apiException);
	}

	/**
	 * Handle email verification code not found response entity.
	 *
	 * @param e the e
	 * @return the response entity
	 */
	@ExceptionHandler(value = {EmailVerificationCodeNotFound.class})
	public ResponseEntity<Object> handleEmailVerificationCodeNotFound(EmailVerificationCodeNotFound e) {
		ApiException apiException =
			ApiException.builder().message(e.getMessage()).httpStatus(HttpStatus.NOT_FOUND).build();

		return ResponseEntity.status(apiException.getHttpStatus()).contentType(MediaType.APPLICATION_JSON)
		                     .body(apiException);
	}

	/**
	 * Handle invalid verification code exception response entity.
	 *
	 * @param e the e
	 * @return the response entity
	 */
	@ExceptionHandler(value = {InvalidVerificationCodeException.class})
	public ResponseEntity<Object> handleInvalidVerificationCodeException(InvalidVerificationCodeException e) {
		ApiException apiException =
			ApiException.builder().message(e.getMessage()).httpStatus(HttpStatus.UNAUTHORIZED).build();

		return ResponseEntity.status(apiException.getHttpStatus()).contentType(MediaType.APPLICATION_JSON)
		                     .body(apiException);
	}

	/**
	 * Handle service exception response entity.
	 *
	 * @param e the e
	 * @return the response entity
	 */
	@ExceptionHandler(value = {ServiceException.class})
	public ResponseEntity<Object> handleServiceException(ServiceException e) {
		ApiException apiException =
			ApiException.builder().message(e.getMessage()).httpStatus(HttpStatus.BAD_REQUEST).build();

		return ResponseEntity.status(apiException.getHttpStatus()).contentType(MediaType.APPLICATION_JSON)
		                     .body(apiException);
	}
}
