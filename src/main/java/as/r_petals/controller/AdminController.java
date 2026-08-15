package as.r_petals.controller;

import as.r_petals.dto.FullProductRequest;
import as.r_petals.dto.admin.AdminDashboardResponse;
import as.r_petals.dto.admin.AdminShopDetailResponse;
import as.r_petals.dto.common.ApiResponse;
import as.r_petals.dto.product.ProductResponse;
import as.r_petals.dto.product.ProductUpdateRequest;
import as.r_petals.dto.shop.ShopResponse;
import as.r_petals.exception.BadRequestException;
import as.r_petals.service.AdminService;
import as.r_petals.service.ProductService;
import as.r_petals.service.ShopService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final ShopService shopService;
    private final ProductService productService;
    private final Validator validator;
    private final AdminService adminService;

    public AdminController(ShopService shopService, ProductService productService, Validator validator, AdminService adminService) {
        this.shopService = shopService;
        this.productService = productService;
        this.validator = validator;
        this.adminService = adminService;
    }

    //    Dashboard......
    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<AdminDashboardResponse>> getDashboard() {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Admin dashboard data fetched successfully",
                        adminService.getDashboardStats()
                )
        );
    }

    // GET all shops
    @GetMapping("/shops")
    public ResponseEntity<ApiResponse<List<ShopResponse>>> getAllShops() {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "All shops fetched successfully",
                        shopService.getAllShopsForAdmin()
                )
        );
    }


    // GET SHOP DETAILS

    @GetMapping("/shops/{shopId}")
    public ResponseEntity<ApiResponse<AdminShopDetailResponse>> getShopDetails(
            @PathVariable String shopId
    ) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Shop details fetched successfully",
                        shopService.getShopDetailsForAdmin(shopId)
                )
        );
    }

    // APPROVE SHOP
    @PatchMapping("/shops/{shopId}/approve")
    public ResponseEntity<ApiResponse<ShopResponse>> approveShop(@PathVariable String shopId) {
        ShopResponse response = shopService.approveShop(shopId);
        return ResponseEntity.ok(ApiResponse.success("Shop approved successfully", response));
    }

//    reject shop
    @PatchMapping("/shops/{shopId}/reject")
    public ResponseEntity<ApiResponse<Void>> rejectShop(@PathVariable String shopId) {
        shopService.rejectShop(shopId);
        return ResponseEntity.ok(ApiResponse.success("Shop rejected successfully", null));
    }

    @PostMapping(value = "/add-product", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ProductResponse>> addFullProduct(

            @RequestPart("product") String productJson,
            @RequestPart("images") List<MultipartFile> images) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        FullProductRequest request = objectMapper.readValue(productJson, FullProductRequest.class);

        Set<ConstraintViolation<FullProductRequest>> violations = validator.validate(request);
        if (!violations.isEmpty()) {
            String message = violations.iterator().next().getMessage();
            throw new BadRequestException(message);
        }

        ProductResponse response = productService.createFullProduct(request, images);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Product created successfully", response));
    }

    // UPDATE PRODUCT

    @PutMapping("/update/{productId}")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(@PathVariable String productId, @Valid @RequestBody ProductUpdateRequest request) {

        return ResponseEntity.ok(ApiResponse.success("Product updated successfully", productService.updateProduct(productId, request)));
    }

    // DELETE PRODUCT

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable String productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok(ApiResponse.success("Product deleted successfully"));
    }

    // DELETE SUBCATEGORY

    @DeleteMapping("/subcategory/{subCategoryId}")
    public ResponseEntity<ApiResponse<Void>> deleteSubCategory(@PathVariable String subCategoryId) {
        productService.deleteSubCategory(subCategoryId);
        return ResponseEntity.ok(ApiResponse.success("Subcategory deleted successfully"));
    }

    // DELETE CATEGORY

    @DeleteMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable String categoryId) {
        productService.deleteCategory(categoryId);
        return ResponseEntity.ok(ApiResponse.success("Category deleted successfully"));
    }
}