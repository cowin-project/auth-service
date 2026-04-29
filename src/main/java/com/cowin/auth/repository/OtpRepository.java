package com.cowin.auth.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OtpRepository {
    private final StringRedisTemplate redisTemplate;

    public void saveOtp(String mobileNumber, String otp, Duration ttl) {
        redisTemplate.opsForValue().set("otp:" + mobileNumber, otp, ttl);
    }

    public Optional<String> getOtp(String mobileNumber) {
        return Optional.ofNullable(redisTemplate.opsForValue().get("otp:" + mobileNumber));
    }

    public void deleteOtp(String mobileNumber) {
        redisTemplate.delete("otp:" + mobileNumber);
    }

    public Long incrementRateLimit(String mobileNumber, Duration ttl) {
        String key = "otp:req:" + mobileNumber;
        Long count = redisTemplate.opsForValue().increment(key);
        if (count != null && count == 1) {
            redisTemplate.expire(key, ttl);
        }
        return count;
    }
}
