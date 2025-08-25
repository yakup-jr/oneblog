package net.oneblog.user.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * The type User controller advice.
 */
@ControllerAdvice
public class UserControllerAdvice {

    /**
     * Handle not found exception response entity.
     *
     * @return the response entity
     */
    @ExceptionHandler(
        value = {UserNotFoundException.class})
    public ResponseEntity<Object> handleNotFoundException() {
        return ResponseEntity.notFound().build();
    }

}
