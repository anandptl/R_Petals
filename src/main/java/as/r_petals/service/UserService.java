package as.r_petals.service;

import as.r_petals.dto.common.ApiResponse;
import as.r_petals.dto.user.UpdateUserRequest;
import as.r_petals.dto.user.UserResponse;
import as.r_petals.entities.Users;
import as.r_petals.enums.OtpType;
import as.r_petals.enums.Role;
import as.r_petals.exception.BadRequestException;
import as.r_petals.exception.ConflictException;
import as.r_petals.exception.ResourceNotFoundException;
import as.r_petals.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OtpService otpService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private CurrentUserService currentUserService;

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

    public ApiResponse<Void> sendEmailOtp(String name, String email) {
        currentUserService.getCurrentUser();
        String normalizedEmail = email.trim().toLowerCase();

        userRepository.findByEmailIgnoreCase(normalizedEmail)
                .ifPresent(existing -> {
                    throw new ConflictException("Email is already in use");
                });

        String otp = otpService.generateOtp(normalizedEmail, OtpType.EMAIL);
        try {
            emailService.sendOtp(normalizedEmail, name.trim(), otp);
        } catch (RuntimeException ex) {
            otpService.invalidate(normalizedEmail, OtpType.EMAIL);
            throw ex;
        }

        return ApiResponse.success("Email OTP sent successfully");
    }

    public ApiResponse<Void> verifyEmailOtp(String name, String email, String otp) {
        Users user = currentUserService.getCurrentUser();
        String normalizedEmail = email.trim().toLowerCase();

        if (!otpService.verifyOtp(normalizedEmail, otp, OtpType.EMAIL)) {
            throw new BadRequestException("Invalid or expired OTP");
        }

        userRepository.findByEmailIgnoreCase(normalizedEmail)
                .filter(existing -> !existing.getId().equals(user.getId()))
                .ifPresent(existing -> {
                    throw new ConflictException("Email is already in use");
                });

        user.setEmail(normalizedEmail);
        user.setFullName(name.trim());
        user.setEmailVerified(true);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        return ApiResponse.success("Email verified successfully");
    }

    public ApiResponse<UserResponse> getCurrentUserProfile() {
        return ApiResponse.success("User profile fetched successfully",
                new UserResponse(currentUserService.getCurrentUser()));
    }

    public ApiResponse<UserResponse> updateCurrentUser(UpdateUserRequest request) {
        Users user = currentUserService.getCurrentUser();

        if (request.getFullName() != null && !request.getFullName().isBlank()) {
            user.setFullName(request.getFullName().trim());
        }

        user.setUpdatedAt(LocalDateTime.now());
        return ApiResponse.success("Profile updated successfully",
                new UserResponse(userRepository.save(user)));
    }
}