package as.r_petals.dto.user;

import jakarta.validation.constraints.Size;

public class UpdateUserRequest {
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String fullName;
    public UpdateUserRequest() {}
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
}
