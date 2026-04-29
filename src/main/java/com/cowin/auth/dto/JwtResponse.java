package com.cowin.auth.dto;

public record JwtResponse(String token, String tokenType, long expiresInSeconds) {}
