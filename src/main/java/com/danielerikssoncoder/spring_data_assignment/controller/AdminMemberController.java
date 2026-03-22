package com.danielerikssoncoder.spring_data_assignment.controller;

import com.danielerikssoncoder.spring_data_assignment.dto.MemberRequestDto;
import com.danielerikssoncoder.spring_data_assignment.entity.Member;
import com.danielerikssoncoder.spring_data_assignment.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminMemberController {

    private final MemberService memberService;

    public AdminMemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/members")
    public List<Member> getAllMembers() {
        return memberService.getAllMembers();
    }

    @GetMapping("/members/{id}")
    public Member getMemberById(@PathVariable Long id) {
        return memberService.getMemberById(id);
    }

    @PutMapping("/members/{id}")
    public Member updateMember(@PathVariable Long id, @RequestBody @Valid MemberRequestDto dto) {
        return memberService.updateMemberFromDto(id, dto);
    }

    @PatchMapping("/members/{id}")
    public Member patchMember(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        return memberService.patchMember(id, updates);
    }

    @PostMapping("/members")
    @ResponseStatus(HttpStatus.CREATED)
    public Member createMember(@RequestBody @Valid MemberRequestDto dto) {
        return memberService.createMemberFromDto(dto);
    }

    @DeleteMapping("/members/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMember(@PathVariable Long id) {
        memberService.deleteMemberById(id);
    }


}
