package as.r_petals.controller;

import as.r_petals.dto.common.ApiResponse;
import as.r_petals.dto.Stores.StoresRegistrationRequest;
import as.r_petals.dto.Stores.StoresResponse;
import as.r_petals.service.StoresService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Stores")
public class StoresController {

    private final StoresService storesService;
    public StoresController(StoresService storesService) {
        this.storesService = storesService;
    }

    @PutMapping("/today-active")
    public ResponseEntity<ApiResponse<StoresResponse>> updateTodayActive(
            @RequestParam boolean active) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Shop today's active status updated successfully",
                        storesService.updateTodayActive(active)
                )
        );
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<StoresResponse>> registerShop(@Valid @RequestBody StoresRegistrationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Shop registration request submitted successfully",
                        storesService.registerShop(request)));
    }
}
