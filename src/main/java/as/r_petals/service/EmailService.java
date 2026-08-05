package as.r_petals.service;


import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Async
    public void sendOtp(String to, String name, String otp) {
        try {

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(new InternetAddress("yourgmail@gmail.com", "R_Petals"));

            helper.setTo(to);

            helper.setSubject("Verify Your R_Petals Account");

            String text =

                    "<div style='font-family:Arial,sans-serif;max-width:600px;margin:auto;padding:20px;border:1px solid #eee;border-radius:10px;'>"

                            + "<h2 style='color:#E91E63;text-align:center;'>🌸 Welcome to R_Petals</h2>"

                            + "<p>Hello <b>" + name + "</b>,</p>"

                            + "<p>Thank you for registering with <b>R_Petals</b>.</p>"

                            + "<p>Please use the following One-Time Password (OTP) to verify your email address.</p>"

                            + "<div style='text-align:center;margin:25px 0;'>"

                            + "<span style='display:inline-block;padding:15px 30px;"
                            + "font-size:28px;font-weight:bold;"
                            + "letter-spacing:6px;"
                            + "background:#FCE4EC;"
                            + "color:#E91E63;"
                            + "border-radius:8px;'>"

                            + otp

                            + "</span>"

                            + "</div>"

                            + "<p><b>OTP Validity:</b> 5 Minutes</p>"

                            + "<p>If you did not create an account on R_Petals, please ignore this email.</p>"

                            + "<hr>"

                            + "<p style='font-size:13px;color:gray;'>"
                            + "This is an automated email. Please do not reply."
                            + "</p>"

                            + "<p style='color:#444;'>"
                            + "Regards,<br>"
                            + "<b>Team R_Petals 🌸</b>"
                            + "</p>"

                            + "</div>";

            helper.setText(text, true);

            mailSender.send(message);

        } catch (Exception e) {

            e.printStackTrace();

        }
    }
}