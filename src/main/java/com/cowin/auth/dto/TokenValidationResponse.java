package com.cowin.auth.dto;

public record TokenValidationResponse(boolean valid, String mobileNumber, String message) {}
