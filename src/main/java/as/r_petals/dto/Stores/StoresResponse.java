package as.r_petals.dto.Stores;

import as.r_petals.entities.Stores;

import java.time.LocalDateTime;

public class StoresResponse {
    private String id;
    private String shopName;
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

    public StoresResponse(Stores store) {
        id = store.getId();
        shopName = store.getShopName();
        address = store.getAddress();
        city = store.getCity();
        state = store.getState();
        pincode = store.getPincode();
        latitude = store.getLatitude();
        longitude = store.getLongitude();
        todayActive = store.isTodayActive();
        createdAt = store.getCreatedAt();
        updatedAt = store.getUpdatedAt();
    }

    public String getId() {
        return id;
    }

    public String getShopName() {return shopName;}

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
