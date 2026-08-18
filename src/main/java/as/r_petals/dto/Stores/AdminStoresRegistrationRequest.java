package as.r_petals.dto.Stores;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class AdminStoresRegistrationRequest {

    @NotBlank(message = "Shopkeeper name is required")
    @Size(max = 200)
    private String shopkeeperName;


    @NotBlank(message = "Mobile number is required")
    @Pattern(
            regexp = "^[6-9][0-9]{9}$",
            message = "Invalid mobile number"
    )
    private String mobile;


    @Email(message = "Invalid email address")
    private String email;


    @NotBlank(message = "Shop name is required")
    @Size(max = 200)
    private String shopName;


    @NotBlank(message = "Country is required")
    private String country;


    @NotBlank(message = "Address is required")
    @Size(max = 300)
    private String address;


    @NotBlank(message = "City is required")
    private String city;


    @NotBlank(message = "State is required")
    private String state;


    @NotBlank(message = "Pincode is required")
    @Pattern(
            regexp = "^[1-9][0-9]{5}$",
            message = "Invalid pincode"
    )
    private String pincode;

    @NotBlank(message = "Latitude is required")
    private String latitude;


    @NotBlank(message = "Longitude is required")
    private String longitude;


    public AdminStoresRegistrationRequest() {
    }

    public String getShopkeeperName() {
        return shopkeeperName;
    }

    public void setShopkeeperName(String shopkeeperName) {
        this.shopkeeperName = shopkeeperName;
    }


    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }


    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }


    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }


    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }


    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}