package com.cowin.auth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "user_identity")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserIdentity {

    @Id
    private String userId; // UUID

    @Column(unique = true, nullable = false)
    private String mobile;

    private Instant createdAt;
}



