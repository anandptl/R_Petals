package as.r_petals.service;

import as.r_petals.entities.Occasion;
import as.r_petals.repository.OccasionRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OccasionService {

    private final OccasionRepository occasionRepository;
    private final ImageUploadService imageUploadService;

    public OccasionService(OccasionRepository occasionRepository, ImageUploadService imageUploadService) {
        this.occasionRepository = occasionRepository;
        this.imageUploadService = imageUploadService;
    }

    public Occasion createOccasion(String occasionName, LocalDateTime occasionDate, boolean active, MultipartFile image) {

        // VALIDATION

        if (occasionName == null || occasionName.isBlank()) {
            throw new IllegalArgumentException("Occasion name is required");
        }

        if (occasionDate == null) {
            throw new IllegalArgumentException("Occasion date is required");
        }
        if (image == null || image.isEmpty()) {
            throw new IllegalArgumentException("Occasion image is required");
        }

        // CLOUDINARY UPLOAD

        ImageUploadService.UploadResult uploadResult = imageUploadService.uploadWithDetails(image, "r_petals/occasions");

        try {
            Occasion occasion = new Occasion();

            occasion.setOccasionName(occasionName.trim());
            occasion.setOccasionImage(uploadResult.url());
            occasion.setOccasionImagePublicId(uploadResult.publicId());
            occasion.setOccasionDate(occasionDate);
            occasion.setActive(active);
            // TIMESTAMPS
            LocalDateTime now = LocalDateTime.now();
            occasion.setCreatedAt(now);
            occasion.setUpdatedAt(now);

            // SAVE MONGODB
            Occasion savedOccasion = occasionRepository.save(occasion);

            return savedOccasion;

        } catch (Exception e) {
            // ROLLBACK CLOUDINARY

            try {
                imageUploadService.deleteByPublicId(uploadResult.publicId());
            } catch (Exception deleteException) {
                System.err.println("Cloudinary rollback failed: " + deleteException.getMessage());
            }

            throw new RuntimeException("Failed to save occasion: " + e.getMessage(), e);
        }
    }

    public List<Occasion> getVisibleOccasions() {

        LocalDateTime now = LocalDateTime.now();

        return occasionRepository.findAll().stream().filter(occasion -> {

            if (!occasion.isActive()) {
                return false;
            }

            if (occasion.getOccasionDate() == null) {
                return false;
            }

            LocalDateTime publishAt = occasion.getOccasionDate().minusDays(10);
            LocalDateTime endAt = occasion.getOccasionDate().plusDays(1);
            return !now.isBefore(publishAt) && now.isBefore(endAt);
        }).toList();
    }
}