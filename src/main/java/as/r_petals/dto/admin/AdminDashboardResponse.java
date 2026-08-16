package as.r_petals.dto.admin;

public class AdminDashboardResponse {

    private long totalShops;
    private long totalProducts;
    private long totalOrders;

    public AdminDashboardResponse() {
    }

    public AdminDashboardResponse(
            long totalShops,
            long totalProducts,
            long totalOrders
    ) {
        this.totalShops = totalShops;
        this.totalProducts = totalProducts;
        this.totalOrders = totalOrders;
    }

    public long getTotalShops() {
        return totalShops;
    }

    public void setTotalShops(long totalShops) {
        this.totalShops = totalShops;
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