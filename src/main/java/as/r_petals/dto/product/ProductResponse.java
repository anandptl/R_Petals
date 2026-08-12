package as.r_petals.dto.product;

import as.r_petals.entities.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class ProductResponse {

    private String id;

    private String subCategoryId;

    private String productName;

    private String description;

    private BigDecimal price;

    private boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private List<ProductImageResponse> images;

    public ProductResponse() {
    }

    public ProductResponse(Product product) {

        this.id = product.getId();
        this.subCategoryId = product.getSubCategoryId();
        this.productName = product.getProductName();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.active = product.isActive();
        this.createdAt = product.getCreatedAt();
        this.updatedAt = product.getUpdatedAt();
    }

    public String getId() {
        return id;
    }

    public String getSubCategoryId() {
        return subCategoryId;
    }

    public String getProductName() {
        return productName;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public boolean isActive() {
        return active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public List<ProductImageResponse> getImages() {
        return images;
    }

    public void setImages(List<ProductImageResponse> images) {
        this.images = images;
    }
}