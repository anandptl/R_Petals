package as.r_petals.dto.occasions;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class OccasionRequest {

    @NotBlank(message = "Occasion name is required")
    private String occasionName;

    private LocalDateTime occasionDate;

    public OccasionRequest() {
    }

    public String getOccasionName() {
        return occasionName;
    }

    public void setOccasionName(String occasionName) {
        this.occasionName = occasionName;
    }

    public LocalDateTime getOccasionDate() {
        return occasionDate;
    }

    public void setOccasionDate(LocalDateTime occasionDate) {
        this.occasionDate = occasionDate;
    }
}