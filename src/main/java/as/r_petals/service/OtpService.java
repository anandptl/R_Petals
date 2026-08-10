package as.r_petals.service;

import as.r_petals.config.PasswordConfig;
import as.r_petals.entities.Otp;
import as.r_petals.enums.OtpType;
import as.r_petals.exception.BadRequestException;
import as.r_petals.repository.OtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
public class OtpService {

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private PasswordConfig passwordConfig;

    private final PasswordEncoder passwordEncoder;

    public OtpService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    private static final int OTP_LENGTH = 6;
    private static final int OTP_EXPIRY_MINUTES = 5;
    private static final int MAX_ATTEMPTS = 5;
    private static final int MAX_REQUESTS_PER_HOUR = 5;
    private static final int RESEND_COOLDOWN_SECONDS = 60;

    private final SecureRandom secureRandom = new SecureRandom();

    // Generate OTP
    public synchronized String generateOtp(String identifier, OtpType type) {
        String normalizedIdentifier = normalize(identifier, type);
        LocalDateTime now = LocalDateTime.now();

        Otp otpEntity = otpRepository
                .findByIdentifierAndType(normalizedIdentifier, type)
                .orElseGet(Otp::new);

        if (otpEntity.getIdentifier() == null) {
            otpEntity.setIdentifier(normalizedIdentifier);
            otpEntity.setType(type);
            otpEntity.setWindowStart(now);
            otpEntity.setRequestCount(0);
        }

        if (otpEntity.getCreatedAt() != null &&
                otpEntity.getCreatedAt().plusSeconds(RESEND_COOLDOWN_SECONDS).isAfter(now)) {
            throw new BadRequestException("Please wait before requesting another OTP");
        }

        if (otpEntity.getWindowStart() == null ||
                !otpEntity.getWindowStart().plusHours(1).isAfter(now)) {
            otpEntity.setWindowStart(now);
            otpEntity.setRequestCount(0);
        }

        if (otpEntity.getRequestCount() >= MAX_REQUESTS_PER_HOUR) {
            throw new BadRequestException("Too many OTP requests. Please try again later");
        }

        int number = secureRandom.nextInt(1_000_000);
        String otp = String.format("%0" + OTP_LENGTH + "d", number);

        otpEntity.setOtp(passwordEncoder.encode(otp));
        otpEntity.setCreatedAt(now);
        otpEntity.setExpiryTime(now.plusMinutes(OTP_EXPIRY_MINUTES));
        otpEntity.setAttempts(0);
        otpEntity.setRequestCount(otpEntity.getRequestCount() + 1);
        otpRepository.save(otpEntity);

        return otp;
    }

    // Verify OTP
    public boolean verifyOtp(String identifier, String otp, OtpType type) {
        String normalizedIdentifier = normalize(identifier, type);
        Otp entity = otpRepository
                .findByIdentifierAndType(normalizedIdentifier, type)
                .orElse(null);

        if (entity == null || entity.getOtp() == null) {
            return false;
        }

        if (entity.getExpiryTime() == null || !LocalDateTime.now().isBefore(entity.getExpiryTime())) {
            invalidate(normalizedIdentifier, type);
            return false;
        }

        if (entity.getAttempts() >= MAX_ATTEMPTS) {
            invalidate(normalizedIdentifier, type);
            return false;
        }

        if (!passwordEncoder.matches(otp, entity.getOtp())) {
            entity.setAttempts(entity.getAttempts() + 1);
            if (entity.getAttempts() >= MAX_ATTEMPTS) {
                entity.setOtp(null);
            }
            otpRepository.save(entity);
            return false;
        }

        // Keep requestCount/windowStart so the hourly rate limit cannot be reset by a successful OTP.
        entity.setOtp(null);
        entity.setAttempts(MAX_ATTEMPTS);
        entity.setExpiryTime(LocalDateTime.now());
        otpRepository.save(entity);
        return true;
    }

    public void invalidate(String identifier, OtpType type) {
        String normalizedIdentifier = normalize(identifier, type);
        otpRepository.findByIdentifierAndType(normalizedIdentifier, type)
                .ifPresent(entity -> {
                    entity.setOtp(null);
                    entity.setAttempts(MAX_ATTEMPTS);
                    entity.setExpiryTime(LocalDateTime.now());
                    entity.setCreatedAt(null);
                    otpRepository.save(entity);
                });
    }

    private String normalize(String identifier, OtpType type) {
        if (identifier == null || identifier.isBlank()) {
            throw new BadRequestException("OTP identifier is required");
        }
        String value = identifier.trim();
        return type == OtpType.EMAIL ? value.toLowerCase() : value;
    }
}