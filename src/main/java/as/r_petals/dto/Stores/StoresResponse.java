package as.r_petals.dto.Stores;

import as.r_petals.entities.Shops;

import java.time.LocalDateTime;

public class StoresResponse {
    private String id;
    private String address;
    private String city;
    private String state;
    private String pincode;
    private String latitude;
    private String longitude;
    private boolean todayActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public StoresResponse() {
    }

    public StoresResponse(Shops shop) {
        id = shop.getId();
        address = shop.getAddress();
        city = shop.getCity();
        state = shop.getState();
        pincode = shop.getPincode();
        latitude = shop.getLatitude();
        longitude = shop.getLongitude();
        todayActive = shop.isTodayActive();
        createdAt = shop.getCreatedAt();
        updatedAt = shop.getUpdatedAt();
    }

    public String getId() {
        return id;
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

    public boolean isTodayActive() {
        return todayActive;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
