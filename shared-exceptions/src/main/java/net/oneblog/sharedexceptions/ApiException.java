package net.oneblog.sharedexceptions;

import lombok.Builder;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

/**
 * The type Api exception.
 */
@Builder
public record ApiException(String message, Throwable throwable, HttpStatus httpStatus,
                           ZonedDateTime timestamp) {
}
