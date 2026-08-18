package as.r_petals.dto.admin;

import java.time.LocalDateTime;

public class AdminStoreResponse {

    private String id;

    private String shopName;

    private String country;

    private String address;

    private String city;

    private String state;

    private String pincode;

    private String latitude;

    private String longitude;

    private String userName;

    private String mobileNumber;

    private String email;

    private boolean userActive;

    private boolean todayActive;

    private LocalDateTime lastActiveAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public AdminStoreResponse() {
    }

    public AdminStoreResponse(
            String id,
            String shopName,
            String country,
            String address,
            String city,
            String state,
            String pincode,
            String latitude,
            String longitude,
            String userName,
            String mobileNumber,
            String email,
            boolean userActive,
            boolean todayActive,
            LocalDateTime lastActiveAt,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.shopName = shopName;
        this.country = country;
        this.address = address;
        this.city = city;
        this.state = state;
        this.pincode = pincode;
        this.latitude = latitude;
        this.longitude = longitude;
        this.userName = userName;
        this.mobileNumber = mobileNumber;
        this.email = email;
        this.userActive = userActive;
        this.todayActive = todayActive;
        this.lastActiveAt = lastActiveAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public String getShopName() {
        return shopName;
    }

    public String getCountry() {
        return country;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getPincode() {
        return pincode;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getUserName() {
        return userName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getEmail() {
        return email;
    }

    public boolean isUserActive() {
        return userActive;
    }

    public boolean isTodayActive() {
        return todayActive;
    }

    public LocalDateTime getLastActiveAt() {
        return lastActiveAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}