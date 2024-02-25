package com.otpverification.otp.service;
import static com.otpverification.otp.service.EmailVerificationService.emailOtpMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.util.Random;


@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;
    @Autowired
    private UserService userService;

    public String OtpGenerator(){
        Random random = new Random();
        int otpDigits = 6; // 6-digit OTP
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < otpDigits; i++) {
            otp.append(random.nextInt(10));
            // Append a random digit (0-9)
        }
        return otp.toString();
    }

    public void sendOtp(String email){
        String otp = OtpGenerator();
        //save otp temporarely
        emailOtpMapping.put(email,otp);
        sendSimpleMessage(email,"Otp for verification is",
                "Your otp is :"+otp);
    }

    public void sendSimpleMessage(String to, String subject, String text) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true); // true indicates html

            emailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
