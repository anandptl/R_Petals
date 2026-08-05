package as.r_petals.service;

import as.r_petals.entities.UserAddress;
import as.r_petals.entities.Users;
import as.r_petals.repository.UserAddressRepository;
import as.r_petals.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserAddressService {

    @Autowired
    private UserAddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    public Map<String, Object> saveAddress(String userId,
                                           UserAddress address) {

        Map<String, Object> response = new HashMap<>();

        Users user = userRepository.findById(userId).orElse(null);

        if (user == null) {

            response.put("success", false);
            response.put("message", "User not found");

            return response;
        }

        address.setUserId(userId);

        address.setCreatedAt(LocalDateTime.now());

        address.setUpdatedAt(LocalDateTime.now());

        // Agar ye user ka pehla address hai to default bana do
        if (addressRepository.findByUserId(userId).isEmpty()) {
            address.setDefaultAddress(true);
        }

        addressRepository.save(address);

        response.put("success", true);
        response.put("message", "Address Saved Successfully");
        response.put("address", address);

        return response;
    }

//  change default address .......
    public Map<String, Object> setDefaultAddress(String userId, String addressId) {

        Map<String, Object> response = new HashMap<>();

        UserAddress newDefault = addressRepository.findById(addressId).orElse(null);

        if (newDefault == null) {
            response.put("success", false);
            response.put("message", "Address not found");
            return response;
        }

        if (!newDefault.getUserId().equals(userId)) {
            response.put("success", false);
            response.put("message", "Unauthorized");
            return response;
        }

        // Purana default address hatao
        addressRepository.findByUserIdAndDefaultAddressTrue(userId)
                .ifPresent(address -> {
                    address.setDefaultAddress(false);
                    address.setUpdatedAt(LocalDateTime.now());
                    addressRepository.save(address);
                });

        // Naya default address banao
        newDefault.setDefaultAddress(true);
        newDefault.setUpdatedAt(LocalDateTime.now());

        addressRepository.save(newDefault);

        response.put("success", true);
        response.put("message", "Default address updated");

        return response;
    }

}