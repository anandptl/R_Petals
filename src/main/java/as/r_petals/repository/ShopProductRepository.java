package as.r_petals.repository;

import as.r_petals.entities.ShopProduct;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ShopProductRepository extends MongoRepository<ShopProduct, String> {

    Optional<ShopProduct> findByShopIdAndProductId(String shopId, String productId );

    List<ShopProduct> findByProductId(String productId);

    List<ShopProduct> findByShopId(String shopId);

    boolean existsByShopIdAndProductId( String shopId,String productId);

    void deleteByShopIdAndProductId( String shopId, String productId);
}