package as.r_petals.service;

import as.r_petals.exception.BadRequestException;
import as.r_petals.exception.ExternalServiceException;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsService {

    @Value("${twilio.account_sid}")
    private String sid;

    @Value("${twilio.auth_token}")
    private String token;

    @Value("${twilio.phone_number}")
    private String phoneNumber;


    @PostConstruct
    public void init() {
        if (sid == null || sid.isBlank() || token == null || token.isBlank() || phoneNumber == null || phoneNumber.isBlank()) {
            throw new IllegalStateException("Twilio configuration is missing");
        }
        Twilio.init(sid, token);
    }

    public void sendOtps(String mobileNumber, String otp) {
        String formattedNumber = formatIndianNumber(mobileNumber);
        try {
            Message.creator(
                    new PhoneNumber(formattedNumber),
                    new PhoneNumber(phoneNumber),
                    "Your R-Petals OTP is: " + otp + ". Valid for 5 minutes."
            ).create();
        } catch (Exception ex) {
            throw new ExternalServiceException("Unable to send SMS", ex);
        }
    }

    private String formatIndianNumber(String mobileNumber) {
        if (mobileNumber == null || mobileNumber.isBlank()) {
            throw new BadRequestException("Mobile number is required");
        }
        String number = mobileNumber.trim();
        if (number.startsWith("+91") && number.length() == 13) return number;
        if (number.startsWith("91") && number.length() == 12) return "+" + number;
        if (number.matches("^[6-9][0-9]{9}$")) return "+91" + number;
        throw new BadRequestException("Invalid mobile number");
    }

}