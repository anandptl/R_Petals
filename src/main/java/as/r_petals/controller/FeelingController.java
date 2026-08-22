package as.r_petals.controller;

import as.r_petals.dto.feelings.FeelingResponse;
import as.r_petals.entities.Feeling;
import as.r_petals.service.FeelingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/feelings")
public class FeelingController {

    private final FeelingService feelingService;

    // Constructor injection for FeelingService
    public FeelingController(FeelingService feelingService) {
        this.feelingService = feelingService;
    }

    // Create and save a new feeling entity with image upload
    @PostMapping("/save")
    public ResponseEntity<?> createFeeling(
            @RequestParam("feelingName") String feelingName,
            @RequestParam("active") boolean active,
            @RequestParam("image") MultipartFile image
    ) {
        try {
            Feeling feeling = feelingService.createFeeling(feelingName, active, image);
            return ResponseEntity.ok(feeling);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    // Retrieve all active and visible feelings
    @GetMapping("/visible")
    public ResponseEntity<List<FeelingResponse>> getVisibleFeelings() {
        return ResponseEntity.ok(feelingService.getActiveFeelings());
    }

    // Retrieve all feelings for administration catalog
    @GetMapping("/all")
    public ResponseEntity<List<FeelingResponse>> getAllFeelings() {
        return ResponseEntity.ok(feelingService.getAllFeelings());
    }

    // Retrieve a single feeling by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getFeelingById(@PathVariable String id) {
        try {
            return ResponseEntity.ok(feelingService.getFeelingById(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }

    // Update feeling details and optionally update the image
    @PutMapping("/{id}")
    public ResponseEntity<?> updateFeeling(
            @PathVariable String id,
            @RequestParam("feelingName") String feelingName,
            @RequestParam("active") boolean active,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) {
        try {
            Feeling updatedFeeling = feelingService.updateFeeling(id, feelingName, active, image);
            return ResponseEntity.ok(updatedFeeling);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    // Delete a feeling record and its Cloudinary media asset
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFeeling(@PathVariable String id) {
        try {
            feelingService.deleteFeeling(id);
            return ResponseEntity.ok(Map.of("message", "Feeling deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}