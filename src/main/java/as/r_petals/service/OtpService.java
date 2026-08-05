package as.r_petals.service;

import as.r_petals.entities.Otp;
import as.r_petals.entities.Users;
import as.r_petals.enums.OtpType;
import as.r_petals.repository.OtpRepository;
import as.r_petals.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Random;

@Service
public class OtpService {

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private UserRepository userRepository;

    // Generate 6 Digit OTP
    public String generateOtp(String identifier, OtpType type) {

        Random random = new Random();

        String otp = String.format("%06d", random.nextInt(999999));

        otpRepository.deleteByIdentifierAndType(identifier, type);

        Otp entity = new Otp();

        entity.setIdentifier(identifier);
        entity.setType(type);
        entity.setOtp(otp);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setExpiryTime(LocalDateTime.now().plusMinutes(5));

        otpRepository.save(entity);

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

        if (LocalDateTime.now().isAfter(entity.getExpiryTime())) {

            otpRepository.delete(entity);

            return false;
        }

        if (!entity.getOtp().equals(otp)) {

            return false;
        }

        otpRepository.delete(entity);

        return true;
    }

}