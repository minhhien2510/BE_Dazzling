package io.dazzling.dazzling_backend.service;

import io.dazzling.dazzling_backend.dto.StatCountResponse;

import java.time.LocalDate;
import java.util.List;

public interface AnalyticsService {

    List<StatCountResponse> getUserStats(LocalDate from, LocalDate to);

    List<StatCountResponse> getSessionStats(LocalDate from, LocalDate to);

    List<StatCountResponse> getPhotoStats(LocalDate from, LocalDate to);
}
