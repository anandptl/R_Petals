package as.r_petals.dto.address;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Size;

public class AddressUpdateRequest {
    @Size(max = 300) private String address;
    private String city;
    private String state;
    @Pattern(regexp = "^[1-9][0-9]{5}$", message = "Invalid pincode") private String pinCode;
    private String latitude;
    private String longitude;
    public AddressUpdateRequest() {}
    public String getAddress(){return address;} public void setAddress(String v){address=v;}
    public String getCity(){return city;} public void setCity(String v){city=v;}
    public String getState(){return state;} public void setState(String v){state=v;}
    public String getPinCode(){return pinCode;} public void setPinCode(String v){pinCode=v;}
    public String getLatitude(){return latitude;} public void setLatitude(String v){latitude=v;}
    public String getLongitude(){return longitude;} public void setLongitude(String v){longitude=v;}
    @AssertTrue(message="At least one address field must be provided")
    public boolean isUpdateProvided(){ return address != null || city != null || state != null || pinCode != null || latitude != null || longitude != null; }
}
