package as.r_petals.dto.subcategory;

import as.r_petals.entities.SubCategory;
import java.time.LocalDateTime;

public class SubCategoryResponse {
    private String id; private String categoryId; private String subCategoryName; private boolean active; private LocalDateTime createdAt; private LocalDateTime updatedAt;
    public SubCategoryResponse() {}
    public SubCategoryResponse(SubCategory s){id=s.getId(); categoryId=s.getCategoryId(); subCategoryName=s.getSubCategoryName(); active=s.isActive(); createdAt=s.getCreatedAt(); updatedAt=s.getUpdatedAt();}
    public String getId(){return id;} public String getCategoryId(){return categoryId;} public String getSubCategoryName(){return subCategoryName;} public boolean isActive(){return active;} public LocalDateTime getCreatedAt(){return createdAt;} public LocalDateTime getUpdatedAt(){return updatedAt;}
}
