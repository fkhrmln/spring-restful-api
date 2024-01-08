package com.example.app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    private String username;

    private String password;

    private String name;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "expired_at")
    @Lob
    private Long expiredAt;

    @OneToMany(mappedBy = "user")
    private List<Contact> contacts;

}
