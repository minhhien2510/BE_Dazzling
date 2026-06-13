package io.dazzling.dazzling_backend.dto;

import java.time.Instant;

public record FeedbackReplyResponse(
        Long id,
        Long feedbackId,
        Long adminId,
        String message,
        Instant createdAt
) {
}
