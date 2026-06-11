package io.dazzling.dazzling_backend.service.impl;

import io.dazzling.dazzling_backend.dto.CreateSessionRequest;
import io.dazzling.dazzling_backend.dto.SessionResponse;
import io.dazzling.dazzling_backend.entity.PhotoSession;
import io.dazzling.dazzling_backend.exception.StorageException;
import io.dazzling.dazzling_backend.repository.PhotoSessionRepository;
import io.dazzling.dazzling_backend.service.PhotoSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PhotoSessionServiceImpl implements PhotoSessionService {

    private final PhotoSessionRepository repository;

    @Override
    public SessionResponse createSession(Long userId, CreateSessionRequest request) {
        PhotoSession session = PhotoSession.builder()
                .userId(userId)
                .sessionName(request.sessionName())
                .layoutType(request.layoutType())
                .createdAt(Instant.now())
                .build();

        session = repository.save(session);
        return new SessionResponse(session.getId(), session.getUserId(), session.getSessionName(), session.getLayoutType());
    }

    @Override
    public List<SessionResponse> listSessions(Long userId) {
        return repository.findByUserId(userId).stream()
                .map(s -> new SessionResponse(s.getId(), s.getUserId(), s.getSessionName(), s.getLayoutType()))
                .collect(Collectors.toList());
    }

    @Override
    public SessionResponse getSession(Long userId, Long id) {
        PhotoSession s = repository.findById(id).orElseThrow(() -> new StorageException("Session not found"));
        if (!s.getUserId().equals(userId)) throw new StorageException("Forbidden");
        return new SessionResponse(s.getId(), s.getUserId(), s.getSessionName(), s.getLayoutType());
    }

    @Override
    public void deleteSession(Long userId, Long id) {
        PhotoSession s = repository.findById(id).orElseThrow(() -> new StorageException("Session not found"));
        if (!s.getUserId().equals(userId)) throw new StorageException("Forbidden");
        repository.delete(s);
    }
}
