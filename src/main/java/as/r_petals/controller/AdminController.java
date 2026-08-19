package as.r_petals.controller;

import as.r_petals.dto.FullProductRequest;
import as.r_petals.dto.Stores.AdminStoresRegistrationRequest;
import as.r_petals.dto.Stores.StoresResponse;
import as.r_petals.dto.admin.AdminDashboardResponse;
import as.r_petals.dto.admin.AdminStoreResponse;
import as.r_petals.dto.category.CategoryResponse;
import as.r_petals.dto.common.ApiResponse;
import as.r_petals.dto.product.ProductResponse;
import as.r_petals.dto.product.ProductUpdateRequest;
import as.r_petals.exception.BadRequestException;
import as.r_petals.service.AdminService;
import as.r_petals.service.ProductService;
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

    private final ProductService productService;
    private final Validator validator;
    private final AdminService adminService;

    public AdminController(ProductService productService, Validator validator, AdminService adminService) {
        this.productService = productService;
        this.validator = validator;
        this.adminService = adminService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<AdminDashboardResponse>> getDashboard() {
        return ResponseEntity.ok(
                ApiResponse.success("Admin dashboard data fetched successfully", adminService.getDashboardStats())
        );
    }

    @GetMapping("/stores")
    public ResponseEntity<ApiResponse<List<AdminStoreResponse>>> getAllShops() {
        return ResponseEntity.ok(
                ApiResponse.success("All shops fetched successfully", adminService.getAllStores())
        );
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<StoresResponse>> registerShop(@Valid @RequestBody AdminStoresRegistrationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Shopkeeper and store registered successfully", adminService.registerShopByAdmin(request)));
    }

    @PostMapping(value = "/add-product", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ProductResponse>> addFullProduct(
            @RequestPart("product") String productJson,
            @RequestPart("images") List<MultipartFile> images
    ) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        FullProductRequest request = objectMapper.readValue(productJson, FullProductRequest.class);

        Set<ConstraintViolation<FullProductRequest>> violations = validator.validate(request);
        if (!violations.isEmpty()) {
            String message = violations.iterator().next().getMessage();
            throw new BadRequestException(message);
        }

        ProductResponse response = productService.createFullProduct(request, images);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Product created successfully", response));
    }

    @GetMapping("/products")
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getAllProducts() {
        return ResponseEntity.ok(
                ApiResponse.success("All products fetched successfully", productService.getAllProducts())
        );
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProduct(@PathVariable String productId) {
        return ResponseEntity.ok(
                ApiResponse.success("Product fetched successfully", productService.getProductById(productId))
        );
    }

    @PutMapping("/update/{productId}")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @PathVariable String productId,
            @Valid @RequestBody ProductUpdateRequest request
    ) {
        return ResponseEntity.ok(
                ApiResponse.success("Product updated successfully", productService.updateProduct(productId, request))
        );
    }

    @PutMapping(value = "/update/{productId}/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ProductResponse>> updateProductImages(
            @PathVariable String productId,
            @RequestPart("images") List<MultipartFile> images
    ) {
        ProductResponse response = productService.updateProductImages(productId, images);
        return ResponseEntity.ok(
                ApiResponse.success("Product images updated successfully", response)
        );
    }

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable String productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok(
                ApiResponse.success("Product deleted successfully")
        );
    }

    @DeleteMapping("/subcategory/{subCategoryId}")
    public ResponseEntity<ApiResponse<Void>> deleteSubCategory(@PathVariable String subCategoryId) {
        productService.deleteSubCategory(subCategoryId);
        return ResponseEntity.ok(
                ApiResponse.success("Subcategory deleted successfully")
        );
    }

    @DeleteMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable String categoryId) {
        productService.deleteCategory(categoryId);
        return ResponseEntity.ok(
                ApiResponse.success("Category deleted successfully")
        );
    }

    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getCategories() {
        return ResponseEntity.ok(
                ApiResponse.success("Categories fetched successfully", productService.getCategoriesWithProductCount())
        );
    }
}