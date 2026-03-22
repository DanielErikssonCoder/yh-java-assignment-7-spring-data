package com.danielerikssoncoder.spring_data_assignment.dto;

import com.danielerikssoncoder.spring_data_assignment.entity.Address;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record MemberRequestDto(
        @NotBlank(message = "Förnamn får inte vara tomt")
        String firstName,

        @NotBlank(message = "Efternamn får inte vara tomt")
        String lastName,

        @NotBlank(message = "Email får inte vara tom")
        @Email(message = "Email måste vara giltig")
        String email,

        String phone,

        LocalDate dateOfBirth,

        Address address
) {}