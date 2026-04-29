package com.cowin.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record VerifyOtpRequest(
        @NotBlank @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid mobile number")
        String mobileNumber,
        @NotBlank @Pattern(regexp = "^\\d{6}$", message = "OTP should be 6 digits")
        String otp
) {}
