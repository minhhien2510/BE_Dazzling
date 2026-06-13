package io.dazzling.dazzling_backend.repository;

import io.dazzling.dazzling_backend.entity.PhotoSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface PhotoSessionRepository extends JpaRepository<PhotoSession, Long> {
    List<PhotoSession> findByUserId(Long userId);

    long countByCreatedAtGreaterThanEqualAndCreatedAtLessThan(Instant start, Instant end);

    @Query(value = """
            SELECT COUNT(DISTINCT user_id) FROM (
                SELECT user_id FROM photo_sessions WHERE created_at >= :start AND created_at < :end
                UNION
                SELECT user_id FROM gallery_items WHERE created_at >= :start AND created_at < :end
            ) active_users
            """, nativeQuery = true)
    long countActiveUsersToday(@Param("start") Instant start, @Param("end") Instant end);

    @Query(value = """
            SELECT DATE(created_at) AS date, COUNT(*) AS count
            FROM photo_sessions
            WHERE created_at >= :start AND created_at < :end
            GROUP BY DATE(created_at)
            ORDER BY date
            """, nativeQuery = true)
    List<Object[]> countByDateRange(@Param("start") Instant start, @Param("end") Instant end);
}
