package as.r_petals.dto.subcategory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SubCategoryRequest {
    @NotBlank(message="Category ID is required") private String categoryId;
    @NotBlank(message="Sub-category name is required") @Size(max=100) private String subCategoryName;
    public SubCategoryRequest() {}
    public String getCategoryId(){return categoryId;} public void setCategoryId(String v){categoryId=v;}
    public String getSubCategoryName(){return subCategoryName;} public void setSubCategoryName(String v){subCategoryName=v;}
}
