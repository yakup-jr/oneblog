package net.oneblog.article.exception;

public class VoteAlreadyExistsException extends RuntimeException {
    public VoteAlreadyExistsException(String message) {
        super(message);
    }
}
