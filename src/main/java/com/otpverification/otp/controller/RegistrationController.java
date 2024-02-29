package com.otpverification.otp.controller;

import com.otpverification.otp.Dto.UserDto;
import com.otpverification.otp.entity.User;
import com.otpverification.otp.service.EmailService;
import com.otpverification.otp.service.EmailVerificationService;
import com.otpverification.otp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class RegistrationController {
    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private EmailVerificationService emailVerificationService;

    @PostMapping("/register")
    public Map<String,String> registerUser(@RequestBody UserDto user){
        userService.registerUser(user);
        emailService.sendOtp(user.getEmail());
        Map<String,String> response = new HashMap<>();
        response.put("status","success");
        response.put("message","User Registered successfully. check your mail for OTP");
        return response;

    }
    @PostMapping("/verify-otp")
    public Map<String,String> verifyOtp(@RequestParam String email, @RequestParam String Otp){
        return emailVerificationService.verifyOtp(email,Otp);
    }


}
