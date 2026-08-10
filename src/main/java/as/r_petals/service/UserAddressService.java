package as.r_petals.service;

import as.r_petals.dto.address.AddressRequest;
import as.r_petals.dto.address.AddressResponse;
import as.r_petals.dto.address.AddressUpdateRequest;
import as.r_petals.entities.UserAddress;
import as.r_petals.entities.Users;
import as.r_petals.exception.ForbiddenException;
import as.r_petals.exception.ResourceNotFoundException;
import as.r_petals.repository.UserAddressRepository;
import as.r_petals.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserAddressService {

    @Autowired
    private UserAddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CurrentUserService currentUserService;

    public AddressResponse saveAddress(AddressRequest request) {
        Users user = currentUserService.getCurrentUser();
        String userId = user.getId();

        UserAddress address = new UserAddress();
        address.setUserId(userId);
        address.setAddress(request.getAddress().trim());
        address.setCity(request.getCity().trim());
        address.setState(request.getState().trim());
        address.setPinCode(request.getPinCode());
        address.setLatitude(request.getLatitude());
        address.setLongitude(request.getLongitude());
        address.setDefaultAddress(addressRepository.findByUserId(userId).isEmpty());
        address.setCreatedAt(LocalDateTime.now());
        address.setUpdatedAt(LocalDateTime.now());

        return new AddressResponse(addressRepository.save(address));
    }

//  change default address .......
public AddressResponse updateAddress(String addressId, AddressUpdateRequest request) {
    String userId = currentUserService.getCurrentUserId();
    UserAddress address = getOwnedAddress(addressId, userId);

    if (request.getAddress() != null) address.setAddress(request.getAddress().trim());
    if (request.getCity() != null) address.setCity(request.getCity().trim());
    if (request.getState() != null) address.setState(request.getState().trim());
    if (request.getPinCode() != null) address.setPinCode(request.getPinCode());
    if (request.getLatitude() != null) address.setLatitude(request.getLatitude());
    if (request.getLongitude() != null) address.setLongitude(request.getLongitude());
    address.setUpdatedAt(LocalDateTime.now());

    return new AddressResponse(addressRepository.save(address));
}

    public void setDefaultAddress(String addressId) {
        String userId = currentUserService.getCurrentUserId();
        UserAddress newDefault = getOwnedAddress(addressId, userId);

        addressRepository.findByUserIdAndDefaultAddressTrue(userId)
                .filter(existing -> !existing.getId().equals(addressId))
                .ifPresent(existing -> {
                    existing.setDefaultAddress(false);
                    existing.setUpdatedAt(LocalDateTime.now());
                    addressRepository.save(existing);
                });

        newDefault.setDefaultAddress(true);
        newDefault.setUpdatedAt(LocalDateTime.now());
        addressRepository.save(newDefault);
    }

    public List<AddressResponse> getMyAddresses() {
        String userId = currentUserService.getCurrentUserId();
        return addressRepository.findByUserId(userId)
                .stream().map(AddressResponse::new).toList();
    }

    public void deleteAddress(String addressId) {
        String userId = currentUserService.getCurrentUserId();
        UserAddress address = getOwnedAddress(addressId, userId);
        addressRepository.delete(address);

        if (address.isDefaultAddress()) {
            addressRepository.findByUserId(userId).stream().findFirst().ifPresent(next -> {
                next.setDefaultAddress(true);
                next.setUpdatedAt(LocalDateTime.now());
                addressRepository.save(next);
            });
        }
    }

    private UserAddress getOwnedAddress(String addressId, String userId) {
        UserAddress address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));
        if (!userId.equals(address.getUserId())) {
            throw new ForbiddenException("You do not have permission to access this address");
        }
        return address;
    }
}