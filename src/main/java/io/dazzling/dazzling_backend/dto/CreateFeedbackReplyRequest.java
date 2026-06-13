package io.dazzling.dazzling_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateFeedbackReplyRequest(
        @NotBlank @Size(max = 5000) String message
) {
}
