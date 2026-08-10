package as.r_petals.dto.address;

import as.r_petals.entities.UserAddress;
import java.time.LocalDateTime;

public class AddressResponse {
    private String id; private String address; private String city; private String state; private String pinCode;
    private String latitude; private String longitude; private boolean defaultAddress; private LocalDateTime createdAt; private LocalDateTime updatedAt;
    public AddressResponse() {}
    public AddressResponse(UserAddress a){id=a.getId(); address=a.getAddress(); city=a.getCity(); state=a.getState(); pinCode=a.getPinCode(); latitude=a.getLatitude(); longitude=a.getLongitude(); defaultAddress=a.isDefaultAddress(); createdAt=a.getCreatedAt(); updatedAt=a.getUpdatedAt();}
    public String getId(){return id;} public String getAddress(){return address;} public String getCity(){return city;} public String getState(){return state;} public String getPinCode(){return pinCode;}
    public String getLatitude(){return latitude;} public String getLongitude(){return longitude;} public boolean isDefaultAddress(){return defaultAddress;} public LocalDateTime getCreatedAt(){return createdAt;} public LocalDateTime getUpdatedAt(){return updatedAt;}
}
