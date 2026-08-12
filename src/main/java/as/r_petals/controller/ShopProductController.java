package as.r_petals.controller;

import as.r_petals.dto.common.ApiResponse;
import as.r_petals.dto.shop.ShopProductRequest;
import as.r_petals.dto.shop.ShopProductResponse;
import as.r_petals.service.ShopProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shop/products")
public class ShopProductController {

    @Autowired
    private ShopProductService shopProductService;

    @PostMapping
    public ResponseEntity<ApiResponse<ShopProductResponse>> addProduct(@Valid @RequestBody ShopProductRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED).body( shopProductService.addProduct(request));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ShopProductResponse>>> getMyShopProducts() {
        return ResponseEntity.ok(shopProductService.getMyShopProducts());
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse<Void>>removeProduct(@PathVariable String productId) {
        return ResponseEntity.ok(shopProductService.removeProduct(productId));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<ApiResponse<List<ShopProductResponse>>> getShopsHavingProduct(
            @PathVariable String productId) {
        return ResponseEntity.ok(shopProductService.getShopsHavingProduct(productId));
    }
}


