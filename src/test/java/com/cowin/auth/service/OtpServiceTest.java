package com.cowin.auth.service;

import com.cowin.auth.exception.AuthException;
import com.cowin.auth.repository.OtpRepository;
import com.cowin.auth.util.OtpGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class OtpServiceTest {

    private OtpRepository otpRepository;
    private OtpService otpService;

    @BeforeEach
    void setup() {
        otpRepository = mock(OtpRepository.class);
        OtpGenerator otpGenerator = mock(OtpGenerator.class);
        when(otpGenerator.generateOtp()).thenReturn("123456");
        otpService = new OtpService(otpRepository, otpGenerator);
    }

    @Test
    void shouldThrowWhenTooManyRequests() {
        when(otpRepository.incrementRateLimit(eq("9999999999"), any(Duration.class))).thenReturn(4L);
        assertThrows(AuthException.class, () -> otpService.sendOtp("9999999999"));
    }

    @Test
    void shouldThrowWhenOtpMismatch() {
        when(otpRepository.getOtp("9999999999")).thenReturn(Optional.of("111111"));
        assertThrows(AuthException.class, () -> otpService.verifyOtp("9999999999", "222222"));
    }
}
