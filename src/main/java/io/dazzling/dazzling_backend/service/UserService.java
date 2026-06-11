package io.dazzling.dazzling_backend.service;

import io.dazzling.dazzling_backend.entity.User;
import io.dazzling.dazzling_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User saveIfNotExists(
            String keycloakId,
            String email,
            String name) {
        return userRepository
                .findByEmail(email)
                .orElseGet(() -> {
                    User user = User.builder()
                            .keycloakId(keycloakId)
                            .email(email)
                            .fullName(name)
                            .role("USER")
                            .build();
                    return userRepository.save(user);
                });
    }
}
