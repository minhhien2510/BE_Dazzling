package io.dazzling.dazzling_backend.controller;

import io.dazzling.dazzling_backend.dto.StatCountResponse;
import io.dazzling.dazzling_backend.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin/stats")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/users")
    public List<StatCountResponse> userStats(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        DateRange range = resolveRange(from, to);
        return analyticsService.getUserStats(range.from(), range.to());
    }

    @GetMapping("/sessions")
    public List<StatCountResponse> sessionStats(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        DateRange range = resolveRange(from, to);
        return analyticsService.getSessionStats(range.from(), range.to());
    }

    @GetMapping("/photos")
    public List<StatCountResponse> photoStats(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        DateRange range = resolveRange(from, to);
        return analyticsService.getPhotoStats(range.from(), range.to());
    }

    private DateRange resolveRange(LocalDate from, LocalDate to) {
        LocalDate end = to != null ? to : LocalDate.now();
        LocalDate start = from != null ? from : end.minusDays(29);
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("'from' date must not be after 'to' date");
        }
        return new DateRange(start, end);
    }

    private record DateRange(LocalDate from, LocalDate to) {
    }
}
