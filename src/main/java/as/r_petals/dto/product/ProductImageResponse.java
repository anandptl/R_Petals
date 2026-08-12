package as.r_petals.dto.product;

public class ProductImageResponse {

    private String id;

    private String productId;

    private String imageUrl;

    private Integer displayOrder;

    private boolean primary;

    public ProductImageResponse() {
    }

    public ProductImageResponse(
            String id,
            String productId,
            String imageUrl,
            Integer displayOrder,
            boolean primary
    ) {
        this.id = id;
        this.productId = productId;
        this.imageUrl = imageUrl;
        this.displayOrder = displayOrder;
        this.primary = primary;
    }

    public String getId() {
        return id;
    }

    public String getProductId() {
        return productId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public boolean isPrimary() {
        return primary;
    }
}