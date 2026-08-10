package as.r_petals.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "shop_products")
@CompoundIndex(
        name = "shop_product_unique",
        def = "{'shopId': 1, 'productId': 1}",
        unique = true
)
public class ShopProduct {

    @Id
    private String id;

    private String shopId;

    private String productId;

    private String subCategoryId;

    private String categoryId;

    public ShopProduct() {
    }

    public ShopProduct(
            String shopId,
            String productId,
            String subCategoryId,
            String categoryId
    ) {
        this.shopId = shopId;
        this.productId = productId;
        this.subCategoryId = subCategoryId;
        this.categoryId = categoryId;
    }

    public String getId() {
        return id;
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