package as.r_petals.dto.shop;

import as.r_petals.entities.Shops;
import as.r_petals.enums.ShopStatus;
import java.time.LocalDateTime;

public class ShopResponse {
    private String id;
    private String shopName;
    private String shopkeeperName;
    private String gstNumber;
    private String address;
    private String city;
    private String state;
    private String pincode;
    private String latitude;
    private String longitude;
    private String shopImage;
    private ShopStatus status;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    public ShopResponse() {}
    public ShopResponse(Shops shop){
        id=shop.getId(); shopName=shop.getShopName(); shopkeeperName=shop.getShopkeeperName(); gstNumber=shop.getGstNumber();
        address=shop.getAddress(); city=shop.getCity(); state=shop.getState(); pincode=shop.getPincode(); latitude=shop.getLatitude(); longitude=shop.getLongitude();
        shopImage=shop.getShopImage(); status=shop.getStatus(); active=shop.isActive(); createdAt=shop.getCreatedAt(); updatedAt=shop.getUpdatedAt();
    }
    public String getId(){return id;} public String getShopName(){return shopName;} public String getShopkeeperName(){return shopkeeperName;} public String getGstNumber(){return gstNumber;}
    public String getAddress(){return address;} public String getCity(){return city;} public String getState(){return state;} public String getPincode(){return pincode;}
    public String getLatitude(){return latitude;} public String getLongitude(){return longitude;} public String getShopImage(){return shopImage;} public ShopStatus getStatus(){return status;}
    public boolean isActive(){return active;} public LocalDateTime getCreatedAt(){return createdAt;} public LocalDateTime getUpdatedAt(){return updatedAt;}
}
