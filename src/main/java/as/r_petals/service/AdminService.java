package as.r_petals.service;

import as.r_petals.dto.admin.AdminDashboardResponse;
import as.r_petals.enums.ShopStatus;
import as.r_petals.repository.ProductRepository;
import as.r_petals.repository.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private ProductRepository productRepository;

    public AdminDashboardResponse getDashboardStats() {

        long totalShops = shopRepository.count();

        long pendingApprovals = shopRepository.countByStatus(ShopStatus.PENDING);

        long totalProducts = productRepository.count();

        long totalOrders = 0;

        return new AdminDashboardResponse(
                totalShops,
                pendingApprovals,
                totalProducts,
                totalOrders
        );
    }
}
