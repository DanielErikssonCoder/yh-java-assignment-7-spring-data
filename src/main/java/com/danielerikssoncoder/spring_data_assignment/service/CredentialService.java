package com.danielerikssoncoder.spring_data_assignment.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CredentialService {

    private final InMemoryUserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;

    public CredentialService(InMemoryUserDetailsManager userDetailsManager, PasswordEncoder passwordEncoder) {
        this.userDetailsManager = userDetailsManager;
        this.passwordEncoder = passwordEncoder;
    }

    public String generateAndRegisterMember(String email) {
        String password = UUID.randomUUID().toString().substring(0, 8);

        UserDetails user = User.builder()
                .username(email)
                .password(passwordEncoder.encode(password))
                .roles("MEMBER")
                .build();

        userDetailsManager.createUser(user);

        return password;
    }

}