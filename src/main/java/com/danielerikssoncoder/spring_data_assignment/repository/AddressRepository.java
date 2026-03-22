package com.danielerikssoncoder.spring_data_assignment.repository;

import com.danielerikssoncoder.spring_data_assignment.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
}