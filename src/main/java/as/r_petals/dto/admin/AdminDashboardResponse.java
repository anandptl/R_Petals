package as.r_petals.dto.admin;

public class AdminDashboardResponse {

    private long totalStores;
    private long totalProducts;
    private long totalOrders;

    public AdminDashboardResponse() {
    }

    public AdminDashboardResponse(
            long totalStores,
            long totalProducts,
            long totalOrders
    ) {
        this.totalStores = totalStores;
        this.totalProducts = totalProducts;
        this.totalOrders = totalOrders;
    }

    public long getTotalStores() {
        return totalStores;
    }

    public void setTotalStores(long totalStores) {
        this.totalStores = totalStores;
    }

    public long getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(long totalProducts) {
        this.totalProducts = totalProducts;
    }

    public long getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(long totalOrders) {
        this.totalOrders = totalOrders;
    }
}