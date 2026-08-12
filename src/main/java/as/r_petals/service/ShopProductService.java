package as.r_petals.service;

import as.r_petals.dto.common.ApiResponse;
import as.r_petals.dto.shop.ShopProductRequest;
import as.r_petals.dto.shop.ShopProductResponse;
import as.r_petals.entities.*;
import as.r_petals.exception.BadRequestException;
import as.r_petals.exception.ConflictException;
import as.r_petals.exception.ResourceNotFoundException;
import as.r_petals.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopProductService {

    @Autowired
    private ShopProductRepository shopProductRepository;
    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private SubCategoryRepository subCategoryRepository;
    @Autowired
    private UserRepository userRepository;

    public ApiResponse<ShopProductResponse> addProduct(ShopProductRequest request) {

        // JWT se mobile number
        String mobileNumber = getCurrentUser();

        // Mobile number se current user
        Users user = userRepository.findByMobileNumber(mobileNumber).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // User ID se shop
        Shops shop = shopRepository.findByUserId(user.getId()).orElseThrow(() -> new ResourceNotFoundException("Shop not found for current user"));

        // Shop active check
        if (!shop.isActive()) {throw new BadRequestException("Shop is not active");}

        // Product find
        Product product = productRepository.findById(request.getProductId()).orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        // Duplicate check
        if (shopProductRepository.existsByShopIdAndProductId(shop.getId(), product.getId())) {
            throw new ConflictException("Product already added to your shop");
        }

        // Product -> SubCategory
        SubCategory subCategory = subCategoryRepository.findById(product.getSubCategoryId()).orElseThrow(() ->
                new ResourceNotFoundException("SubCategory not found"));

        // Create ShopProduct
        ShopProduct shopProduct = new ShopProduct();

        shopProduct.setShopId(shop.getId());
        shopProduct.setProductId(product.getId());
        shopProduct.setSubCategoryId(subCategory.getId());
        shopProduct.setCategoryId(subCategory.getCategoryId());

        // Save
        ShopProduct saved = shopProductRepository.save(shopProduct);

        return ApiResponse.success("Product added to shop successfully", mapToResponse(saved));
    }

    public ApiResponse<List<ShopProductResponse>> getMyShopProducts() {

        String mobileNumber = getCurrentUser();
        Shops shop = shopRepository.findByUserId(mobileNumber).orElseThrow(() -> new ResourceNotFoundException("Shop not found"));
        List<ShopProduct> products = shopProductRepository.findByShopId(shop.getId());
        List<ShopProductResponse> response = products.stream().map(this::mapToResponse).toList();
        return ApiResponse.success("Shop products fetched successfully", response);
    }

    public ApiResponse<Void> removeProduct(String productId) {

        String mobileNumber = getCurrentUser();
        Shops shop = shopRepository.findByUserId(mobileNumber).orElseThrow(() -> new ResourceNotFoundException("Shop not found"));
        if (!shopProductRepository.existsByShopIdAndProductId(shop.getId(), productId)) {
            throw new ResourceNotFoundException("Product is not available in your shop");
        }
        shopProductRepository.deleteByShopIdAndProductId(shop.getId(), productId);
        return ApiResponse.success("Product removed successfully");
    }

    public ApiResponse<List<ShopProductResponse>> getShopsHavingProduct(String productId) {

        List<ShopProduct> products = shopProductRepository.findByProductId(productId);
        List<ShopProductResponse> response = products.stream().map(this::mapToResponse).toList();
        return ApiResponse.success("Shops found successfully", response);
    }

    private String getCurrentUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BadRequestException("Authentication required");
        }
        return authentication.getName();
    }

    private ShopProductResponse mapToResponse(ShopProduct shopProduct) {
        return new ShopProductResponse(
                shopProduct.getId(),
                shopProduct.getShopId(),
                shopProduct.getProductId(),
                shopProduct.getSubCategoryId(),
                shopProduct.getCategoryId());
    }
}