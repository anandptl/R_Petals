package as.r_petals.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "products")
public class Product {

    @Id
    private String id;

    private String subCategoryId;

    private String productName;

    private String description;

    private BigDecimal price;

    private List<String> occasionIds;

    private List<String> feelingIds;

    private boolean active = true;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public Product() {
    }
//  getter & setter......

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(String subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {this.price = price;}

    public List<String> getOccasionIds() {
        return occasionIds;
    }

    public void setOccasionIds(List<String> occasionIds) {
        this.occasionIds = occasionIds;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<String> getFeelingIds() {
        return feelingIds;
    }

    public void setFeelingIds(List<String> feelingIds) {
        this.feelingIds = feelingIds;
    }
}