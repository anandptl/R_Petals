package as.r_petals.service;

import as.r_petals.dto.auth.LoginResponse;
import as.r_petals.dto.common.ApiResponse;
import as.r_petals.dto.user.UserResponse;
import as.r_petals.entities.Users;
import as.r_petals.enums.OtpType;
import as.r_petals.exception.BadRequestException;
import as.r_petals.util.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

    public ApiResponse<String> sendOtp(String mobileNumber) {
        String otp = otpService.generateOtp(mobileNumber, OtpType.MOBILE);

        // OTP console mein hamesha dikhe
        System.out.println("=================================");
        System.out.println("Mobile Number : " + mobileNumber);
        System.out.println("OTP           : " + otp);
        System.out.println("=================================");

        try {
            smsService.sendOtps(mobileNumber, otp);
        } catch (RuntimeException ex) {
            otpService.invalidate(mobileNumber, OtpType.MOBILE);
            throw ex;
        }
        return ApiResponse.success("OTP sent successfully");
    }


    public ApiResponse<LoginResponse> verifyOtp(String mobileNumber, String otp) {

        if (!otpService.verifyOtp(mobileNumber, otp, OtpType.MOBILE)) {
            throw new BadRequestException("Invalid or expired OTP");
        }

        Users user = userService.findByMobileNumber(mobileNumber)
                .orElseGet(() -> userService.createUser(mobileNumber));

        user.setVerified(true);
        user.setUpdatedAt(LocalDateTime.now());
        user = userService.save(user);

        String token = jwtUtil.generateToken(
                user.getMobileNumber(),
                user.getRole().name()
        );

        LoginResponse loginResponse = new LoginResponse(
                token,
                user.getRole(),
                new UserResponse(user)
        );

        return ApiResponse.success("Login successful", loginResponse);
    }
}