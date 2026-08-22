package as.r_petals.dto.feelings;

import jakarta.validation.constraints.NotBlank;

public class FeelingRequest {

    @NotBlank(message = "Feeling name is required")
    private String feelingName;

    private boolean active = true;

    public FeelingRequest() {
    }

    public String getFeelingName() {
        return feelingName;
    }

    public void setFeelingName(String feelingName) {
        this.feelingName = feelingName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}