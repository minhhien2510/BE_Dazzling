package io.dazzling.dazzling_backend.service.impl;

import io.dazzling.dazzling_backend.dto.CreateFeedbackReplyRequest;
import io.dazzling.dazzling_backend.dto.CreateFeedbackRequest;
import io.dazzling.dazzling_backend.dto.FeedbackReplyResponse;
import io.dazzling.dazzling_backend.dto.FeedbackResponse;
import io.dazzling.dazzling_backend.dto.UpdateFeedbackStatusRequest;
import io.dazzling.dazzling_backend.entity.Feedback;
import io.dazzling.dazzling_backend.entity.FeedbackReply;
import io.dazzling.dazzling_backend.entity.FeedbackStatus;
import io.dazzling.dazzling_backend.exception.FeedbackNotFoundException;
import io.dazzling.dazzling_backend.mapper.FeedbackMapper;
import io.dazzling.dazzling_backend.repository.FeedbackReplyRepository;
import io.dazzling.dazzling_backend.repository.FeedbackRepository;
import io.dazzling.dazzling_backend.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final FeedbackReplyRepository feedbackReplyRepository;
    private final FeedbackMapper feedbackMapper;

    @Override
    @Transactional
    public FeedbackResponse create(Long userId, CreateFeedbackRequest request) {
        Instant now = Instant.now();
        Feedback feedback = feedbackMapper.toEntity(
                userId,
                request.title(),
                request.content(),
                request.type(),
                now
        );
        feedback = feedbackRepository.save(feedback);
        return feedbackMapper.toResponse(feedback);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FeedbackResponse> getMyFeedbacks(Long userId) {
        return feedbackRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(feedbackMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FeedbackResponse> listAll(FeedbackStatus status, Pageable pageable) {
        Page<Feedback> page = status == null
                ? feedbackRepository.findAllByOrderByCreatedAtDesc(pageable)
                : feedbackRepository.findByStatusOrderByCreatedAtDesc(status, pageable);
        return page.map(feedbackMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public FeedbackResponse getById(Long id) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new FeedbackNotFoundException(id));
        List<FeedbackReply> replies = feedbackReplyRepository.findByFeedbackIdOrderByCreatedAtAsc(id);
        return feedbackMapper.toResponse(feedback, replies);
    }

    @Override
    @Transactional
    public FeedbackResponse updateStatus(Long id, UpdateFeedbackStatusRequest request) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new FeedbackNotFoundException(id));
        feedback.setStatus(request.status());
        feedback.setUpdatedAt(Instant.now());
        feedback = feedbackRepository.save(feedback);
        return feedbackMapper.toResponse(feedback);
    }

    @Override
    @Transactional
    public FeedbackReplyResponse reply(Long feedbackId, Long adminId, CreateFeedbackReplyRequest request) {
        if (!feedbackRepository.existsById(feedbackId)) {
            throw new FeedbackNotFoundException(feedbackId);
        }

        FeedbackReply reply = feedbackMapper.toReplyEntity(
                feedbackId,
                adminId,
                request.message(),
                Instant.now()
        );
        reply = feedbackReplyRepository.save(reply);
        return feedbackMapper.toReplyResponse(reply);
    }
}
