package as.r_petals.dto.auth;

public class RefreshResult {

    private final LoginResponse loginResponse;
    private final String refreshToken;
    private final long refreshTokenMaxAgeSeconds;

    public RefreshResult(LoginResponse loginResponse, String refreshToken, long refreshTokenMaxAgeSeconds) {
        this.loginResponse = loginResponse;
        this.refreshToken = refreshToken;
        this.refreshTokenMaxAgeSeconds = refreshTokenMaxAgeSeconds;
    }

    public LoginResponse getLoginResponse() {
        return loginResponse;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public long getRefreshTokenMaxAgeSeconds() {
        return refreshTokenMaxAgeSeconds;
    }
}
