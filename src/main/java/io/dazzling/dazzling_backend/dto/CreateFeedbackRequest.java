package io.dazzling.dazzling_backend.dto;

import io.dazzling.dazzling_backend.entity.FeedbackType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateFeedbackRequest(
        @NotBlank @Size(max = 200) String title,
        @NotBlank @Size(max = 5000) String content,
        @NotNull FeedbackType type
) {
}
