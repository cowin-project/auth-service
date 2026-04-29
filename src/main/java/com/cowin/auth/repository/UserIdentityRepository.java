package com.cowin.auth.repository;

import com.cowin.auth.entity.UserIdentity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserIdentityRepository
        extends JpaRepository<UserIdentity, String> {

    Optional<UserIdentity> findByMobile(String mobile);
}