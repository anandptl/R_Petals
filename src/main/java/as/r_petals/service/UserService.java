package as.r_petals.service;

import as.r_petals.entities.Users;
import as.r_petals.enums.Role;
import as.r_petals.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Find User
    public Optional<Users> findByMobileNumber(String mobileNumber) {
        return userRepository.findByMobileNumber(mobileNumber);
    }

    // Check User Exists
    public boolean existsByMobileNumber(String mobileNumber) {
        return userRepository.existsByMobileNumber(mobileNumber);
    }

    // Register New User
    public Users createUser(String mobileNumber) {

        Users user = new Users();

        user.setMobileNumber(mobileNumber);
        user.setVerified(true);
        user.setRole(Role.USER);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }

    // Update User
    public Users save(Users user) {

        user.setUpdatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }

}