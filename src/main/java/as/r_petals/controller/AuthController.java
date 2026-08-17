package as.r_petals.controller;

import as.r_petals.dto.auth.LoginResponse;
import as.r_petals.dto.auth.RefreshResult;
import as.r_petals.dto.auth.SendOtpRequest;
import as.r_petals.dto.auth.VerifyOtpRequest;
import as.r_petals.dto.common.ApiResponse;
import as.r_petals.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    public static final String REFRESH_COOKIE_NAME = "rpetals_refresh_token";

    @Autowired
    private AuthService authService;

    @Value("${auth.cookie.secure:false}")
    private boolean secureCookie;

    @Value("${auth.cookie.same-site:Lax}")
    private String sameSite;

    @PostMapping("/send-otp")
    public ResponseEntity<ApiResponse<String>> sendOtp(
            @Valid @RequestBody SendOtpRequest request) {

        return ResponseEntity.ok(authService.sendOtp(request.getMobileNumber()));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse<LoginResponse>> verifyOtp(
            @Valid @RequestBody VerifyOtpRequest request,
            HttpServletResponse response) {

        AuthService.LoginResult result = authService.login(
                request.getMobileNumber(),
                request.getOtp()
        );

        addRefreshCookie(response, result.refreshToken(), result.refreshTokenMaxAgeSeconds());

        return ResponseEntity.ok(
                ApiResponse.success("Login successful", result.loginResponse())
        );
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<LoginResponse>> refresh(
            @CookieValue(value = REFRESH_COOKIE_NAME, required = false) String refreshToken,
            HttpServletResponse response) {

        RefreshResult result = authService.refresh(refreshToken);

        // Rotation: replace old refresh cookie with the new one.
        addRefreshCookie(response, result.getRefreshToken(), result.getRefreshTokenMaxAgeSeconds());

        return ResponseEntity.ok(
                ApiResponse.success("Token refreshed successfully", result.getLoginResponse())
        );
    }

    private void addRefreshCookie(
            HttpServletResponse response,
            String refreshToken,
            long maxAgeSeconds) {

        ResponseCookie.ResponseCookieBuilder builder = ResponseCookie.from(REFRESH_COOKIE_NAME, refreshToken)
                .httpOnly(true)
                .secure(secureCookie)
                .sameSite(sameSite)
                .path("/");

        String role = null;
        if (refreshToken != null) {
            role = authService.getRoleFromRefreshToken(refreshToken);
        }

        if ("USER".equals(role)) {
            builder.maxAge(maxAgeSeconds);
        }

        ResponseCookie cookie = builder.build();

        response.addHeader("Set-Cookie", cookie.toString());
    }
}
