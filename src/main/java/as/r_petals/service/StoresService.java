package as.r_petals.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import as.r_petals.dto.Stores.StoresRegistrationRequest;
import as.r_petals.dto.Stores.StoresResponse;
import as.r_petals.dto.Stores.StoresUpdateRequest;
import as.r_petals.entities.Stores;
import as.r_petals.entities.Users;
import as.r_petals.enums.Role;
import as.r_petals.exception.BadRequestException;
import as.r_petals.exception.ConflictException;
import as.r_petals.exception.ResourceNotFoundException;
import as.r_petals.repository.ProductRepository;
import as.r_petals.repository.StoresRepository;
import as.r_petals.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class StoresService {

    private final StoresRepository storesRepository;
    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;
    private final ProductRepository productRepository;



    public StoresService(StoresRepository storesRepository, UserRepository userRepository,
                         CurrentUserService currentUserService, ProductRepository productRepository) {
        this.storesRepository = storesRepository;
        this.userRepository = userRepository;
        this.currentUserService = currentUserService;
        this.productRepository = productRepository;
    }

    // register shop by the shopkeeper.....

    public StoresResponse registerShop(StoresRegistrationRequest request) {

        Users user = currentUserService.getCurrentUser();

        if (user.getRole() == Role.ADMIN) {
            throw new BadRequestException("Admin cannot register a customer shop");
        }

        if (storesRepository.existsByUserId(user.getId())) {
            throw new ConflictException("You have already applied for shop registration");
        }

        Stores shop = new Stores();
        shop.setUserId(user.getId());
        shop.setAddress(request.getAddress().trim());
        shop.setCity(request.getCity().trim());
        shop.setState(request.getState().trim());
        shop.setPincode(request.getPincode());
        shop.setLatitude(request.getLatitude());
        shop.setLongitude(request.getLongitude());
        shop.setTodayActive(false);
        shop.setCreatedAt(LocalDateTime.now());
        shop.setUpdatedAt(LocalDateTime.now());

        Stores savedShop = storesRepository.save(shop);

        return new StoresResponse(savedShop);
    }


    // Admin - update our shops
    public StoresResponse updateCurrentShop(StoresUpdateRequest request) {

        Users user = currentUserService.getCurrentUser();
        Stores shop = storesRepository.findByUserId(user.getId()).orElseThrow(() -> new ResourceNotFoundException("Shop not found"));

        if (request.getAddress() != null) {shop.setAddress(request.getAddress().trim());}
        if (request.getCity() != null) {shop.setCity(request.getCity().trim());}
        if (request.getState() != null) {shop.setState(request.getState().trim());}
        if (request.getPincode() != null) {shop.setPincode(request.getPincode());}
        if (request.getLatitude() != null) {shop.setLatitude(request.getLatitude());}
        if (request.getLongitude() != null) {shop.setLongitude(request.getLongitude());}

        shop.setUpdatedAt(LocalDateTime.now());

        Stores savedShop = storesRepository.save(shop);

        return new StoresResponse(savedShop);
    }


    // shop today open or not....

    public StoresResponse updateTodayActive(boolean todayActive) {

        Users user = currentUserService.getCurrentUser();

        Stores shop = storesRepository.findByUserId(user.getId()).orElseThrow(() -> new ResourceNotFoundException("Shop not found"));

        // Only approved + active shops can open today


        shop.setTodayActive(todayActive);
        shop.setUpdatedAt(LocalDateTime.now());
        Stores savedShop = storesRepository.save(shop);
        return new StoresResponse(savedShop);
    }

    // ADMIN - GET ALL SHOPS
    public List<StoresResponse> getAllShopsForAdmin() {
        return storesRepository.findAll().stream().map(StoresResponse::new).toList();
    }
}