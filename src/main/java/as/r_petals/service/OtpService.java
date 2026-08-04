package as.r_petals.service;

import as.r_petals.entities.Otp;
import as.r_petals.repository.OtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class OtpService {

    @Autowired
    private OtpRepository otpRepository;

    // Generate 6 Digit OTP
    public String generateOtp(String mobileNumber) {

        Random random = new Random();

        String otp = String.format("%06d", random.nextInt(999999));

        otpRepository.deleteByMobileNumber(mobileNumber);

        Otp otpEntity = new Otp();

        otpEntity.setMobileNumber(mobileNumber);
        otpEntity.setOtp(otp);
        otpEntity.setCreatedAt(LocalDateTime.now());
        otpEntity.setExpiryTime(LocalDateTime.now().plusMinutes(5));

        otpRepository.save(otpEntity);

        return otp;
    }

    // Verify OTP
    public boolean verifyOtp(String mobileNumber, String otp) {

        Otp otpEntity = otpRepository
                .findByMobileNumber(mobileNumber)
                .orElse(null);

        if (otpEntity == null) {
            return false;
        }

        if (LocalDateTime.now().isAfter(otpEntity.getExpiryTime())) {

            otpRepository.delete(otpEntity);

            return false;
        }

        if (!otpEntity.getOtp().equals(otp)) {
            return false;
        }

        otpRepository.delete(otpEntity);

        return true;
    }

}