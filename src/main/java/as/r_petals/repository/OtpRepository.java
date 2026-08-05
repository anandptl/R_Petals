package as.r_petals.repository;

import as.r_petals.entities.Otp;
import as.r_petals.enums.OtpType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpRepository extends MongoRepository<Otp, String> {

    Optional<Otp> findByIdentifierAndType(
            String identifier,
            OtpType type
    );

    void deleteByIdentifierAndType(
            String identifier,
            OtpType type
    );
}