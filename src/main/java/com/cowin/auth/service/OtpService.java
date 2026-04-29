package com.cowin.auth.service;

import com.cowin.auth.exception.AuthException;
import com.cowin.auth.repository.OtpRepository;
import com.cowin.auth.util.OtpGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class OtpService {
    private static final Duration OTP_TTL = Duration.ofMinutes(2);
    private static final Duration RATE_LIMIT_WINDOW = Duration.ofMinutes(5);
    private static final long MAX_OTP_REQUESTS = 3;

    private final OtpRepository otpRepository;
    private final OtpGenerator otpGenerator;

    public void sendOtp(String mobileNumber) {
        Long count = otpRepository.incrementRateLimit(mobileNumber, RATE_LIMIT_WINDOW);
        if (count != null && count > MAX_OTP_REQUESTS) {
            throw new AuthException("Too many OTP requests. Please try again after 5 minutes.");
        }
        String otp = otpGenerator.generateOtp();
        otpRepository.saveOtp(mobileNumber, otp, OTP_TTL);
        log.info("Generated OTP for mobile={} otp={} (for demo/logging only)", mobileNumber, otp);
    }

    public void verifyOtp(String mobileNumber, String otp) {
        String savedOtp = otpRepository.getOtp(mobileNumber)
                .orElseThrow(() -> new AuthException("OTP expired or not found"));
        if (!savedOtp.equals(otp)) {
            throw new AuthException("Invalid OTP");
        }
        otpRepository.deleteOtp(mobileNumber);
    }
}
