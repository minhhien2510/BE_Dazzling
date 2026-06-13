package io.dazzling.dazzling_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;

@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String keycloakId;

    @Column(nullable = false, unique = true)
    private String email;

    @JsonIgnore
    private String password;

    private String fullName;

    private String avatarUrl;

    private String role;

    @CreatedDate
    @Column(nullable = true, updatable = false, columnDefinition = "datetime(6)")
    private Instant createdAt;
}
