package net.oneblog.user.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserControllerAdvice {

    @ExceptionHandler(
        value = {UserNotFoundException.class})
    public ResponseEntity<Object> handleNotFoundException() {
        return ResponseEntity.notFound().build();
    }

}
