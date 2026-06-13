package io.dazzling.dazzling_backend.service.impl;

import io.dazzling.dazzling_backend.dto.DashboardResponse;
import io.dazzling.dazzling_backend.entity.FeedbackStatus;
import io.dazzling.dazzling_backend.repository.FeedbackRepository;
import io.dazzling.dazzling_backend.repository.GalleryItemRepository;
import io.dazzling.dazzling_backend.repository.PhotoSessionRepository;
import io.dazzling.dazzling_backend.repository.UserRepository;
import io.dazzling.dazzling_backend.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final UserRepository userRepository;
    private final PhotoSessionRepository photoSessionRepository;
    private final GalleryItemRepository galleryItemRepository;
    private final FeedbackRepository feedbackRepository;

    @Override
    @Transactional(readOnly = true)
    public DashboardResponse getSummary() {
        Instant startOfToday = LocalDate.now(ZoneOffset.UTC).atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant startOfTomorrow = LocalDate.now(ZoneOffset.UTC).plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC);

        long totalBytes = galleryItemRepository.sumFileSize();
        double storageUsedMB = totalBytes / (1024.0 * 1024.0);

        return new DashboardResponse(
                userRepository.count(),
                photoSessionRepository.countActiveUsersToday(startOfToday, startOfTomorrow),
                userRepository.countByCreatedAtGreaterThanEqualAndCreatedAtLessThan(startOfToday, startOfTomorrow),
                photoSessionRepository.count(),
                photoSessionRepository.countByCreatedAtGreaterThanEqualAndCreatedAtLessThan(startOfToday, startOfTomorrow),
                galleryItemRepository.count(),
                galleryItemRepository.countByCreatedAtGreaterThanEqualAndCreatedAtLessThan(startOfToday, startOfTomorrow),
                Math.round(storageUsedMB * 100.0) / 100.0,
                feedbackRepository.countByStatus(FeedbackStatus.OPEN)
        );
    }
}
