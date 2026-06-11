package io.dazzling.dazzling_backend.service;

import io.dazzling.dazzling_backend.dto.CreateSessionRequest;
import io.dazzling.dazzling_backend.dto.SessionResponse;

import java.util.List;

public interface PhotoSessionService {
    SessionResponse createSession(Long userId, CreateSessionRequest request);
    List<SessionResponse> listSessions(Long userId);
    SessionResponse getSession(Long userId, Long id);
    void deleteSession(Long userId, Long id);
}
