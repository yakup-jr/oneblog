package net.oneblog.article.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * The type Article controller advice.
 */
@RestControllerAdvice
public class ArticleControllerAdvice {

    /**
     * Handle not found exception response entity.
     *
     * @return the response entity
     */
    @ExceptionHandler(value = {LabelNotFoundException.class, ArticleNotFoundException.class})
    public ResponseEntity<Object> handleNotFoundException() {
        return ResponseEntity.notFound().build();
    }

}
