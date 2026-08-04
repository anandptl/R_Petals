package as.r_petals.repository;

import as.r_petals.entities.Users;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<Users, String> {

    Optional<Users> findByMobileNumber(String mobileNumber);

    boolean existsByMobileNumber(String mobileNumber);

}
