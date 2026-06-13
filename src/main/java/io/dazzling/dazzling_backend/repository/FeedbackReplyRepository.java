package io.dazzling.dazzling_backend.repository;

import io.dazzling.dazzling_backend.entity.FeedbackReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackReplyRepository extends JpaRepository<FeedbackReply, Long> {

    List<FeedbackReply> findByFeedbackIdOrderByCreatedAtAsc(Long feedbackId);
}
