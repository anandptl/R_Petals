package as.r_petals.service;

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
        Twilio.init(sid, token);
    }

    public void sendOtps(String mobileNumber, String otp) {
        String formattedNumber = mobileNumber.startsWith("+") ? mobileNumber : "+91" + mobileNumber;

        Message.creator(
                new PhoneNumber(formattedNumber),
                new PhoneNumber(phoneNumber),
                "Your R-Petals OTP is: " + otp + ". Valid for 5 minutes."
        ).create();
    }
}