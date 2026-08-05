package as.r_petals.service;

import as.r_petals.entities.Users;
import as.r_petals.enums.OtpType;
import as.r_petals.enums.Role;
import as.r_petals.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OtpService otpService;

    @Autowired
    private EmailService emailService;

    // Find User
    public Optional<Users> findByMobileNumber(String mobileNumber) {
        return userRepository.findByMobileNumber(mobileNumber);
    }

    // Check User Exists
    public boolean existsByMobileNumber(String mobileNumber) {
        return userRepository.existsByMobileNumber(mobileNumber);
    }

    // Register New User
    public Users createUser(String mobileNumber) {

        Users user = new Users();

        user.setMobileNumber(mobileNumber);
        user.setVerified(true);
        user.setRole(Role.USER);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }

    // Update User
    public Users save(Users user) {

        user.setUpdatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }


// sending email otp
    public Map<String, Object> sendEmailOtp(String userId, String name, String email) {

        Map<String, Object> response = new HashMap<>();

        Users user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            response.put("success", false);
            response.put("message", "User not found");
            return response;
        }

        user.setEmail(email);
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);

        String otp = otpService.generateOtp(email, OtpType.EMAIL);

        emailService.sendOtp(email, name,  otp);

        response.put("success", true);
        response.put("message", "Email OTP sent successfully");

        return response;
    }

    public Map<String, Object> verifyEmailOtp(String userId,
                                              String name,
                                              String email,
                                              String otp) {

        Map<String, Object> response = new HashMap<>();

        boolean verified = otpService.verifyOtp(
                email,
                otp,
                OtpType.EMAIL
        );

        if (!verified) {

            response.put("success", false);
            response.put("message", "Invalid or Expired OTP");

            return response;
        }

        Users user = userRepository.findById(userId).orElseThrow();

        user.setVerified(true);

        user.setFullName(name);

        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);

        response.put("success", true);
        response.put("message", "Email Verified Successfully");

        return response;
    }
}