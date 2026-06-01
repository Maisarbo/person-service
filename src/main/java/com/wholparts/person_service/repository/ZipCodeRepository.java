package com.wholparts.person_service.repository;

import com.wholparts.person_service.model.ZipCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ZipCodeRepository
        extends JpaRepository<ZipCode, UUID> {

    Optional<ZipCode> findByZipCode(String zipCode);
}
