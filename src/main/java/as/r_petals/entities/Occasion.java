package as.r_petals.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "occasions")
public class Occasion {

    @Id
    private String id;

    private String occasionName;
    private String occasionImage;
    private String occasionImagePublicId;
    private LocalDateTime occasionDate;
    private boolean active = true;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Occasion(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOccasionName() {
        return occasionName;
    }

    public void setOccasionName(String occasionName) {
        this.occasionName = occasionName;
    }

    public String getOccasionImage() {
        return occasionImage;
    }

    public void setOccasionImage(String occasionImage) {
        this.occasionImage = occasionImage;
    }

    public String getOccasionImagePublicId() { return occasionImagePublicId; }

    public void setOccasionImagePublicId(String occasionImagePublicId) { this.occasionImagePublicId = occasionImagePublicId;}

    public LocalDateTime getOccasionDate() {
        return occasionDate;
    }

    public void setOccasionDate(LocalDateTime occasionDate) {
        this.occasionDate = occasionDate;
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