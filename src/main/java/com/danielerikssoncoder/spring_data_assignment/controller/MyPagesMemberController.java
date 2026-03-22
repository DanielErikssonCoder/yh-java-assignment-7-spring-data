package com.danielerikssoncoder.spring_data_assignment.controller;

import com.danielerikssoncoder.spring_data_assignment.dto.MemberDto;
import com.danielerikssoncoder.spring_data_assignment.dto.MemberUpdateDto;
import com.danielerikssoncoder.spring_data_assignment.entity.Member;
import com.danielerikssoncoder.spring_data_assignment.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;

@RestController
@RequestMapping("/mypages")
public class MyPagesMemberController {

    private final MemberService memberService;

    public MyPagesMemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/members")
    public List<MemberDto> getAllMembers() {

        return memberService.getAllMembers().stream()
                .map(member -> new MemberDto(
                        member.getFirstName(),
                        member.getLastName(),
                        member.getAddress(),
                        member.getEmail(),
                        member.getPhone()))
                .toList();
    }

    @PutMapping("/members/{id}")
    public Member updateOwnMember(@PathVariable Long id, @RequestBody @Valid MemberUpdateDto memberDto) {
        Member existing = memberService.getMemberById(id);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        if (!existing.getEmail().equals(email)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Du kan endast uppdatera din egen data");
        }

        existing.setFirstName(memberDto.firstName());
        existing.setLastName(memberDto.lastName());
        existing.setPhone(memberDto.phone());
        existing.setAddress(memberDto.address());

        return memberService.updateMember(existing.getId(), existing);
    }
}
