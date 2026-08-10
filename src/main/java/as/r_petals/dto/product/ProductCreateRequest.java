package as.r_petals.dto.product;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public class ProductCreateRequest {
    @NotBlank(message="Sub-category ID is required") private String subCategoryId;
    @NotBlank(message="Product name is required") @Size(max=150) private String productName;
    @Size(max=1000) private String description;
    @NotNull(message="Price is required") @DecimalMin(value="0.0", message="Price cannot be negative") private BigDecimal price;
    private String productImage;
    public ProductCreateRequest() {}
    public String getSubCategoryId(){return subCategoryId;} public void setSubCategoryId(String v){subCategoryId=v;}
    public String getProductName(){return productName;} public void setProductName(String v){productName=v;}
    public String getDescription(){return description;} public void setDescription(String v){description=v;}
    public BigDecimal getPrice(){return price;} public void setPrice(BigDecimal v){price=v;}
    public String getProductImage(){return productImage;} public void setProductImage(String v){productImage=v;}
}
