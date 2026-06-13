package io.dazzling.dazzling_backend.dto;

import io.dazzling.dazzling_backend.entity.FeedbackStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateFeedbackStatusRequest(
        @NotNull FeedbackStatus status
) {
}
