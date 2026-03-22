package com.danielerikssoncoder.spring_data_assignment.dto;

import com.danielerikssoncoder.spring_data_assignment.entity.Address;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotBlank;

public record MemberUpdateDto(
        @NotBlank(message = "Förnamn får inte vara tomt")
        String firstName,

        @NotBlank(message = "Efternamn får inte vara tomt")
        String lastName,

        @Pattern(regexp = "\\+?[0-9]{7,15}", message = "Telefonnummer måste innehålla 7-15 siffror, ev. med + prefix")
        String phone,

        Address address
) { }