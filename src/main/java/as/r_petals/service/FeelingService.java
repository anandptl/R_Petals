package as.r_petals.service;

import as.r_petals.dto.feelings.FeelingResponse;
import as.r_petals.entities.Feeling;
import as.r_petals.repository.FeelingRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FeelingService {

    private final FeelingRepository feelingRepository;
    private final ImageUploadService imageUploadService;

    // Constructor injection for repositories and image upload service
    public FeelingService(FeelingRepository feelingRepository, ImageUploadService imageUploadService) {
        this.feelingRepository = feelingRepository;
        this.imageUploadService = imageUploadService;
    }

    // Create and save a new feeling entity with image upload to Cloudinary
    public Feeling createFeeling(String feelingName, boolean active, MultipartFile image) {
        // Validate required feeling name
        if (feelingName == null || feelingName.isBlank()) {
            throw new IllegalArgumentException("Feeling name is required");
        }

        // Validate required feeling image
        if (image == null || image.isEmpty()) {
            throw new IllegalArgumentException("Feeling image is required");
        }

        // Upload image to Cloudinary feeling folder
        ImageUploadService.UploadResult uploadResult = imageUploadService.uploadWithDetails(image, "r_petals/feelings");

        try {
            Feeling feeling = new Feeling();
            feeling.setFeelingName(feelingName.trim());
            feeling.setFeelingImage(uploadResult.url());
            feeling.setFeelingImagePublicId(uploadResult.publicId());
            feeling.setActive(active);

            // Set creation and update timestamps
            LocalDateTime now = LocalDateTime.now();
            feeling.setCreatedAt(now);
            feeling.setUpdatedAt(now);

            // Persist feeling document in repository
            return feelingRepository.save(feeling);
        } catch (Exception e) {
            // Roll back uploaded Cloudinary asset if saving document fails
            try {
                imageUploadService.deleteByPublicId(uploadResult.publicId());
            } catch (Exception deleteException) {
                System.err.println("Cloudinary rollback failed: " + deleteException.getMessage());
            }

            throw new RuntimeException("Failed to save feeling: " + e.getMessage(), e);
        }
    }

    // Retrieve all feeling records mapped to response DTOs
    public List<FeelingResponse> getAllFeelings() {
        return feelingRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // Retrieve all active feelings mapped to response DTOs
    public List<FeelingResponse> getActiveFeelings() {
        return feelingRepository.findByActiveTrue()
                .stream()
                .map(this::toResponse)
                .toList();
    }

//    get Visible
    public List<Feeling> getVisibleFeelings(){
        return feelingRepository.findByActiveTrue();
    }

    // Retrieve single feeling by ID or throw exception if not found
    public FeelingResponse getFeelingById(String id) {
        Feeling feeling = feelingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feeling not found"));

        return toResponse(feeling);
    }

    // Update feeling details and optionally replace image asset
    public Feeling updateFeeling(String id, String feelingName, boolean active, MultipartFile image) {
        Feeling feeling = feelingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feeling not found"));

        // Validate feeling name parameter
        if (feelingName == null || feelingName.isBlank()) {
            throw new IllegalArgumentException("Feeling name is required");
        }

        feeling.setFeelingName(feelingName.trim());
        feeling.setActive(active);

        // Process new image upload if provided
        if (image != null && !image.isEmpty()) {
            String oldPublicId = feeling.getfeelingImagePublicId();
            ImageUploadService.UploadResult uploadResult = imageUploadService.uploadWithDetails(image, "r_petals/feelings");

            try {
                feeling.setFeelingImage(uploadResult.url());
                feeling.setFeelingImagePublicId(uploadResult.publicId());
                feeling.setUpdatedAt(LocalDateTime.now());

                Feeling updated = feelingRepository.save(feeling);

                // Clean up previous image asset from Cloudinary
                if (oldPublicId != null && !oldPublicId.isBlank()) {
                    try {
                        imageUploadService.deleteByPublicId(oldPublicId);
                    } catch (Exception e) {
                        System.err.println("Old Cloudinary image deletion failed: " + e.getMessage());
                    }
                }

                return updated;
            } catch (Exception e) {
                // Roll back newly uploaded image on save failure
                try {
                    imageUploadService.deleteByPublicId(uploadResult.publicId());
                } catch (Exception deleteException) {
                    System.err.println("Cloudinary rollback failed: " + deleteException.getMessage());
                }

                throw new RuntimeException("Failed to update feeling: " + e.getMessage(), e);
            }
        } else {
            // Update timestamp and save when no new image is provided
            feeling.setUpdatedAt(LocalDateTime.now());
            return feelingRepository.save(feeling);
        }
    }

    // Delete feeling document and remove image asset from Cloudinary
    public void deleteFeeling(String id) {
        Feeling feeling = feelingRepository.findById(id).orElseThrow(() -> new RuntimeException("Feeling not found"));

        String publicId = feeling.getfeelingImagePublicId();

        // Delete image asset from Cloudinary if public ID exists
        if (publicId != null && !publicId.isBlank()) {
            try {
                imageUploadService.deleteByPublicId(publicId);
            } catch (Exception e) {
                throw new RuntimeException("Failed to delete feeling image from Cloudinary: " + e.getMessage(), e);
            }
        }

        // Delete database record
        feelingRepository.deleteById(id);
    }

    // Convert Feeling entity into FeelingResponse DTO
    private FeelingResponse toResponse(Feeling feeling) {
        return new FeelingResponse(
                feeling.getId(),
                feeling.getFeelingName(),
                feeling.getFeelingImage(),
                feeling.isActive(),
                feeling.getCreatedAt(),
                feeling.getUpdatedAt()
        );
    }
}