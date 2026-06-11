package io.dazzling.dazzling_backend.controller;

import io.dazzling.dazzling_backend.dto.CreateSessionRequest;
import io.dazzling.dazzling_backend.dto.SessionResponse;
import io.dazzling.dazzling_backend.service.PhotoSessionService;
import io.dazzling.dazzling_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
public class SessionController {

    private final PhotoSessionService service;
    private final UserService userService;

    @PostMapping
    public SessionResponse create(@Valid @RequestBody CreateSessionRequest request, Authentication auth) {
        String email = auth.getName();
        Long userId = userService.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found")).getId();
        return service.createSession(userId, request);
    }

    @GetMapping
    public List<SessionResponse> list(Authentication auth) {
        String email = auth.getName();
        Long userId = userService.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found")).getId();
        return service.listSessions(userId);
    }

    @GetMapping("/{id}")
    public SessionResponse get(@PathVariable Long id, Authentication auth) {
        String email = auth.getName();
        Long userId = userService.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found")).getId();
        return service.getSession(userId, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id, Authentication auth) {
        String email = auth.getName();
        Long userId = userService.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found")).getId();
        service.deleteSession(userId, id);
    }
}
