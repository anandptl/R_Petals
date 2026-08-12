package as.r_petals.dto.product;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class ProductUpdateRequest {

    @Size(max = 150)
    private String productName;

    @Size(max = 1000)
    private String description;

    @DecimalMin(
            value = "0.0",
            message = "Price cannot be negative"
    )
    private BigDecimal price;

    private Boolean active;

    public ProductUpdateRequest() {
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

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @AssertTrue(message = "At least one product field must be provided")
    public boolean isUpdateProvided() {

        return productName != null
                || description != null
                || price != null
                || active != null;
    }
}