package com.cowin.auth.controller;

import com.cowin.auth.dto.*;
import com.cowin.auth.service.JwtService;
import com.cowin.auth.service.OtpService;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final OtpService otpService;
    private final JwtService jwtService;

    @PostMapping("/send-otp")
    public ApiResponse sendOtp(@Valid @RequestBody SendOtpRequest request) {
        System.out.println("Controller hit: " + request.mobileNumber());
        otpService.sendOtp(request.mobileNumber());
        return new ApiResponse("OTP sent successfully");
    }

    @PostMapping("/verify-otp")
    public JwtResponse verifyOtp(@Valid @RequestBody VerifyOtpRequest request) {
        otpService.verifyOtp(request.mobileNumber(), request.otp());
        String token = jwtService.generateToken(request.mobileNumber());
        return new JwtResponse(token, "Bearer", jwtService.getExpirySeconds());
    }

    @GetMapping("/validate")
    public TokenValidationResponse validate(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        Claims claims = jwtService.validateAndParse(token);
        return new TokenValidationResponse(true, claims.getSubject(), "Token is valid");
    }
}
