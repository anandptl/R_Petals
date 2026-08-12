package as.r_petals.repository;

import as.r_petals.entities.ProductImage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductImageRepository
        extends MongoRepository<ProductImage, String> {

    List<ProductImage> findByProductIdOrderByDisplayOrderAsc(String productId);

    void deleteByProductId(String productId);
}