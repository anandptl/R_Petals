package as.r_petals.dto.shop;

public class ShopProductResponse {

    private String id;

    private String shopId;

    private String productId;

    private String subCategoryId;

    private String categoryId;

    public ShopProductResponse() {
    }

    public ShopProductResponse(
            String id,
            String shopId,
            String productId,
            String subCategoryId,
            String categoryId
    ) {
        this.id = id;
        this.shopId = shopId;
        this.productId = productId;
        this.subCategoryId = subCategoryId;
        this.categoryId = categoryId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(String subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}