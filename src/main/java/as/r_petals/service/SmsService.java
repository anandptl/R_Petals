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

    public void sendOtp(String mobileNumber, String otp) {

        Message.creator(
                new PhoneNumber(mobileNumber),     // Receiver
                new PhoneNumber(phoneNumber),      // Twilio Number
                "Your R-Petals OTP is: " + otp + ". Valid for 5 minutes."
        ).create();
    }
}