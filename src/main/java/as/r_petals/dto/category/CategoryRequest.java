package as.r_petals.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CategoryRequest {
    @NotBlank(message="Category name is required") @Size(max=100) private String categoryName;
    @Size(max=500) private String categoryImage;
    public CategoryRequest() {}
    public String getCategoryName(){return categoryName;} public void setCategoryName(String v){categoryName=v;}
    public String getCategoryImage(){return categoryImage;} public void setCategoryImage(String v){categoryImage=v;}
}
