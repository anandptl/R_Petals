package as.r_petals.controller;

import as.r_petals.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/send-otp")
    public Map<String, String> sendOtp(@RequestParam String mobileNumber) {

        if (mobileNumber == null || !mobileNumber.matches("^[6-9]\\d{9}$")) {

            return Map.of(
                    "success", "false",
                    "message",
                    "Invalid mobile number"
            );
        }
        return authService.sendOtp(mobileNumber);
    }

    @PostMapping("/verify-otp")
    public Map<String, Object> verifyOtp(
            @RequestParam String mobileNumber,
            @RequestParam String otp) {

        return authService.verifyOtp(mobileNumber, otp);
    }

}