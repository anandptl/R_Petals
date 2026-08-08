package as.r_petals.controller;

import as.r_petals.dto.FullProductRequest;
import as.r_petals.entities.Product;
import as.r_petals.service.ProductService;
import as.r_petals.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ShopService shopService;

    @Autowired
    private ProductService productService;

    @PutMapping("/approve/{shopId}")
    public Map<String, Object> approveShop(
            @PathVariable String shopId) {

        return shopService.approveShop(shopId);
    }

    @PutMapping("/reject/{shopId}")
    public Map<String, Object> rejectShop(
            @PathVariable String shopId) {

        return shopService.rejectShop(shopId);
    }

    @PostMapping("/add-product")
    public ResponseEntity<?> addFullProduct(@RequestBody FullProductRequest request) {
        try {
            Product savedProduct = productService.createFullProduct(
                    request.getCategoryName(),
                    request.getCategoryImage(),
                    request.getSubCategoryName(),
                    request.getProductName(),
                    request.getDescription(),
                    request.getPrice(),
                    request.getProductImage()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Product Update
    @PutMapping("/update/{productId}")
    public Map<String, Object> updateProduct(
            @PathVariable String productId,
            @RequestBody Product product) {

        return productService.updateProduct(productId, product);
    }

    // Product Delete
    @DeleteMapping("/delete/{productId}")
    public Map<String, Object> deleteProduct(
            @PathVariable String productId) {

        return productService.deleteProduct(productId);
    }

    // SubCategory Delete
    @DeleteMapping("/subcategory/{subCategoryId}")
    public Map<String, Object> deleteSubCategory(
            @PathVariable String subCategoryId) {

        return productService.deleteSubCategory(subCategoryId);
    }

    // Category Delete
    @DeleteMapping("/category/{categoryId}")
    public Map<String, Object> deleteCategory(
            @PathVariable String categoryId) {

        return productService.deleteCategory(categoryId);
    }
}
