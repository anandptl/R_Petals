package as.r_petals.repository;

import as.r_petals.entities.SubCategory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubCategoryRepository extends MongoRepository<SubCategory, String> {

    Optional<SubCategory> findByCategoryIdAndSubCategoryNameIgnoreCase( String categoryId, String name );

    boolean existsByCategoryId(String categoryId);

    List<SubCategory> findByCategoryId(String categoryId);
}