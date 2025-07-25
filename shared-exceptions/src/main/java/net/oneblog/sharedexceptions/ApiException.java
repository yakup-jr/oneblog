package net.oneblog.sharedexceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

/**
 * The type Api exception.
 */
@Builder
public record ApiException(String message, Throwable throwable, HttpStatus httpStatus,
                           ZonedDateTime timestamp) {
}
