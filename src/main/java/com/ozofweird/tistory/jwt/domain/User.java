package com.ozofweird.tistory.jwt.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false, unique = true)
    private String email;

    @Column(length = 300, nullable = false)
    private String password;

    @Column
    private String username;

    @Column
    private int active; // 1: active, 0 : inactive

    @Column
    private Role role;

    @Builder
    public User(String email, String password, String username, int active, Role role) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.active = active;
        this.role = role;
    }

}
