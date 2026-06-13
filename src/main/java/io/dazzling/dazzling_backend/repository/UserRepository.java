package io.dazzling.dazzling_backend.repository;

import io.dazzling.dazzling_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository
        extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    long countByCreatedAtGreaterThanEqualAndCreatedAtLessThan(Instant start, Instant end);

    @Query(value = """
            SELECT DATE(created_at) AS date, COUNT(*) AS count
            FROM users
            WHERE created_at >= :start AND created_at < :end
            GROUP BY DATE(created_at)
            ORDER BY date
            """, nativeQuery = true)
    List<Object[]> countByDateRange(@Param("start") Instant start, @Param("end") Instant end);
}
