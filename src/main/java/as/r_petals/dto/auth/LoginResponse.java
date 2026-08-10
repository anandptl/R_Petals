package as.r_petals.dto.auth;

import as.r_petals.dto.user.UserResponse;
import as.r_petals.enums.Role;

public class LoginResponse {
    private String accessToken;
    private Role role;
    private UserResponse user;
    public LoginResponse() {}
    public LoginResponse(String accessToken, Role role, UserResponse user) {
        this.accessToken = accessToken;
        this.role = role;
        this.user = user;
    }
    public String getAccessToken() { return accessToken; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public UserResponse getUser() { return user; }
    public void setUser(UserResponse user) { this.user = user; }
}
