package as.r_petals.dto.admin;

import java.time.LocalDateTime;

public class StoreStatusResponse {

    private String storeId;

    private String shopName;

    private String userName;

    private String mobileNumber;

    private boolean todayActive;

    private LocalDateTime lastActiveAt;

    public StoreStatusResponse() {
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public boolean isTodayActive() {
        return todayActive;
    }

    public void setTodayActive(boolean todayActive) {
        this.todayActive = todayActive;
    }

    public LocalDateTime getLastActiveAt() {
        return lastActiveAt;
    }

    public void setLastActiveAt(LocalDateTime lastActiveAt) {
        this.lastActiveAt = lastActiveAt;
    }
}