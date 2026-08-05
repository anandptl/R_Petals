package as.r_petals.controller;

import as.r_petals.service.OtpService;
import as.r_petals.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private OtpService otpService;

    @Autowired
    private UserService userService;

    @PostMapping("/send-emailOtp")
    public Map<String,Object> sendEmailOtp(
            @RequestParam String userId,
            @RequestParam String name,
            @RequestParam String email){

        return userService.sendEmailOtp(userId, name, email);

    }

    @PostMapping("/verify-emailOtp")
    public Map<String,Object> verifyEmailOtp(
            @RequestParam String userId,
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String otp){

        return userService.verifyEmailOtp(userId, name, email, otp );

    }
}
