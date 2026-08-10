package as.r_petals.service;

import as.r_petals.entities.Users;
import as.r_petals.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository repo;

    @Override
    public UserDetails loadUserByUsername(String mobileNumber)
            throws UsernameNotFoundException {

        Users user = repo.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new UsernameNotFoundException(
                                "User not found : " + mobileNumber));

        return User.builder()
                .username(user.getMobileNumber())
                .password("")
                .roles(user.getRole().name())
                .disabled(!user.isActive())
                .build();
    }
}