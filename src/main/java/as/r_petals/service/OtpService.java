package as.r_petals.service;

import as.r_petals.entities.Otp;
import as.r_petals.enums.OtpType;
import as.r_petals.repository.OtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
public class OtpService {

    @Autowired
    private OtpRepository otpRepository;


    private static final int OTP_LENGTH = 6;

    private static final int OTP_EXPIRY_MINUTES = 5;

    private static final int MAX_ATTEMPTS = 5;

    private static final int MAX_REQUESTS_PER_HOUR = 5;

    private static final int RESEND_COOLDOWN_SECONDS = 60;


    private final SecureRandom secureRandom = new SecureRandom();


    // ============================================================
    // Generate OTP
    // ============================================================

    public String generateOtp(String identifier, OtpType type) {

        LocalDateTime now = LocalDateTime.now();

        Otp existingOtp = otpRepository
                .findByIdentifierAndType(identifier, type)
                .orElse(null);

        // Rate limit

        if (existingOtp != null) {

            // 60 second resend cooldown
            if (existingOtp.getCreatedAt() != null &&
                    existingOtp.getCreatedAt()
                            .plusSeconds(RESEND_COOLDOWN_SECONDS)
                            .isAfter(now)) {

                throw new RuntimeException(
                        "Please wait before requesting another OTP."
                );
            }


            // Reset hourly window
            if (existingOtp.getWindowStart() == null ||
                    existingOtp.getWindowStart()
                            .plusHours(1)
                            .isBefore(now)) {

                existingOtp.setWindowStart(now);
                existingOtp.setRequestCount(0);
            }


            // Maximum 5 OTP requests per hour
            if (existingOtp.getRequestCount()
                    >= MAX_REQUESTS_PER_HOUR) {

                throw new RuntimeException(
                        "Too many OTP requests. Please try again later."
                );
            }
        }

        // Generate secure OTP

        int number = secureRandom.nextInt(1_000_000);

        String otp = String.format(
                "%0" + OTP_LENGTH + "d",
                number
        );

        // Create / update OTP entity

        if (existingOtp == null) {

            existingOtp = new Otp();

            existingOtp.setIdentifier(identifier);
            existingOtp.setType(type);

            existingOtp.setRequestCount(0);
            existingOtp.setWindowStart(now);
        }


        existingOtp.setOtp(hashOtp(otp));

        existingOtp.setCreatedAt(now);

        existingOtp.setExpiryTime(
                now.plusMinutes(OTP_EXPIRY_MINUTES)
        );

        existingOtp.setAttempts(0);

        existingOtp.setRequestCount(
                existingOtp.getRequestCount() + 1
        );


        otpRepository.save(existingOtp);


        return otp;
    }

    // Verify OTP

    public boolean verifyOtp(
            String identifier,
            String otp,
            OtpType type) {


        Otp entity = otpRepository
                .findByIdentifierAndType(identifier, type)
                .orElse(null);


        if (entity == null) {
            return false;
        }

        // Expired

        if (LocalDateTime.now()
                .isAfter(entity.getExpiryTime())) {

            otpRepository.delete(entity);

            return false;
        }

        // Maximum attempts

        if (entity.getAttempts() >= MAX_ATTEMPTS) {

            otpRepository.delete(entity);

            return false;
        }

        // Compare hashed OTP

        String hashedOtp = hashOtp(otp);


        if (!MessageDigest.isEqual(
                hashedOtp.getBytes(StandardCharsets.UTF_8),
                entity.getOtp()
                        .getBytes(StandardCharsets.UTF_8)
        )) {

            entity.setAttempts(
                    entity.getAttempts() + 1
            );

            otpRepository.save(entity);

            return false;
        }

        // OTP correct → delete immediately

        otpRepository.delete(entity);

        return true;
    }


    // Hash OTP

    private String hashOtp(String otp) {

        try {

            MessageDigest digest =
                    MessageDigest.getInstance("SHA-256");

            byte[] hash =
                    digest.digest(
                            otp.getBytes(StandardCharsets.UTF_8)
                    );

            StringBuilder hexString =
                    new StringBuilder();

            for (byte b : hash) {

                String hex =
                        Integer.toHexString(
                                0xff & b
                        );

                if (hex.length() == 1) {
                    hexString.append('0');
                }

                hexString.append(hex);
            }

            return hexString.toString();

        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to generate OTP hash"
            );
        }
    }
}