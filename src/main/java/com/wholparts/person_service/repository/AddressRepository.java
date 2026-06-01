package com.wholparts.person_service.repository;

import com.wholparts.person_service.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AddressRepository
        extends JpaRepository<Address, UUID> {
}