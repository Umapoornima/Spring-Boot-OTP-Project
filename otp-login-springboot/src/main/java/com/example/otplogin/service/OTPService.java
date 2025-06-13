package com.example.otplogin.service;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class OTPService {
    private final Map<String, String> otpStore = new HashMap<>();
    private final SnsClient snsClient;

    public OTPService() {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(
            System.getenv("AWS_ACCESS_KEY_ID"),
            System.getenv("AWS_SECRET_ACCESS_KEY")
        );
        snsClient = SnsClient.builder()
                .region(Region.AP_SOUTH_1)
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }

    public String generateOTP() {
        return String.valueOf(new Random().nextInt(900000) + 100000);
    }

    public void sendOTP(String phoneNumber) {
        String otp = generateOTP();
        otpStore.put(phoneNumber, otp);
        snsClient.publish(PublishRequest.builder()
                .phoneNumber("+91" + phoneNumber)
                .message("Your OTP is: " + otp)
                .build());
    }

    public boolean verifyOTP(String phoneNumber, String otp) {
        return otp.equals(otpStore.get(phoneNumber));
    }
}
