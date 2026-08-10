package as.r_petals.dto.shop;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Size;

public class ShopUpdateRequest {
    @Size(max = 100) private String shopName;
    @Size(max = 100) private String shopkeeperName;
    @Size(max = 20) private String gstNumber;
    @Size(max = 300) private String address;
    private String city;
    private String state;
    @Pattern(regexp = "^[1-9][0-9]{5}$", message = "Invalid pincode") private String pincode;
    private String latitude;
    private String longitude;
    private String shopImage;
    public ShopUpdateRequest() {}
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
    @AssertTrue(message="At least one shop field must be provided")
    public boolean isUpdateProvided(){ return shopName != null || shopkeeperName != null || gstNumber != null || address != null || city != null || state != null || pincode != null || latitude != null || longitude != null || shopImage != null; }
}
