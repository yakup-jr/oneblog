package net.oneblog.email.exceptions;

import net.oneblog.sharedexceptions.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class EmailControllerAdvice {

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

}
