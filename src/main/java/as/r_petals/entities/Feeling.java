package as.r_petals.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "feelings")
public class Feeling {

    @Id
    private String id;

    private String feelingName;

    private String feelingImage;

    private boolean active = true;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public Feeling() {
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