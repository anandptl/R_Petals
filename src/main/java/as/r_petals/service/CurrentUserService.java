package as.r_petals.service;

import as.r_petals.entities.Users;
import as.r_petals.exception.UnauthorizedException;
import as.r_petals.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {

    private final UserRepository userRepository;

    public CurrentUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Users getCurrentUser() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated() ||
                authentication.getName() == null ||
                "anonymousUser".equals(authentication.getName())) {
            throw new UnauthorizedException("Authentication required");
        }

        return userRepository.findByMobileNumber(authentication.getName())
                .orElseThrow(() -> new UnauthorizedException("User account not found"));
    }

    public String getCurrentUserId() {
        return getCurrentUser().getId();
    }
}
