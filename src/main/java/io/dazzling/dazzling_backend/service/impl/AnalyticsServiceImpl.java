package io.dazzling.dazzling_backend.service.impl;

import io.dazzling.dazzling_backend.dto.StatCountResponse;
import io.dazzling.dazzling_backend.mapper.AnalyticsMapper;
import io.dazzling.dazzling_backend.repository.GalleryItemRepository;
import io.dazzling.dazzling_backend.repository.PhotoSessionRepository;
import io.dazzling.dazzling_backend.repository.UserRepository;
import io.dazzling.dazzling_backend.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {

    private final UserRepository userRepository;
    private final PhotoSessionRepository photoSessionRepository;
    private final GalleryItemRepository galleryItemRepository;
    private final AnalyticsMapper analyticsMapper;

    @Override
    @Transactional(readOnly = true)
    public List<StatCountResponse> getUserStats(LocalDate from, LocalDate to) {
        return analyticsMapper.toStatCountResponses(
                userRepository.countByDateRange(toInstantStart(from), toInstantEnd(to))
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<StatCountResponse> getSessionStats(LocalDate from, LocalDate to) {
        return analyticsMapper.toStatCountResponses(
                photoSessionRepository.countByDateRange(toInstantStart(from), toInstantEnd(to))
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<StatCountResponse> getPhotoStats(LocalDate from, LocalDate to) {
        return analyticsMapper.toStatCountResponses(
                galleryItemRepository.countByDateRange(toInstantStart(from), toInstantEnd(to))
        );
    }

    private Instant toInstantStart(LocalDate date) {
        return date.atStartOfDay().toInstant(ZoneOffset.UTC);
    }

    private Instant toInstantEnd(LocalDate date) {
        return date.plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC);
    }
}
