package as.r_petals.controller;

import as.r_petals.entities.UserAddress;
import as.r_petals.service.UserAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/address")
public class UserAddressController {

    @Autowired
    private UserAddressService addressService;

    @PostMapping("/save")
    public Map<String, Object> saveAddress(
            @RequestParam String userId,
            @RequestBody UserAddress address) {

        return addressService.saveAddress(userId, address);
    }

    @PutMapping("/default")
    public Map<String, Object> setDefaultAddress(
            @RequestParam String userId,
            @RequestParam String addressId) {

        return addressService.setDefaultAddress(userId, addressId);
    }

}
