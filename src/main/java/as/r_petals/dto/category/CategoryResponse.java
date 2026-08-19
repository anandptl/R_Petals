package as.r_petals.dto.category;

import as.r_petals.dto.subcategory.SubCategoryResponse;
import as.r_petals.entities.Category;

import java.time.LocalDateTime;
import java.util.List;

public class CategoryResponse {

    private String id;
    private String categoryName;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<SubCategoryResponse> subCategories;
    private long productCount;


    public CategoryResponse() {
    }


    public CategoryResponse(Category c) {

        this.id = c.getId();
        this.categoryName = c.getCategoryName();
        this.active = c.isActive();
        this.createdAt = c.getCreatedAt();
        this.updatedAt = c.getUpdatedAt();

    }

    public String getId() {
        return id;
    }

    public String getCategoryName() {
        return categoryName;
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

    public List<SubCategoryResponse> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<SubCategoryResponse> subCategories) {
        this.subCategories = subCategories;
    }

    public long getProductCount() {return productCount;
    }

    public void setProductCount(long productCount) {
        this.productCount = productCount;
    }
}