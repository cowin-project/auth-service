package com.cowin.auth.repository;


import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class OtpRepository {

    private final Map<String, String> store = new HashMap<>();
    private final Map<String, Long> rateLimit = new HashMap<>();

    public void saveOtp(String mobileNumber, String otp, Duration ttl) {
        store.put(mobileNumber, otp);
    }

    public Optional<String> getOtp(String mobileNumber) {
        return Optional.ofNullable(store.get(mobileNumber));
    }

    public void deleteOtp(String mobileNumber) {
        store.remove(mobileNumber);
    }

    public Long incrementRateLimit(String mobileNumber, Duration ttl) {
        long count = rateLimit.getOrDefault(mobileNumber, 0L) + 1;
        rateLimit.put(mobileNumber, count);
        return count;
    }
}