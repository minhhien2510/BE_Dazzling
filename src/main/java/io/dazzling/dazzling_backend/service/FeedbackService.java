package io.dazzling.dazzling_backend.service;

import io.dazzling.dazzling_backend.dto.CreateFeedbackReplyRequest;
import io.dazzling.dazzling_backend.dto.CreateFeedbackRequest;
import io.dazzling.dazzling_backend.dto.FeedbackReplyResponse;
import io.dazzling.dazzling_backend.dto.FeedbackResponse;
import io.dazzling.dazzling_backend.dto.UpdateFeedbackStatusRequest;
import io.dazzling.dazzling_backend.entity.FeedbackStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FeedbackService {

    FeedbackResponse create(Long userId, CreateFeedbackRequest request);

    List<FeedbackResponse> getMyFeedbacks(Long userId);

    Page<FeedbackResponse> listAll(FeedbackStatus status, Pageable pageable);

    FeedbackResponse getById(Long id);

    FeedbackResponse updateStatus(Long id, UpdateFeedbackStatusRequest request);

    FeedbackReplyResponse reply(Long feedbackId, Long adminId, CreateFeedbackReplyRequest request);
}
