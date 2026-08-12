package as.r_petals.dto.product;

import jakarta.validation.constraints.Min;

public class ProductImageUpdateRequest {

    @Min(value = 1, message = "Display order must be at least 1")
    private Integer displayOrder;

    private Boolean primary;

    public ProductImageUpdateRequest() {
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Boolean getPrimary() {
        return primary;
    }

    public void setPrimary(Boolean primary) {
        this.primary = primary;
    }
}