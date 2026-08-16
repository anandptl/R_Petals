package as.r_petals.service;

import as.r_petals.dto.auth.LoginResponse;
import as.r_petals.dto.auth.RefreshResult;
import as.r_petals.dto.common.ApiResponse;
import as.r_petals.dto.user.UserResponse;
import as.r_petals.entities.RefreshToken;
import as.r_petals.entities.RevokedToken;
import as.r_petals.entities.Users;
import as.r_petals.enums.OtpType;
import as.r_petals.enums.Role;
import as.r_petals.exception.BadRequestException;
import as.r_petals.repository.RefreshTokenRepository;
import as.r_petals.repository.RevokedTokenRepository;
import as.r_petals.util.JwtUtil;
import as.r_petals.util.RefreshTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Service
public class AuthService {

    // USER: 45 days, ADMIN/SHOPKEEPER: 1 day
    private static final long USER_REFRESH_TOKEN_SECONDS = 45L * 24 * 60 * 60;
    private static final long ADMIN_SHOPKEEPER_REFRESH_TOKEN_SECONDS = 24L * 60 * 60;

    @Autowired
    private OtpService otpService;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private RefreshTokenUtil refreshTokenUtil;
    @Autowired
    private SmsService smsService;
    @Autowired
    private RevokedTokenRepository revokedTokenRepository;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    public ApiResponse<String> sendOtp(String mobileNumber) {
        String otp = otpService.generateOtp(mobileNumber, OtpType.MOBILE);

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

    public LoginResult login(String mobileNumber, String otp) {
        if (!otpService.verifyOtp(mobileNumber, otp, OtpType.MOBILE)) {
            throw new BadRequestException("Invalid or expired OTP");
        }

        Users user = userService.findByMobileNumber(mobileNumber)
                .orElseGet(() -> userService.createUser(mobileNumber));

        if (!user.isActive()) {
            throw new BadRequestException("Account is blocked");
        }

        user.setVerified(true);
        user.setUpdatedAt(LocalDateTime.now());
        user = userService.save(user);

        String accessToken = jwtUtil.generateToken(
                user.getMobileNumber(),
                user.getRole().name()
        );

        RefreshTokenData refreshTokenData = createRefreshToken(user);

        LoginResponse loginResponse = new LoginResponse(
                accessToken,
                user.getRole(),
                new UserResponse(user)
        );

        return new LoginResult(
                loginResponse,
                refreshTokenData.rawToken(),
                refreshTokenData.maxAgeSeconds()
        );
    }

    public RefreshResult refresh(String rawRefreshToken) {
        if (rawRefreshToken == null || rawRefreshToken.isBlank()) {
            throw new BadRequestException("Refresh token is required");
        }

        String tokenHash = refreshTokenUtil.hash(rawRefreshToken);
        Optional<RefreshToken> optional = refreshTokenRepository.findById(tokenHash);

        if (optional.isEmpty()) {
            throw new BadRequestException("Invalid refresh token");
        }

        RefreshToken current = optional.get();

        if (current.isRevoked() || current.getExpiresAt() == null || current.getExpiresAt().isBefore(Instant.now())) {
            throw new BadRequestException("Refresh token has expired or been revoked");
        }

        Users user = userService.findByMobileNumber(current.getMobileNumber())
                .orElseThrow(() -> new BadRequestException("User not found"));

        if (!user.isActive()) {
            throw new BadRequestException("Account is blocked");
        }

        // Refresh-token rotation: old token becomes unusable immediately.
        current.setRevoked(true);
        current.setRevokedAt(Instant.now());
        refreshTokenRepository.save(current);

        String accessToken = jwtUtil.generateToken(
                user.getMobileNumber(),
                user.getRole().name()
        );

        RefreshTokenData newRefreshToken = createRefreshToken(user);

        LoginResponse loginResponse = new LoginResponse(
                accessToken,
                user.getRole(),
                new UserResponse(user)
        );

        return new RefreshResult(
                loginResponse,
                newRefreshToken.rawToken(),
                newRefreshToken.maxAgeSeconds()
        );
    }

    public ApiResponse<String> logout(String accessToken, String rawRefreshToken) {
        // Revoke the current access JWT if it is still parseable.
        if (accessToken != null && !accessToken.isBlank()) {
            try {
                String jti = jwtUtil.extractJti(accessToken);
                Date expiration = jwtUtil.extractExpiration(accessToken);

                if (jti != null && expiration != null && !revokedTokenRepository.existsById(jti)) {
                    revokedTokenRepository.save(
                            new RevokedToken(jti, expiration.toInstant())
                    );
                }
            } catch (Exception ignored) {
                // An already-expired/invalid access token does not prevent refresh-token logout.
            }
        }

        // Revoke refresh token server-side. Only its hash is stored in MongoDB.
        if (rawRefreshToken != null && !rawRefreshToken.isBlank()) {
            String tokenHash = refreshTokenUtil.hash(rawRefreshToken);
            refreshTokenRepository.findById(tokenHash).ifPresent(token -> {
                if (!token.isRevoked()) {
                    token.setRevoked(true);
                    token.setRevokedAt(Instant.now());
                    refreshTokenRepository.save(token);
                }
            });
        }

        return ApiResponse.success("Logout successful");
    }

    private RefreshTokenData createRefreshToken(Users user) {
        String rawToken = refreshTokenUtil.generateToken();
        String tokenHash = refreshTokenUtil.hash(rawToken);

        long maxAgeSeconds = refreshTokenLifetimeSeconds(user.getRole());
        Instant expiresAt = Instant.now().plusSeconds(maxAgeSeconds);

        RefreshToken refreshToken = new RefreshToken(
                tokenHash,
                user.getId(),
                user.getMobileNumber(),
                user.getRole(),
                expiresAt,
                false,
                null,
                Instant.now()
        );

        refreshTokenRepository.save(refreshToken);

        return new RefreshTokenData(rawToken, maxAgeSeconds);
    }

    public long refreshTokenLifetimeSeconds(Role role) {
        return role == Role.USER
                ? USER_REFRESH_TOKEN_SECONDS
                : ADMIN_SHOPKEEPER_REFRESH_TOKEN_SECONDS;
    }

    public record LoginResult(
            LoginResponse loginResponse,
            String refreshToken,
            long refreshTokenMaxAgeSeconds
    ) {}

    private record RefreshTokenData(
            String rawToken,
            long maxAgeSeconds
    ) {}
}
