package com.cowin.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record SendOtpRequest(
        @NotBlank @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid mobile number")
        String mobileNumber
) {}
