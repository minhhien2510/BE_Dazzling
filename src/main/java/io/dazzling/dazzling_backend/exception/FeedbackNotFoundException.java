package io.dazzling.dazzling_backend.exception;

public class FeedbackNotFoundException extends RuntimeException {

    public FeedbackNotFoundException(Long id) {
        super("Feedback not found: " + id);
    }
}
