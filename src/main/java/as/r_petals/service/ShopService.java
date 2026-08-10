package as.r_petals.service;

import java.time.LocalDateTime;

import as.r_petals.dto.shop.ShopRegistrationRequest;
import as.r_petals.dto.shop.ShopResponse;
import as.r_petals.dto.shop.ShopUpdateRequest;
import as.r_petals.enums.Role;
import as.r_petals.exception.BadRequestException;
import as.r_petals.exception.ConflictException;
import as.r_petals.exception.ResourceNotFoundException;
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
    @Autowired
    private CurrentUserService currentUserService;

    public ShopResponse registerShop(ShopRegistrationRequest request) {
        Users user = currentUserService.getCurrentUser();

        if (user.getRole() == Role.ADMIN) {
            throw new BadRequestException("Admin cannot register a customer shop");
        }

        if (shopRepository.existsByUserId(user.getId())) {
            throw new ConflictException("You have already applied for shop registration");
        }

        Shops shop = new Shops();
        shop.setUserId(user.getId());
        shop.setShopName(request.getShopName().trim());
        shop.setShopkeeperName(request.getShopkeeperName().trim());
        shop.setGstNumber(request.getGstNumber());
        shop.setAddress(request.getAddress().trim());
        shop.setCity(request.getCity().trim());
        shop.setState(request.getState().trim());
        shop.setPincode(request.getPincode());
        shop.setLatitude(request.getLatitude());
        shop.setLongitude(request.getLongitude());
        shop.setShopImage(request.getShopImage());
        shop.setStatus(ShopStatus.PENDING);
        shop.setActive(false);
        shop.setCreatedAt(LocalDateTime.now());
        shop.setUpdatedAt(LocalDateTime.now());

        return new ShopResponse(shopRepository.save(shop));
    }

    public ShopResponse approveShop(String shopId) {
        Shops shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new ResourceNotFoundException("Shop not found"));

        if (shop.getStatus() != ShopStatus.PENDING) {
            throw new ConflictException("Only pending shops can be approved");
        }

        Users user = userRepository.findById(shop.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Shop owner not found"));

        shop.setStatus(ShopStatus.APPROVED);
        shop.setActive(true);
        shop.setUpdatedAt(LocalDateTime.now());
        Shops savedShop = shopRepository.save(shop);

        user.setRole(Role.SHOPKEEPER);
        user.setShop(savedShop);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        return new ShopResponse(savedShop);
    }

    public void rejectShop(String shopId) {
        Shops shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new ResourceNotFoundException("Shop not found"));

        if (shop.getStatus() != ShopStatus.PENDING) {
            throw new ConflictException("Only pending shops can be rejected");
        }

        shop.setStatus(ShopStatus.REJECTED);
        shop.setActive(false);
        shop.setUpdatedAt(LocalDateTime.now());
        shopRepository.save(shop);
    }

    public ShopResponse updateCurrentShop(ShopUpdateRequest request) {
        Users user = currentUserService.getCurrentUser();
        Shops shop = shopRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Shop not found"));

        if (shop.getStatus() == ShopStatus.REJECTED) {
            throw new ConflictException("Rejected shop cannot be updated");
        }

        if (request.getShopName() != null) shop.setShopName(request.getShopName().trim());
        if (request.getShopkeeperName() != null) shop.setShopkeeperName(request.getShopkeeperName().trim());
        if (request.getGstNumber() != null) shop.setGstNumber(request.getGstNumber());
        if (request.getAddress() != null) shop.setAddress(request.getAddress().trim());
        if (request.getCity() != null) shop.setCity(request.getCity().trim());
        if (request.getState() != null) shop.setState(request.getState().trim());
        if (request.getPincode() != null) shop.setPincode(request.getPincode());
        if (request.getLatitude() != null) shop.setLatitude(request.getLatitude());
        if (request.getLongitude() != null) shop.setLongitude(request.getLongitude());
        if (request.getShopImage() != null) shop.setShopImage(request.getShopImage());
        shop.setUpdatedAt(LocalDateTime.now());

        return new ShopResponse(shopRepository.save(shop));
    }
}