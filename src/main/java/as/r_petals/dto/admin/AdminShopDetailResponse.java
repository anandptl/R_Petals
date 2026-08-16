package as.r_petals.dto.admin;

import as.r_petals.dto.product.ProductResponse;
import as.r_petals.dto.Stores.StoresResponse;

import java.util.List;

public class AdminShopDetailResponse {

    private StoresResponse shop;

    private UserInfo user;

    private List<ProductResponse> products;

    public AdminShopDetailResponse() {
    }

    public AdminShopDetailResponse(StoresResponse shop, UserInfo user, List<ProductResponse> products) {
        this.shop = shop;
        this.user = user;
        this.products = products;
    }

    public StoresResponse getShop() {
        return shop;
    }

    public UserInfo getUser() {
        return user;
    }

    public List<ProductResponse> getProducts() {
        return products;
    }

    public static class UserInfo {

        private String id;
        private String fullName;
        private String mobileNumber;
        private String email;
        private boolean verified;
        private boolean emailVerified;
        private String role;
        private boolean active;

        public UserInfo() {
        }

        public UserInfo(String id, String fullName, String mobileNumber, String email, boolean verified, boolean emailVerified, String role, boolean active) {
            this.id = id;
            this.fullName = fullName;
            this.mobileNumber = mobileNumber;
            this.email = email;
            this.verified = verified;
            this.emailVerified = emailVerified;
            this.role = role;
            this.active = active;
        }

        public String getId() {
            return id;
        }

        public String getFullName() {
            return fullName;
        }

        public String getMobileNumber() {
            return mobileNumber;
        }

        public String getEmail() {
            return email;
        }

        public boolean isVerified() {
            return verified;
        }

        public boolean isEmailVerified() {
            return emailVerified;
        }

        public String getRole() {
            return role;
        }

        public boolean isActive() {
            return active;
        }
    }
}