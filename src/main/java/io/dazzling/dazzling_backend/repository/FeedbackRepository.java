package io.dazzling.dazzling_backend.repository;

import io.dazzling.dazzling_backend.entity.Feedback;
import io.dazzling.dazzling_backend.entity.FeedbackStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    List<Feedback> findByUserIdOrderByCreatedAtDesc(Long userId);

    Page<Feedback> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<Feedback> findByStatusOrderByCreatedAtDesc(FeedbackStatus status, Pageable pageable);

    long countByStatus(FeedbackStatus status);
}
