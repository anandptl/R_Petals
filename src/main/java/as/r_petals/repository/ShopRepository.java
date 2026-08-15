package as.r_petals.repository;

import as.r_petals.entities.Shops;
import as.r_petals.enums.ShopStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShopRepository extends MongoRepository<Shops, String> {

    boolean existsByUserId(String userId);
    
    Optional<Shops> findByUserId(String userId);

    List<Shops> findByStatus(ShopStatus status);

//    Admin dashbord ke liye
    long countByStatus(ShopStatus status);


}
