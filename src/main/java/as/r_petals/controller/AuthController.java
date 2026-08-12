package as.r_petals.controller;

import as.r_petals.dto.auth.LoginResponse;
import as.r_petals.dto.auth.SendOtpRequest;
import as.r_petals.dto.auth.VerifyOtpRequest;
import as.r_petals.dto.common.ApiResponse;
import as.r_petals.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/send-otp")
    public ResponseEntity<ApiResponse<String>> sendOtp(
            @Valid @RequestBody SendOtpRequest request) {

        return ResponseEntity.ok(authService.sendOtp(request.getMobileNumber()));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<LoginResponse>> verifyOtp(
            @Valid @RequestBody VerifyOtpRequest request) {

        return ResponseEntity.ok(
                authService.verifyOtp(request.getMobileNumber(),
                        request.getOtp()
                )
        );
    }

}