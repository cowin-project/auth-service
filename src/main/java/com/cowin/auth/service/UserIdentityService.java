package com.cowin.auth.service;

import com.cowin.auth.entity.UserIdentity;
import com.cowin.auth.repository.UserIdentityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserIdentityService {

    private final UserIdentityRepository repository;

    public String getOrCreateUserId(String mobile) {

        return repository.findByMobile(mobile)
                .map(UserIdentity::getUserId)
                .orElseGet(() -> {
                    String userId = UUID.randomUUID().toString();

                    UserIdentity user = UserIdentity.builder()
                            .userId(userId)
                            .mobile(mobile)
                            .createdAt(Instant.now())
                            .build();

                    repository.save(user);
                    return userId;
                });
    }
}
