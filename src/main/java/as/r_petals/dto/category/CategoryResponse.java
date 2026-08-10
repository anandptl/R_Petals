package as.r_petals.dto.category;

import as.r_petals.entities.Category;
import java.time.LocalDateTime;

public class CategoryResponse {
    private String id; private String categoryName; private String categoryImage; private boolean active; private LocalDateTime createdAt; private LocalDateTime updatedAt;
    public CategoryResponse() {}
    public CategoryResponse(Category c){id=c.getId(); categoryName=c.getCategoryName(); categoryImage=c.getCategoryImage(); active=c.isActive(); createdAt=c.getCreatedAt(); updatedAt=c.getUpdatedAt();}
    public String getId(){return id;} public String getCategoryName(){return categoryName;} public String getCategoryImage(){return categoryImage;} public boolean isActive(){return active;} public LocalDateTime getCreatedAt(){return createdAt;} public LocalDateTime getUpdatedAt(){return updatedAt;}
}
