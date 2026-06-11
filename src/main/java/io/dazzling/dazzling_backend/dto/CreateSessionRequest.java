package io.dazzling.dazzling_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateSessionRequest(
        @NotBlank
        String sessionName,
        @NotBlank
        String layoutType
) {
}
