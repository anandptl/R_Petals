package as.r_petals.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import as.r_petals.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import as.r_petals.entities.Shops;
import as.r_petals.entities.Users;
import as.r_petals.enums.ShopStatus;
import as.r_petals.repository.ShopRepository;
import as.r_petals.repository.UserRepository;

@Service
public class ShopService {

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private UserRepository userRepository;

    public Map<String, Object> registerShop(Shops shop) {

        Map<String, Object> response = new HashMap<>();

        // User Exists
        Users user = userRepository.findById(shop.getUserId()).orElse(null);

        if (user == null) {
            response.put("success", false);
            response.put("message", "User not found.");
            return response;
        }

        // Already Registered
        if (shopRepository.existsByUserId(shop.getUserId())) {
            response.put("success", false);
            response.put("message", "You have already applied for shop registration.");
            return response;
        }

        // Save Shop
        shop.setStatus(ShopStatus.PENDING);
        shop.setActive(false);
        shop.setCreatedAt(LocalDateTime.now());
        shop.setUpdatedAt(LocalDateTime.now());

        Shops savedShop = shopRepository.save(shop);

        response.put("success", true);
        response.put("message", "Shop registration request submitted successfully. Please wait for admin approval.");
        response.put("shop", savedShop);

        return response;
    }


    //    shop approve by admin for the shops....
    public Map<String, Object> approveShop(String shopId) {

        Map<String, Object> response = new HashMap<>();

        Shops shop = shopRepository.findById(shopId).orElse(null);

        if (shop == null) {

            response.put("success", false);
            response.put("message", "Shop not found");

            return response;
        }

        Users user = userRepository.findById(shop.getUserId()).orElse(null);

        if (user == null) {

            response.put("success", false);
            response.put("message", "User not found");

            return response;
        }

        shop.setStatus(ShopStatus.APPROVED);
        shop.setActive(true);
        shop.setUpdatedAt(LocalDateTime.now());

        shopRepository.save(shop);

        user.setRole(Role.SHOPKEEPER);
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);

        response.put("success", true);
        response.put("message", "Shop Approved Successfully");

        return response;
    }

    //    shops reject by the admin..
    public Map<String, Object> rejectShop(String shopId) {

        Map<String, Object> response = new HashMap<>();

        Shops shop = shopRepository.findById(shopId).orElse(null);

        if (shop == null) {

            response.put("success", false);
            response.put("message", "Shop not found");

            return response;
        }

        shop.setStatus(ShopStatus.REJECTED);
        shop.setActive(false);
        shop.setUpdatedAt(LocalDateTime.now());

        shopRepository.save(shop);

        response.put("success", true);
        response.put("message", "Shop Rejected");

        return response;
    }
}