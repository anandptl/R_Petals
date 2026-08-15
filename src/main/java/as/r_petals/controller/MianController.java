package as.r_petals.controller;

import as.r_petals.dto.common.ApiResponse;
import as.r_petals.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class MianController {
    @Autowired
    private AuthService authService;

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(
            @RequestHeader(value = "Authorization", required = false)
            String authorization) {

        if (authorization == null || !authorization.startsWith("Bearer ")) {

            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Bearer token is required"));
        }

        String token = authorization.substring(7);

        return ResponseEntity.ok(authService.logout(token));
    }
}
