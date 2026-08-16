package as.r_petals.util;

import as.r_petals.enums.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    // Access token is intentionally short-lived for all roles.
    private static final long ACCESS_TOKEN_EXPIRATION = 1000L * 60 * 15; // 15 minutes

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String mobileNumber, String role) {
        Date issuedAt = new Date();
        Date expirationDate = new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION);

        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(mobileNumber)
                .claim("role", role)
                .issuedAt(issuedAt)
                .expiration(expirationDate)
                .signWith(getKey(), Jwts.SIG.HS256)
                .compact();
    }

    public String extractMobileNumber(String token) {
        return parseClaims(token).getSubject();
    }

    public String extractRole(String token) {
        return parseClaims(token).get("role", String.class);
    }

    public String extractJti(String token) {
        return parseClaims(token).getId();
    }

    public Date extractExpiration(String token) {
        return parseClaims(token).getExpiration();
    }

    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
