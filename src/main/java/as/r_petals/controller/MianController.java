package as.r_petals.controller;

import as.r_petals.dto.common.ApiResponse;
import as.r_petals.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static as.r_petals.controller.AuthController.REFRESH_COOKIE_NAME;

@RestController
@RequestMapping("/api")
public class MianController {

    @Autowired
    private AuthService authService;

    @Value("${auth.cookie.secure:false}")
    private boolean secureCookie;

    @Value("${auth.cookie.same-site:Lax}")
    private String sameSite;

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @CookieValue(
                    value = REFRESH_COOKIE_NAME,
                    required = false
            ) String refreshToken,
            HttpServletResponse response) {

        String accessToken = null;

        if (authorization != null && authorization.startsWith("Bearer ")) {
            accessToken = authorization.substring(7);
        }

        ApiResponse<String> result = authService.logout(accessToken, refreshToken);

        // Clear refresh-token cookie
        ResponseCookie clearCookie = ResponseCookie
                .from(REFRESH_COOKIE_NAME, "")
                .httpOnly(true)
                .secure(secureCookie)
                .sameSite(sameSite)
                .path("/")
                .maxAge(0)
                .build();

        response.addHeader(
                "Set-Cookie",
                clearCookie.toString()
        );

        return ResponseEntity.ok(result);
    }
}
