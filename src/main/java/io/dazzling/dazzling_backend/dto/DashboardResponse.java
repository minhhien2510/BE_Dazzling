package io.dazzling.dazzling_backend.dto;

public record DashboardResponse(
        long totalUsers,
        long activeUsersToday,
        long newUsersToday,
        long totalSessions,
        long sessionsToday,
        long totalPhotos,
        long photosToday,
        double storageUsedMB,
        long openFeedbacks
) {
}
