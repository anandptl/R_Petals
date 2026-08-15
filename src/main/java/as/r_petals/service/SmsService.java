package as.r_petals.service;

import as.r_petals.exception.BadRequestException;
import as.r_petals.exception.ExternalServiceException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;

@Service
public class SmsService {

    private static final String MSG91_FLOW_URL = "https://control.msg91.com/api/v5/flow";

    @Value("${msg91.auth-key}")
    private String authKey;

    @Value("${msg91.template-id}")
    private String templateId;

    @Value("${msg91.otp-variable-name:OTP}")
    private String otpVariableName;

    private final HttpClient httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void init() {

        if (authKey == null || authKey.isBlank()) {
            throw new IllegalStateException("MSG91 auth-key is missing");
        }

        if (templateId == null || templateId.isBlank()) {
            throw new IllegalStateException("MSG91 flow/template id is missing");
        }
    }

    public void sendOtps(String mobileNumber, String otp) {

        String formattedNumber = formatIndianNumber(mobileNumber);

        Map<String, Object> recipient = Map.of("mobiles", formattedNumber, otpVariableName, otp);

        Map<String, Object> payload = Map.of("flow_id", templateId, "recipients", new Object[]{recipient});

        try {

            String body = objectMapper.writeValueAsString(payload);

            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(MSG91_FLOW_URL))
                            .header("Content-Type", "application/json")
                            .header("authkey", authKey)
                            .POST(HttpRequest.BodyPublishers.ofString(body))
                            .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() / 100 != 2) {
                throw new ExternalServiceException("MSG91 responded with status " + response.statusCode() + ": " + response.body(), null);
            }

            JsonNode json = objectMapper.readTree(response.body());

            String type = json.has("type") ? json.get("type").asText() : "";

            if (!"success".equalsIgnoreCase(type)) {
                throw new ExternalServiceException("MSG91 failed to send OTP: " + response.body(), null);
            }

        } catch (ExternalServiceException ex) {
            throw ex;

        } catch (Exception ex) {
            throw new ExternalServiceException("Unable to send SMS via MSG91", ex);
        }
    }

    /*
     * Convert Indian mobile number to:
     * 919876543210
     */
    private String formatIndianNumber(String mobileNumber) {

        if (mobileNumber == null || mobileNumber.isBlank()) {
            throw new BadRequestException("Mobile number is required");
        }

        String number = mobileNumber.trim();

        // +919876543210
        if (number.startsWith("+91") && number.length() == 13) {
            return number.substring(1);
        }

        // 919876543210
        if (number.startsWith("91") && number.length() == 12) {
            return number;
        }

        // 9876543210
        if (number.matches("^[6-9][0-9]{9}$")) {
            return "91" + number;
        }

        throw new BadRequestException("Invalid mobile number");
    }
}