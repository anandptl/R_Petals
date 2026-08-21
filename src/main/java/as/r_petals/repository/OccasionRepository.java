package as.r_petals.repository;

import as.r_petals.entities.Occasion;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OccasionRepository extends MongoRepository<Occasion, String> {

    List<Occasion> findByActiveTrueAndOccasionDateBetween(
            LocalDateTime start,
            LocalDateTime end
    );
}