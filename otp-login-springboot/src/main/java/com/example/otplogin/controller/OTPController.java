package com.example.otplogin.controller;

import com.example.otplogin.service.OTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class OTPController {

    @Autowired
    private OTPService otpService;

    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestParam String phone) {
        otpService.sendOTP(phone);
        return ResponseEntity.ok("OTP sent to " + phone);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestParam String phone, @RequestParam String otp) {
        boolean isValid = otpService.verifyOTP(phone, otp);
        return isValid ? ResponseEntity.ok("Login Successful") :
                         ResponseEntity.status(401).body("Invalid OTP");
    }
}
