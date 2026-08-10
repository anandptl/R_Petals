package as.r_petals.controller;

import as.r_petals.dto.auth.SendEmailOtpRequest;
import as.r_petals.dto.auth.VerifyEmailOtpRequest;
import as.r_petals.dto.common.ApiResponse;
import as.r_petals.dto.user.UpdateUserRequest;
import as.r_petals.dto.user.UserResponse;
import as.r_petals.service.OtpService;
import as.r_petals.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponse<Void>> sendEmailOtp(
            @Valid @RequestBody SendEmailOtpRequest request) {
        return ResponseEntity.ok(userService.sendEmailOtp(request.getName(), request.getEmail()));
    }

    @PostMapping("/verify-emailOtp")
    public ResponseEntity<ApiResponse<Void>> verifyEmailOtp(
            @Valid @RequestBody VerifyEmailOtpRequest request) {
        return ResponseEntity.ok(userService.verifyEmailOtp(request.getName(), request.getEmail(), request.getOtp()));
    }
}
