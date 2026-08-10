package as.r_petals.dto.shop;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ShopRegistrationRequest {
    @NotBlank(message = "Shop name is required")
    @Size(max = 100)
    private String shopName;
    @NotBlank(message = "Shopkeeper name is required")
    @Size(max = 100)
    private String shopkeeperName;
    @Size(max = 20)
    private String gstNumber;
    @NotBlank(message = "Address is required")
    @Size(max = 300)
    private String address;
    @NotBlank(message = "City is required")
    private String city;
    @NotBlank(message = "State is required")
    private String state;
    @NotBlank(message = "Pincode is required")
    @Pattern(regexp = "^[1-9][0-9]{5}$", message = "Invalid pincode")
    private String pincode;
    private String latitude;
    private String longitude;
    private String shopImage;
    public ShopRegistrationRequest() {}
    public String getShopName(){return shopName;} public void setShopName(String v){shopName=v;}
    public String getShopkeeperName(){return shopkeeperName;} public void setShopkeeperName(String v){shopkeeperName=v;}
    public String getGstNumber(){return gstNumber;} public void setGstNumber(String v){gstNumber=v;}
    public String getAddress(){return address;} public void setAddress(String v){address=v;}
    public String getCity(){return city;} public void setCity(String v){city=v;}
    public String getState(){return state;} public void setState(String v){state=v;}
    public String getPincode(){return pincode;} public void setPincode(String v){pincode=v;}
    public String getLatitude(){return latitude;} public void setLatitude(String v){latitude=v;}
    public String getLongitude(){return longitude;} public void setLongitude(String v){longitude=v;}
    public String getShopImage(){return shopImage;} public void setShopImage(String v){shopImage=v;}
}
