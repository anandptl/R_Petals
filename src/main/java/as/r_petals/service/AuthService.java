package as.r_petals.service;

import as.r_petals.entities.Users;
import as.r_petals.enums.Role;
import as.r_petals.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    @Autowired
    private OtpService otpService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private SmsService smsService;

    // Send OTP
    public Map<String, String> sendOtp(String mobileNumber) {

        String otp = otpService.generateOtp(mobileNumber);

        //send sms on mobile number
        smsService.sendOtp(mobileNumber, otp);

        Map<String, String> response = new HashMap<>();

        response.put("otp", otp);
        response.put("success", "true");
        response.put("message", "OTP Sent Successfully");

        return response;
    }

    // Verify OTP
    public Map<String, Object> verifyOtp(String mobileNumber, String otp) {

        Map<String, Object> response = new HashMap<>();

        boolean verified = otpService.verifyOtp(mobileNumber, otp);

        if (!verified) {

            response.put("success", false);
            response.put("message", "Invalid or Expired OTP");

            return response;
        }

        Users user;

        if (userService.existsByMobileNumber(mobileNumber)) {

            user = userService.findByMobileNumber(mobileNumber).orElseThrow();

            user.setVerified(true);
            user.setUpdatedAt(LocalDateTime.now());

            user = userService.save(user);
        } else {

            user = new Users();

            user.setMobileNumber(mobileNumber);
            user.setVerified(true);
            user.setRole(Role.USER);
            user.setCreatedAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());

            user = userService.save(user);
        }

        String token = jwtUtil.generateToken(user.getMobileNumber(), user.getRole().name());

        response.put("success", true);
        response.put("message", "Login Successful");
        response.put("token", token);
        response.put("role", user.getRole());
        response.put("user", user);

        return response;
    }

}