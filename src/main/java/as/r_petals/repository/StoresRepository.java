package as.r_petals.repository;

import as.r_petals.entities.Stores;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoresRepository extends MongoRepository<Stores, String> {

    boolean existsByUserId(String userId);
    
    Optional<Stores> findByUserId(String userId);
}
