package io.dazzling.dazzling_backend.controller;

import io.dazzling.dazzling_backend.dto.CreateFeedbackReplyRequest;
import io.dazzling.dazzling_backend.dto.FeedbackReplyResponse;
import io.dazzling.dazzling_backend.dto.FeedbackResponse;
import io.dazzling.dazzling_backend.dto.UpdateFeedbackStatusRequest;
import io.dazzling.dazzling_backend.entity.FeedbackStatus;
import io.dazzling.dazzling_backend.service.FeedbackService;
import io.dazzling.dazzling_backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/feedbacks")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminFeedbackController {

    private final FeedbackService feedbackService;
    private final UserService userService;

    @GetMapping
    public Page<FeedbackResponse> list(
            @RequestParam(required = false) FeedbackStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return feedbackService.listAll(status, PageRequest.of(page, size));
    }

    @GetMapping("/{id}")
    public FeedbackResponse get(@PathVariable Long id) {
        return feedbackService.getById(id);
    }

    @PutMapping("/{id}/status")
    public FeedbackResponse updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateFeedbackStatusRequest request) {
        return feedbackService.updateStatus(id, request);
    }

    @PostMapping("/{id}/reply")
    public FeedbackReplyResponse reply(
            @PathVariable Long id,
            @Valid @RequestBody CreateFeedbackReplyRequest request,
            Authentication auth) {
        Long adminId = resolveUserId(auth);
        return feedbackService.reply(id, adminId, request);
    }

    private Long resolveUserId(Authentication auth) {
        String email = auth.getName();
        return userService.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"))
                .getId();
    }
}
