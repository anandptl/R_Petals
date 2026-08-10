package as.r_petals.dto.product;

import as.r_petals.entities.Product;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductResponse {
    private String id; private String subCategoryId; private String productName; private String description; private BigDecimal price; private String productImage; private boolean active; private LocalDateTime createdAt; private LocalDateTime updatedAt;
    public ProductResponse() {}
    public ProductResponse(Product p){id=p.getId(); subCategoryId=p.getSubCategoryId(); productName=p.getProductName(); description=p.getDescription(); price=p.getPrice(); productImage=p.getProductImage(); active=p.isActive(); createdAt=p.getCreatedAt(); updatedAt=p.getUpdatedAt();}
    public String getId(){return id;} public String getSubCategoryId(){return subCategoryId;} public String getProductName(){return productName;} public String getDescription(){return description;} public BigDecimal getPrice(){return price;} public String getProductImage(){return productImage;} public boolean isActive(){return active;} public LocalDateTime getCreatedAt(){return createdAt;} public LocalDateTime getUpdatedAt(){return updatedAt;}
}
