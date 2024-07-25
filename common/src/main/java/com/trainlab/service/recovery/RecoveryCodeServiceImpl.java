package com.trainlab.service.recovery;

import com.trainlab.dto.UserPageDto;
import com.trainlab.dto.recovery.EmailRequestDto;
import com.trainlab.dto.recovery.RecoveryCodeDto;
import com.trainlab.exception.recovery.IllegalRecoveryCodeException;
import com.trainlab.exception.recovery.RateLimitExceededException;
import com.trainlab.exception.recovery.RecoveryCodeExpiredException;
import com.trainlab.mapper.UserMapper;
import com.trainlab.model.User;
import com.trainlab.model.recovery.RecoveryCode;
import com.trainlab.repository.recovery.RecoveryCodeRepository;
import com.trainlab.repository.UserRepository;
import com.trainlab.service.email.EmailService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecoveryCodeServiceImpl implements RecoveryCodeService {

    private static final Integer CODE_TIME_TO_LIVE = 5;
    private final RecoveryCodeRepository recoveryCodeRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final UserMapper userMapper;

    @Override
    public void resetPassword(EmailRequestDto emailRequestDto) {
        User user = userRepository.findByAuthenticationInfoEmailAndIsDeletedFalse(emailRequestDto.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("User could not be found"));

        Optional<RecoveryCode> code = recoveryCodeRepository.findRecoveryCodeByUser(user);
        if (code.isPresent()) {
            RecoveryCode oldRecoveryCode = code.get();

            boolean isExpired = oldRecoveryCode
                    .getExpiredAt()
                    .isBefore(OffsetDateTime.now(ZoneId.of("Europe/Minsk")));
            System.out.println(OffsetDateTime.now(ZoneId.of("Europe/Minsk")));
            System.out.println(oldRecoveryCode.getExpiredAt());
            if (isExpired) {
                recoveryCodeRepository.delete(oldRecoveryCode);
                createAndSendRecoveryCode(user);
            } else {
                LocalTime localTime = getTimeUntilNextRequest(oldRecoveryCode.getExpiredAt());
                String timeUntilNextRequest = String.format("%s:%s", localTime.getMinute(), localTime.getSecond());
                throw new RateLimitExceededException("Too many code recovery requests. Please, check under " + timeUntilNextRequest);
            }
        } else
            createAndSendRecoveryCode(user);
    }

    @Override
    public UserPageDto verifyCode(RecoveryCodeDto requestRecoveryCode) {
        User user = userRepository.findByAuthenticationInfoEmail(requestRecoveryCode.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("User couldn't be found"));

        RecoveryCode recoveryCode = recoveryCodeRepository.findRecoveryCodeByUser(user)
                .orElseThrow(() -> new IllegalRecoveryCodeException("Recovery code couldn't be found"));

        if (!recoveryCode.getCode().equals(requestRecoveryCode.getCode()))
            throw new IllegalRecoveryCodeException("Illegal recovery code");

        if (recoveryCode.getExpiredAt().isBefore(OffsetDateTime.now(ZoneId.of("Europe/Minsk")))){
            recoveryCodeRepository.delete(recoveryCode);
            throw new RecoveryCodeExpiredException("Recovery code has expired");
        }

        recoveryCodeRepository.delete(recoveryCode);

        return userMapper.toUserPageDto(user);
    }

    private void createAndSendRecoveryCode(User user) {
        String toAddress = user.getAuthenticationInfo().getEmail();
        String randomCode = RandomStringUtils.randomNumeric(10);

        RecoveryCode recoveryCode = RecoveryCode.builder()
                .code(randomCode)
                .expiredAt(OffsetDateTime.now(ZoneId.of("Europe/Minsk")).plusMinutes(CODE_TIME_TO_LIVE))
                .build();
        recoveryCode.setUser(user);

        recoveryCodeRepository.saveAndFlush(recoveryCode);
        emailService.sendNewPassword(toAddress, randomCode);
    }

    private LocalTime getTimeUntilNextRequest(OffsetDateTime expiredAt) {
        OffsetDateTime timeUntilExpiration = expiredAt
                .minusSeconds(OffsetDateTime.now(ZoneId.of("Europe/Minsk")).toEpochSecond());
        return timeUntilExpiration.toLocalTime();
    }
}