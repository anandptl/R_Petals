package as.r_petals.dto.shop;

import jakarta.validation.constraints.NotBlank;

public class ShopProductRequest {

    @NotBlank(message = "Product ID is required")
    private String productId;

    public ShopProductRequest() {
    }

    public ShopProductRequest(String productId) {
        this.productId = productId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
