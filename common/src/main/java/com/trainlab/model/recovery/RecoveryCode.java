package com.trainlab.model.recovery;

import com.trainlab.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.concurrent.TimeUnit;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(schema = "public", name = "recovery_code")
public class RecoveryCode {

    public final static Integer TIME_TO_LIVE = 5;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "created_at", nullable = false)
    @Builder.Default
    private OffsetDateTime createdAt = OffsetDateTime.now(ZoneId.of("Europe/Minsk"));

    @Column(name = "expired_at", nullable = false)
    private OffsetDateTime expiredAt;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}