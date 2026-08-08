package as.r_petals.repository;

import as.r_petals.entities.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

    boolean existsBySubCategoryIdAndProductNameIgnoreCase(String subcategoryId, String name);

    boolean existsBySubCategoryId(String subCategoryId);
}