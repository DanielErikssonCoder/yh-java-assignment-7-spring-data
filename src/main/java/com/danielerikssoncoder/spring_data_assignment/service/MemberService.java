package com.danielerikssoncoder.spring_data_assignment.service;

import com.danielerikssoncoder.spring_data_assignment.dto.MemberRequestDto;
import com.danielerikssoncoder.spring_data_assignment.entity.Address;
import com.danielerikssoncoder.spring_data_assignment.entity.Member;
import com.danielerikssoncoder.spring_data_assignment.exception.AddressNotFoundException;
import com.danielerikssoncoder.spring_data_assignment.exception.MemberAlreadyExistsException;
import com.danielerikssoncoder.spring_data_assignment.exception.MemberNotFoundException;
import com.danielerikssoncoder.spring_data_assignment.repository.AddressRepository;
import com.danielerikssoncoder.spring_data_assignment.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final AddressRepository addressRepository;
    private final CredentialService credentialService;

    public MemberService(MemberRepository memberRepository, AddressRepository addressRepository, CredentialService credentialService) {
        this.memberRepository = memberRepository;
        this.addressRepository = addressRepository;
        this.credentialService = credentialService;
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    public Member getMemberById(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new MemberNotFoundException("Medlem med id " + id + " kunde inte hittas"));
    }

    public Member createMember(Member member) {
        if (memberRepository.findByEmail(member.getEmail()).isPresent()) {
            throw new MemberAlreadyExistsException("Medlem med email " + member.getEmail() + " finns redan");
        }

        if (member.getAddress() != null && member.getAddress().getId() != null) {
            Address address = addressRepository.findById(member.getAddress().getId())
                    .orElseThrow(() -> new AddressNotFoundException("Adress med id " + member.getAddress().getId() + " kunde inte hittas"));
            member.setAddress(address);
        }

        Member saved = memberRepository.save(member);

        String password = credentialService.generateAndRegisterMember(saved.getEmail());
        System.out.println("Ny medlem: " + saved.getEmail() + " | " + password);

        return saved;
    }

    public Member createMemberFromDto(MemberRequestDto dto) {
        if (memberRepository.findByEmail(dto.email()).isPresent()) {
            throw new MemberAlreadyExistsException("Medlem med email " + dto.email() + " finns redan");
        }

        Member member = new Member();
        member.setFirstName(dto.firstName());
        member.setLastName(dto.lastName());
        member.setEmail(dto.email());
        member.setPhone(dto.phone());
        member.setDateOfBirth(dto.dateOfBirth());

        if (dto.address() != null && dto.address().getId() != null) {
            Address address = addressRepository.findById(dto.address().getId())
                    .orElseThrow(() -> new AddressNotFoundException("Adress med id " + dto.address().getId() + " kunde inte hittas"));
            member.setAddress(address);
        }

        Member saved = memberRepository.save(member);
        String password = credentialService.generateAndRegisterMember(saved.getEmail());
        System.out.println("Ny medlem: " + saved.getEmail() + " | " + password);

        return saved;
    }

    public Member updateMember(Long id, Member member) {
        Member existing = getMemberById(id);
        existing.setFirstName(member.getFirstName());
        existing.setLastName(member.getLastName());
        existing.setEmail(member.getEmail());
        existing.setPhone(member.getPhone());
        existing.setDateOfBirth(member.getDateOfBirth());

        if (member.getAddress() != null && member.getAddress().getId() != null) {
            Address address = addressRepository.findById(member.getAddress().getId())
                    .orElseThrow(() -> new AddressNotFoundException("Adress med id " + member.getAddress().getId() + " kunde inte hittas"));
            existing.setAddress(address);
        }

        return memberRepository.save(existing);
    }

    public Member updateMemberFromDto(Long id, MemberRequestDto dto) {
        Member existing = getMemberById(id);
        existing.setFirstName(dto.firstName());
        existing.setLastName(dto.lastName());
        existing.setEmail(dto.email());
        existing.setPhone(dto.phone());
        existing.setDateOfBirth(dto.dateOfBirth());

        if (dto.address() != null && dto.address().getId() != null) {
            Address address = addressRepository.findById(dto.address().getId())
                    .orElseThrow(() -> new AddressNotFoundException("Adress med id " + dto.address().getId() + " kunde inte hittas"));
            existing.setAddress(address);
        }

        return memberRepository.save(existing);
    }

    public Member patchMember(Long id, Map<String, Object> updates) {
        Member existing = getMemberById(id);

        updates.forEach((key, value) -> {
            switch (key) {
                case "firstName" -> existing.setFirstName((String) value);
                case "lastName" -> existing.setLastName((String) value);
                case "email" -> existing.setEmail((String) value);
                case "phone" -> existing.setPhone((String) value);
                case "dateOfBirth" -> existing.setDateOfBirth(LocalDate.parse((String) value));
                case "address" -> {
                    Long addressId = Long.parseLong(value.toString());
                    Address address = addressRepository.findById(addressId)
                            .orElseThrow(() -> new AddressNotFoundException("Adressen kunde inte hittas"));
                    existing.setAddress(address);
                }
            }
        });
        return memberRepository.save(existing);
    }

    public void deleteMemberById(Long id) {
        Member existing = getMemberById(id);
        memberRepository.delete(existing);
    }

    public Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException("Medlem med email " + email + " kunde inte hittas"));
    }
}