package as.r_petals.dto.feelings;

import java.time.LocalDateTime;

public class FeelingResponse {

    private String id;
    private String feelingName;
    private String feelingImage;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public FeelingResponse() {
    }

    public FeelingResponse(
            String id,
            String feelingName,
            String feelingImage,
            boolean active,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.feelingName = feelingName;
        this.feelingImage = feelingImage;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFeelingName() {
        return feelingName;
    }

    public void setFeelingName(String feelingName) {
        this.feelingName = feelingName;
    }

    public String getFeelingImage() {
        return feelingImage;
    }

    public void setFeelingImage(String feelingImage) {
        this.feelingImage = feelingImage;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}