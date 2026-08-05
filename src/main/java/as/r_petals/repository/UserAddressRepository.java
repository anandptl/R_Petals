package as.r_petals.repository;

import as.r_petals.entities.UserAddress;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserAddressRepository extends MongoRepository<UserAddress, String> {

    List<UserAddress> findByUserId(String userId);

    Optional<UserAddress> findByUserIdAndDefaultAddressTrue(String userId);

}
