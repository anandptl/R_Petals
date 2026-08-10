package as.r_petals.controller;

import as.r_petals.dto.FullProductRequest;
import as.r_petals.dto.common.ApiResponse;
import as.r_petals.dto.product.ProductResponse;
import as.r_petals.dto.product.ProductUpdateRequest;
import as.r_petals.dto.shop.ShopResponse;
import as.r_petals.entities.Product;
import as.r_petals.service.ProductService;
import as.r_petals.service.ShopService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final ShopService shopService;
    private final ProductService productService;

    public AdminController(ShopService shopService, ProductService productService) {
        this.shopService = shopService;
        this.productService = productService;
    }

    @PutMapping("/approve/{shopId}")
    public ResponseEntity<ApiResponse<ShopResponse>> approveShop(@PathVariable String shopId) {
        return ResponseEntity.ok(ApiResponse.success("Shop approved successfully", shopService.approveShop(shopId)));
    }

    @PostMapping("/add-product")
    public ResponseEntity<ApiResponse<ProductResponse>> addFullProduct(
            @Valid @RequestBody FullProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Product created successfully", productService.createFullProduct(request)));
    }

    @PutMapping("/update/{productId}")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @PathVariable String productId,
            @Valid @RequestBody ProductUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Product updated successfully",
                productService.updateProduct(productId, request)));
    }

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable String productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok(ApiResponse.success("Product deleted successfully"));
    }

    @DeleteMapping("/subcategory/{subCategoryId}")
    public ResponseEntity<ApiResponse<Void>> deleteSubCategory(@PathVariable String subCategoryId) {
        productService.deleteSubCategory(subCategoryId);
        return ResponseEntity.ok(ApiResponse.success("Subcategory deleted successfully"));
    }

    @DeleteMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable String categoryId) {
        productService.deleteCategory(categoryId);
        return ResponseEntity.ok(ApiResponse.success("Category deleted successfully"));
    }
}
