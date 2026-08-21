package as.r_petals.controller;

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
            @RequestParam("occasionDate")
            String occasionDate,
            @RequestParam("active")
            boolean active,
            @RequestParam("image")
            MultipartFile image

    ) {

        try {
            LocalDateTime date = LocalDateTime.parse(occasionDate);
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
}