package as.r_petals.repository;

import as.r_petals.entities.Shops;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoresRepository extends MongoRepository<Shops, String> {

    boolean existsByUserId(String userId);
    
    Optional<Shops> findByUserId(String userId);
}
