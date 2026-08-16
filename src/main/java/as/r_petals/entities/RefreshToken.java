package as.r_petals.entities;

import as.r_petals.enums.Role;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "refresh_tokens")
public class RefreshToken {

    // SHA-256 hash of the real refresh token. Raw token is never stored in MongoDB.
    @Id
    private String tokenHash;

    @Indexed
    private String userId;

    @Indexed
    private String mobileNumber;

    private Role role;

    @Indexed(expireAfter = "0s")
    private Instant expiresAt;

    private boolean revoked;

    private Instant revokedAt;

    @CreatedDate
    private Instant createdAt;

    public RefreshToken() {
    }

    public RefreshToken(String tokenHash, String userId, String mobileNumber,
                        Role role, Instant expiresAt, boolean revoked,
                        Instant revokedAt, Instant createdAt) {
        this.tokenHash = tokenHash;
        this.userId = userId;
        this.mobileNumber = mobileNumber;
        this.role = role;
        this.expiresAt = expiresAt;
        this.revoked = revoked;
        this.revokedAt = revokedAt;
        this.createdAt = createdAt;
    }

    public String getTokenHash() {
        return tokenHash;
    }

    public void setTokenHash(String tokenHash) {
        this.tokenHash = tokenHash;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }

    public Instant getRevokedAt() {
        return revokedAt;
    }

    public void setRevokedAt(Instant revokedAt) {
        this.revokedAt = revokedAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
