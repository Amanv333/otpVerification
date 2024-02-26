package com.otpverification.otp.service;

import com.otpverification.otp.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class EmailVerificationService {
    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;
    static final Map<String,String> emailOtpMapping = new HashMap<>();

    public Map<String,String> verifyOtp(String email,String otp) {

        String storeOtp = emailOtpMapping.get(email);

        Map<String,String> response = new HashMap<>();
        if(storeOtp != null && storeOtp.equals(otp)) {
            User user = userService.getUserByEmail(email);
            if (user != null){
                emailOtpMapping.remove(email);
                userService.verifyEmail(user);
                response.put("status","success");
                response.put("message","Email verified successfully");
            } else {
                response.put("status","errror");
                response.put("message","User not found");
            }
        } else{
            response.put("status","error");
            response.put("message","Invalid Otp");
        }
        return response;
    }


    public Map<String, String> sendOtpForLogin(String email) {
        if(userService.isEmailVerified(email)){
            String storeOtp = emailService.OtpGenerator();
            emailOtpMapping.put(email,storeOtp);

            emailService.sendOtp(email);

            Map<String,String> response = new HashMap<>();
            response.put("status","success");
            response.put("message","Otp sent successfully");
            return response;

        }
        else {
            Map<String,String> response = new HashMap<>();
            response.put("status","error");
            response.put("message","email is not verified");
            return response;
        }

    }

    public Map<String, String> verifyOtpForLogin(String email, String otp) {
        String storeOtp = emailOtpMapping.get(email);

        Map<String,String> response = new HashMap<>();
        if (storeOtp!=null && storeOtp.equals(otp)){
            //valid otp
            emailOtpMapping.remove(email);
            response.put("status","success");
            response.put("message","otp verified successfully");
            return response;
        }
        else {
            //invalid Otp
            response.put("status","error");
            response.put("message","Invalid Otp");
            return response;
        }


    }
}
