package io.dazzling.dazzling_backend.controller;

import io.dazzling.dazzling_backend.dto.CreateFeedbackRequest;
import io.dazzling.dazzling_backend.dto.FeedbackResponse;
import io.dazzling.dazzling_backend.service.FeedbackService;
import io.dazzling.dazzling_backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final UserService userService;

    @PostMapping
    public FeedbackResponse create(@Valid @RequestBody CreateFeedbackRequest request, Authentication auth) {
        Long userId = resolveUserId(auth);
        return feedbackService.create(userId, request);
    }

    @GetMapping("/my")
    public List<FeedbackResponse> myFeedbacks(Authentication auth) {
        Long userId = resolveUserId(auth);
        return feedbackService.getMyFeedbacks(userId);
    }

    private Long resolveUserId(Authentication auth) {
        String email = auth.getName();
        return userService.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"))
                .getId();
    }
}
