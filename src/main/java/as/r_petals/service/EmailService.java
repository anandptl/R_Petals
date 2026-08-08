package as.r_petals.service;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);


    @Autowired
    private JavaMailSender mailSender;


    @Value("${spring.mail.username}")
    private String senderEmail;


    public void sendOtp(String to, String name, String otp) {

        try {

            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(new InternetAddress( senderEmail,"R_Petals" ) );

            helper.setTo(to);

            helper.setSubject("Verify Your R_Petals Account" );


            String text =
                    "<div style='font-family:Arial,sans-serif;"
                            + "max-width:600px;margin:auto;padding:20px;"
                            + "border:1px solid #eee;border-radius:10px;'>"

                            + "<h2 style='color:#E91E63;"
                            + "text-align:center;'>"
                            + "🌸 Welcome to R_Petals"
                            + "</h2>"

                            + "<p>Hello <b>"
                            + name
                            + "</b>,</p>"

                            + "<p>"
                            + "Please use the OTP below to verify "
                            + "your email address."
                            + "</p>"

                            + "<div style='text-align:center;"
                            + "margin:25px 0;'>"

                            + "<span style='display:inline-block;"
                            + "padding:15px 30px;font-size:28px;"
                            + "font-weight:bold;letter-spacing:6px;"
                            + "background:#FCE4EC;color:#E91E63;"
                            + "border-radius:8px;'>"

                            + otp

                            + "</span>"
                            + "</div>"

                            + "<p><b>OTP Validity:</b> 5 Minutes</p>"

                            + "<p>"
                            + "If you did not request this OTP, "
                            + "please ignore this email."
                            + "</p>"

                            + "<hr>"

                            + "<p style='font-size:13px;color:gray;'>"
                            + "This is an automated email."
                            + "</p>"

                            + "<p>"
                            + "Regards,<br>"
                            + "<b>Team R_Petals 🌸</b>"
                            + "</p>"

                            + "</div>";


            helper.setText( text,true );
            mailSender.send(message);

        } catch (Exception e) {

            log.error("Failed to send OTP email to {}", to, e);

            throw new RuntimeException("Unable to send OTP email");
        }
    }
}