package com.danielerikssoncoder.spring_data_assignment.config;

import com.danielerikssoncoder.spring_data_assignment.entity.Address;
import com.danielerikssoncoder.spring_data_assignment.entity.Member;
import com.danielerikssoncoder.spring_data_assignment.repository.AddressRepository;
import com.danielerikssoncoder.spring_data_assignment.repository.MemberRepository;
import com.danielerikssoncoder.spring_data_assignment.service.CredentialService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final AddressRepository addressRepository;
    private final CredentialService credentialService;

    public DataSeeder(MemberRepository memberRepository, AddressRepository addressRepository, CredentialService credentialService) {
        this.memberRepository = memberRepository;
        this.addressRepository = addressRepository;
        this.credentialService = credentialService;
    }

    @Override
    public void run(String... args) {

        memberRepository.deleteAll();
        addressRepository.deleteAll();

        Address addr1 = new Address();
        addr1.setStreet("Storgatan 1");
        addr1.setPostalCode(11111);
        addr1.setCity("Stockholm");

        Address addr2 = new Address();
        addr2.setStreet("Lillgatan 2");
        addr2.setPostalCode(22222);
        addr2.setCity("Göteborg");

        Address addr3 = new Address();
        addr3.setStreet("Mellangatan 3");
        addr3.setPostalCode(33333);
        addr3.setCity("Malmö");

        addressRepository.saveAll(List.of(addr1, addr2, addr3));

        Member anna = new Member();
        anna.setFirstName("Anna");
        anna.setLastName("Andersson");
        anna.setEmail("anna@example.com");
        anna.setPhone("0701234567");
        anna.setDateOfBirth(LocalDate.of(1990, 1, 1));
        anna.setAddress(addr1);

        Member bengt = new Member();
        bengt.setFirstName("Bengt");
        bengt.setLastName("Berg");
        bengt.setEmail("bengt@example.com");
        bengt.setPhone("0702345678");
        bengt.setDateOfBirth(LocalDate.of(1985, 2, 2));
        bengt.setAddress(addr2);

        Member cecilia = new Member();
        cecilia.setFirstName("Cecilia");
        cecilia.setLastName("Carlsson");
        cecilia.setEmail("cecilia@example.com");
        cecilia.setPhone("0703456789");
        cecilia.setDateOfBirth(LocalDate.of(1992, 3, 3));
        cecilia.setAddress(addr3);

        Member david = new Member();
        david.setFirstName("David");
        david.setLastName("Dahl");
        david.setEmail("david@example.com");
        david.setPhone("0704567890");
        david.setDateOfBirth(LocalDate.of(1988, 4, 4));
        david.setAddress(addr1);

        Member elin = new Member();
        elin.setFirstName("Elin");
        elin.setLastName("Eriksson");
        elin.setEmail("elin@example.com");
        elin.setPhone(null);
        elin.setDateOfBirth(LocalDate.of(1995, 5, 5));
        elin.setAddress(addr2);

        List<Member> members = memberRepository.saveAll(List.of(anna, bengt, cecilia, david, elin));

        System.out.println("Credentials:");
        members.forEach(member -> {
            String password = credentialService.generateAndRegisterMember(member.getEmail());
            System.out.println(member.getEmail() + " | " + password);
        });

        System.out.println("Seeding klar: " + memberRepository.count() + " medlemmar, " + addressRepository.count() + " adresser.");
    }
}