package as.r_petals.repository;

import as.r_petals.entities.RevokedToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RevokedTokenRepository extends MongoRepository<RevokedToken, String> {
    boolean existsById(String jti);
}