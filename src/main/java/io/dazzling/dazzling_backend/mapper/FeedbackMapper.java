package io.dazzling.dazzling_backend.mapper;

import io.dazzling.dazzling_backend.dto.FeedbackReplyResponse;
import io.dazzling.dazzling_backend.dto.FeedbackResponse;
import io.dazzling.dazzling_backend.entity.Feedback;
import io.dazzling.dazzling_backend.entity.FeedbackReply;
import io.dazzling.dazzling_backend.entity.FeedbackStatus;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class FeedbackMapper {

    public FeedbackResponse toResponse(Feedback feedback) {
        return toResponse(feedback, Collections.emptyList());
    }

    public FeedbackResponse toResponse(Feedback feedback, List<FeedbackReply> replies) {
        List<FeedbackReplyResponse> replyResponses = replies.stream()
                .map(this::toReplyResponse)
                .toList();

        return new FeedbackResponse(
                feedback.getId(),
                feedback.getUserId(),
                feedback.getTitle(),
                feedback.getContent(),
                feedback.getType(),
                feedback.getStatus(),
                feedback.getCreatedAt(),
                feedback.getUpdatedAt(),
                replyResponses
        );
    }

    public FeedbackReplyResponse toReplyResponse(FeedbackReply reply) {
        return new FeedbackReplyResponse(
                reply.getId(),
                reply.getFeedbackId(),
                reply.getAdminId(),
                reply.getMessage(),
                reply.getCreatedAt()
        );
    }

    public Feedback toEntity(Long userId, String title, String content,
                             io.dazzling.dazzling_backend.entity.FeedbackType type,
                             java.time.Instant now) {
        return Feedback.builder()
                .userId(userId)
                .title(title)
                .content(content)
                .type(type)
                .status(FeedbackStatus.OPEN)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    public FeedbackReply toReplyEntity(Long feedbackId, Long adminId, String message, java.time.Instant now) {
        return FeedbackReply.builder()
                .feedbackId(feedbackId)
                .adminId(adminId)
                .message(message)
                .createdAt(now)
                .build();
    }
}
