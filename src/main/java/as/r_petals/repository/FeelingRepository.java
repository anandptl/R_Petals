package as.r_petals.repository;

import as.r_petals.entities.Feeling;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FeelingRepository extends MongoRepository<Feeling, String> {

    List<Feeling> findByActiveTrue();

    long countByActiveTrue();
}
