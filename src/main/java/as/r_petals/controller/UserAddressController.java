package as.r_petals.controller;

import as.r_petals.dto.address.AddressRequest;
import as.r_petals.dto.address.AddressResponse;
import as.r_petals.dto.address.AddressUpdateRequest;
import as.r_petals.dto.common.ApiResponse;
import as.r_petals.entities.UserAddress;
import as.r_petals.service.UserAddressService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/address")
public class UserAddressController {

    @Autowired
    private UserAddressService addressService;

    @PostMapping("/save")
    public ResponseEntity<ApiResponse<AddressResponse>> saveAddress(@Valid @RequestBody AddressRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Address saved successfully", addressService.saveAddress(request)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AddressResponse>>> getMyAddresses() {
        return ResponseEntity.ok(ApiResponse.success("Addresses fetched successfully", addressService.getMyAddresses()));
    }

    @PutMapping("/{addressId}/default")
    public ResponseEntity<ApiResponse<Void>> setDefaultAddress(@PathVariable String addressId) {
        addressService.setDefaultAddress(addressId);
        return ResponseEntity.ok(ApiResponse.success("Default address updated successfully"));
    }

//    @DeleteMapping("/{addressId}")
//    public ResponseEntity<ApiResponse<Void>> deleteAddress(@PathVariable String addressId) {
//        addressService.deleteAddress(addressId);
//        return ResponseEntity.ok(ApiResponse.success("Address deleted successfully"));
//    }
}