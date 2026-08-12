package as.r_petals.controller;

import as.r_petals.dto.common.ApiResponse;
import as.r_petals.dto.shop.ShopProductRequest;
import as.r_petals.dto.shop.ShopProductResponse;
import as.r_petals.dto.shop.ShopRegistrationRequest;
import as.r_petals.dto.shop.ShopResponse;
import as.r_petals.service.ShopProductService;
import as.r_petals.service.ShopService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shop")
public class ShopController {

    private final ShopProductService shopProductService;
    private final ShopService shopService;
    public ShopController(ShopProductService shopProductService, ShopService shopService) {
        this.shopProductService = shopProductService;
        this.shopService = shopService;
    }


    @PostMapping("/register")
    public ResponseEntity<ApiResponse<ShopResponse>> registerShop(@Valid @RequestBody ShopRegistrationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Shop registration request submitted successfully",
                        shopService.registerShop(request)));
    }



    @PostMapping("/product/add")
    public ResponseEntity<ApiResponse<ShopProductResponse>> addProduct(@Valid @RequestBody ShopProductRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED).body( shopProductService.addProduct(request));
    }

//    @GetMapping
//    public ResponseEntity<ApiResponse<List<ShopProductResponse>>> getMyShopProducts() {
//
//        return ResponseEntity.ok(shopProductService.getMyShopProducts());
//    }

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
