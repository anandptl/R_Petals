package as.r_petals.controller;

import as.r_petals.dto.occasions.OccasionStatsResponse;
import as.r_petals.entities.Occasion;
import as.r_petals.service.OccasionService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/occasions")
public class OccasionController {

    private final OccasionService occasionService;

    public OccasionController(OccasionService occasionService) {
        this.occasionService = occasionService;
    }


    @PostMapping("/save")
    public ResponseEntity<?> createOccasion(

            @RequestParam("occasionName")
            String occasionName,
            @RequestParam(value = "occasionDate", required = false)
            String occasionDate,
            @RequestParam("active")
            boolean active,
            @RequestParam("image")
            MultipartFile image

    ) {

        try {
            LocalDateTime date = null;
            if (occasionDate != null && !occasionDate.isBlank()) {
                date = LocalDateTime.parse(occasionDate);
            }
            Occasion occasion = occasionService.createOccasion(occasionName, date, active, image);

            return ResponseEntity.ok(occasion);

        } catch (Exception e) {

            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    //    occasion visible
    @GetMapping("/visible")
    public ResponseEntity<List<Occasion>> getVisibleOccasions() {
        return ResponseEntity.ok(
                occasionService.getVisibleOccasions()
        );
    }

    @GetMapping("/stats")
    public ResponseEntity<OccasionStatsResponse> getOccasionStats() {
        return ResponseEntity.ok(occasionService.getOccasionStats());
    }

    //Admin - get all occasiuon
    @GetMapping("/all")
    public ResponseEntity<List<Occasion>> getAllOccasions() {
        return ResponseEntity.ok(occasionService.getAllOccasions());
    }

    //    admin update occasion details..
    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<?> updateOccasion(@PathVariable String id, @RequestParam("occasionName")
                                            String occasionName,
                                            @RequestParam(value = "occasionDate", required = false)
                                            String occasionDate,
                                            @RequestParam("active")
                                                boolean active,
                                            @RequestParam(value = "image", required = false)
                                            MultipartFile image) {

        try {

            LocalDateTime date = null;
            if (occasionDate != null && !occasionDate.isBlank()) {
                date = LocalDateTime.parse(occasionDate);
            }
            Occasion updated = occasionService.updateOccasion(id, occasionName, date, active, image);

            return ResponseEntity.ok(updated);

        } catch (Exception e) {

            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOccasion(@PathVariable String id) {
        try {
            occasionService.deleteOccasion(id);
            return ResponseEntity.ok(Map.of("message", "Occasion deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}