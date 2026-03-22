package com.danielerikssoncoder.spring_data_assignment.dto;

import com.danielerikssoncoder.spring_data_assignment.entity.Address;

public record MemberDto(
        String firstName,
        String lastName,
        Address address,
        String email,
        String phone
) { }