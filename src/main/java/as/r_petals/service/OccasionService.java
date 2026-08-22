package as.r_petals.service;

import as.r_petals.dto.occasions.OccasionStatsResponse;
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

    //  Visible at home page of user
    public List<Occasion> getVisibleOccasions() {

        LocalDateTime now = LocalDateTime.now();

        return occasionRepository.findAll().stream()
                .filter(occasion -> {

                    // 1. Active nahi hai -> hidden
                    if (!occasion.isActive()) {
                        return false;
                    }
                    // 2. Active hai + date nahi hai -> always visible
                    if (occasion.getOccasionDate() == null) {
                        return true;
                    }

                    LocalDateTime publishAt = occasion.getOccasionDate().minusDays(15);

                    LocalDateTime endAt = occasion.getOccasionDate().plusDays(1);

                    return !now.isBefore(publishAt)
                            && now.isBefore(endAt);
                })
                .toList();
    }


    public OccasionStatsResponse getOccasionStats() {

        LocalDateTime now = LocalDateTime.now();

        List<Occasion> occasions = occasionRepository.findAll();

        long totalOccasions = occasions.size();

        long activeOccasions = occasions.stream()
                .filter(Occasion::isActive)
                .filter(occasion -> {

                    // No date -> Always visible
                    if (occasion.getOccasionDate() == null) {
                        return true;
                    }

                    LocalDateTime publishAt =
                            occasion.getOccasionDate().minusDays(15);

                    LocalDateTime endAt =
                            occasion.getOccasionDate().plusDays(1);

                    // Currently visible
                    return !now.isBefore(publishAt)
                            && now.isBefore(endAt);
                })
                .count();

        long upcomingOccasions = occasions.stream()
                .filter(Occasion::isActive)
                .filter(occasion -> occasion.getOccasionDate() != null)
                .filter(occasion -> {

                    LocalDateTime publishAt =
                            occasion.getOccasionDate().minusDays(15);

                    // Abhi visible nahi hai,
                    // future me 15 days window me aayega
                    return now.isBefore(publishAt);
                })
                .count();

        return new OccasionStatsResponse(
                totalOccasions,
                activeOccasions,
                upcomingOccasions
        );
    }

    //  occasion update and ..
    public Occasion updateOccasion(String id, String occasionName, LocalDateTime occasionDate, boolean active, MultipartFile newImage) {

        Occasion occasion = occasionRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Occasion not found")
        );

        if (occasionName == null || occasionName.isBlank()) {
            throw new IllegalArgumentException("Occasion name is required");
        }

        String oldPublicId = occasion.getOccasionImagePublicId();
        ImageUploadService.UploadResult newUpload = null;

        try {
            occasion.setOccasionName(occasionName.trim());
            occasion.setOccasionDate(occasionDate);
            occasion.setActive(active);

            //New image selected
            if (newImage != null && !newImage.isEmpty()) {

                newUpload = imageUploadService.uploadWithDetails(newImage, "r_petals/occasions");

                occasion.setOccasionImage(newUpload.url());
                occasion.setOccasionImagePublicId(newUpload.publicId());
            }

            occasion.setUpdatedAt(LocalDateTime.now());
            Occasion updatedOccasion = occasionRepository.save(occasion);
            /*
             * Delete OLD image only after MongoDB update succeeds
             */
            if (newUpload != null && oldPublicId != null && !oldPublicId.isBlank()) {
                try {
                    imageUploadService.deleteByPublicId(oldPublicId);
                } catch (Exception e) {
                    System.err.println("Old Cloudinary image deletion failed: " + e.getMessage());
                }
            }

            return updatedOccasion;

        } catch (Exception e) {

            if (newUpload != null) {

                try {
                    imageUploadService.deleteByPublicId(newUpload.publicId());
                } catch (Exception rollbackError) {
                    System.err.println("New image rollback failed: " + rollbackError.getMessage());
                }
            }

            throw new RuntimeException("Failed to update occasion: " + e.getMessage(), e);
        }
    }

    //  delete service
    public void deleteOccasion(String id) {

        Occasion occasion = occasionRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(
                "Occasion not found"));

        String publicId = occasion.getOccasionImagePublicId();

        // Delete MongoDB document
        occasionRepository.delete(occasion);

        // Delete Cloudinary image
        if (publicId != null && !publicId.isBlank()) {
            try {
                imageUploadService.deleteByPublicId(publicId);

            } catch (Exception e) {
                System.err.println("Cloudinary image deletion failed: " + e.getMessage());
            }
        }
    }

    //     get all occasion details
    public List<Occasion> getAllOccasions() {
        return occasionRepository.findAll();
    }
}