package as.r_petals.controller;

import as.r_petals.dto.FullProductRequest;
import as.r_petals.dto.common.ApiResponse;
import as.r_petals.dto.product.ProductResponse;
import as.r_petals.dto.product.ProductUpdateRequest;
import as.r_petals.dto.shop.ShopResponse;
import as.r_petals.service.ProductService;
import as.r_petals.service.ShopService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final ShopService shopService;
    private final ProductService productService;

    public AdminController(ShopService shopService, ProductService productService) {
        this.shopService = shopService;
        this.productService = productService;
    }

    // APPROVE SHOP

    @PutMapping("/approve/{shopId}")
    public ResponseEntity<ApiResponse<ShopResponse>> approveShop(@PathVariable String shopId) {

        return ResponseEntity.ok(ApiResponse.success("Shop approved successfully", shopService.approveShop(shopId)));
    }

    // CREATE PRODUCT + MULTIPLE IMAGES

    @PostMapping(value = "/add-product", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ProductResponse>> addFullProduct(

            @RequestPart("product") String productJson,
            @RequestPart("images") List<MultipartFile> images) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        FullProductRequest request = objectMapper.readValue(productJson, FullProductRequest.class);

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