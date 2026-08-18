package as.r_petals.service;

import as.r_petals.dto.Stores.StoresResponse;
import as.r_petals.dto.admin.AdminDashboardResponse;
import as.r_petals.dto.admin.AdminShopDetailResponse;
import as.r_petals.dto.admin.StoreStatusResponse;
import as.r_petals.entities.Stores;
import as.r_petals.entities.Users;
import as.r_petals.enums.Role;
import as.r_petals.exception.ConflictException;
import as.r_petals.repository.ProductRepository;
import as.r_petals.repository.StoresRepository;
import as.r_petals.repository.UserRepository;
import org.springframework.stereotype.Service;
import as.r_petals.dto.Stores.AdminStoresRegistrationRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


@Service
public class AdminService {
    private final StoresRepository storesRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public AdminService(StoresRepository storesRepository, ProductRepository productRepository,
                        UserRepository userRepository) {
        this.storesRepository = storesRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public AdminDashboardResponse getDashboardStats() {

        long totalShops = storesRepository.count();
        long totalProducts = productRepository.count();
        long totalOrders = 0;
        return new AdminDashboardResponse(
                totalShops,
                totalProducts,
                totalOrders
        );
    }

    //    Admin - register shop
    public StoresResponse registerShopByAdmin(AdminStoresRegistrationRequest request) {

        String mobile = request.getMobile().trim();
        String email = null;

        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            email = request.getEmail().trim().toLowerCase();
        }

        Users user = userRepository.findByMobileNumber(mobile).orElse(null);
        // Create new user if mobile not found

        if (user == null) {
            if (email != null) {
                userRepository.findByEmailIgnoreCase(email).ifPresent(existing -> {
                    throw new ConflictException("A user with this email already exists.");
                });
            }

            user = new Users();
            user.setFullName(request.getShopkeeperName().trim());
            user.setMobileNumber(mobile);
            user.setEmail(email);
            user.setVerified(true);
            user.setEmailVerified(email != null);
            user.setRole(Role.SHOPKEEPER);
            user.setActive(true);
            user.setCreatedAt(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());

            user = userRepository.save(user);
        }


        // Use existing user if mobile already exists

        else {
            if (user.getRole() == Role.ADMIN) {
                throw new ConflictException(
                        "Admin user cannot be registered as a shopkeeper."
                );
            }

            user.setRole(Role.SHOPKEEPER);

            if (user.getFullName() == null || user.getFullName().isBlank()) {
                user.setFullName(request.getShopkeeperName().trim());
            }

            if ((user.getEmail() == null || user.getEmail().isBlank()) && email != null) {
                user.setEmail(email);
                user.setEmailVerified(true);
            }
            user.setUpdatedAt(LocalDateTime.now());

            user = userRepository.save(user);
        }

        // Create store

        Stores shop = new Stores();
        shop.setUserId(user.getId());
        shop.setShopName(request.getShopName().trim());
        shop.setCountry(request.getCountry().trim());
        shop.setAddress(request.getAddress().trim());
        shop.setCity(request.getCity().trim());
        shop.setState(request.getState().trim());
        shop.setPincode(request.getPincode().trim());
        shop.setLatitude(request.getLatitude().trim());
        shop.setLongitude(request.getLongitude().trim());
        shop.setTodayActive(false);
        shop.setCreatedAt(LocalDateTime.now());
        shop.setUpdatedAt(LocalDateTime.now());


        Stores savedShop = storesRepository.save(shop);
        // Link store with user
        user.setShop(savedShop);

        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        return new StoresResponse(savedShop);
    }


    //    shop the table admin dashbord....
    public List<StoreStatusResponse> getStoreStatus() {

        List<Stores> stores = storesRepository.findAll();
        List<StoreStatusResponse> response = new ArrayList<>();

        for (Stores store : stores) {

            Users user = userRepository.findById(store.getUserId()).orElse(null);
            StoreStatusResponse item = new StoreStatusResponse();
            item.setStoreId(store.getId());
            item.setShopName(store.getShopName());

            if (user != null) {
                item.setUserName(user.getFullName());
                item.setMobileNumber(user.getMobileNumber());
            }
            item.setTodayActive(store.isTodayActive());
            item.setLastActiveAt(store.getLastActiveAt());

            response.add(item);
        }

        response.sort(
                Comparator.comparing(StoreStatusResponse::isTodayActive).reversed()
        );

        return response;
    }

    public List<AdminShopDetailResponse> getAllStores() {

        List<Stores> stores =
                storesRepository.findAll();

        List<AdminShopDetailResponse> response =
                new ArrayList<>();

        for (Stores store : stores) {

            StoresResponse shopResponse =
                    new StoresResponse(store);

            AdminShopDetailResponse.UserInfo userInfo =
                    null;

            if (store.getUserId() != null) {

                Users user =
                        userRepository
                                .findById(store.getUserId())
                                .orElse(null);

                if (user != null) {

                    userInfo =
                            new AdminShopDetailResponse.UserInfo(
                                    user.getId(),
                                    user.getFullName(),
                                    user.getMobileNumber(),
                                    user.getEmail(),
                                    user.isVerified(),
                                    user.isEmailVerified(),
                                    user.getRole() != null
                                            ? user.getRole().name()
                                            : null,
                                    user.isActive()
                            );
                }
            }

            response.add(
                    new AdminShopDetailResponse(
                            shopResponse,
                            userInfo,
                            new ArrayList<>()
                    )
            );
        }

        return response;
    }
}