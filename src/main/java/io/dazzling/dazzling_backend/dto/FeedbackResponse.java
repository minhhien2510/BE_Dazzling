package io.dazzling.dazzling_backend.dto;

import io.dazzling.dazzling_backend.entity.FeedbackStatus;
import io.dazzling.dazzling_backend.entity.FeedbackType;

import java.time.Instant;
import java.util.List;

public record FeedbackResponse(
        Long id,
        Long userId,
        String title,
        String content,
        FeedbackType type,
        FeedbackStatus status,
        Instant createdAt,
        Instant updatedAt,
        List<FeedbackReplyResponse> replies
) {
}
