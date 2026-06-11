package io.dazzling.dazzling_backend.repository;

import io.dazzling.dazzling_backend.entity.PhotoSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoSessionRepository extends JpaRepository<PhotoSession, Long> {
    List<PhotoSession> findByUserId(Long userId);
}
