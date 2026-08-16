package as.r_petals.service;

import as.r_petals.dto.admin.AdminDashboardResponse;
import as.r_petals.repository.ProductRepository;
import as.r_petals.repository.StoresRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    @Autowired
    private StoresRepository storesRepository;
    @Autowired
    private ProductRepository productRepository;

    public AdminDashboardResponse getDashboardStats() {

        long totalShops = storesRepository.count();

        long totalProducts = productRepository.count();

        long totalOrders = 0;

        return new AdminDashboardResponse(
                totalShops,
                totalProducts,
                totalOrders
        );
    }
}
