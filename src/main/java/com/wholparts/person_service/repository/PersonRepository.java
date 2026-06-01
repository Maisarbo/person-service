package com.wholparts.person_service.repository;

import com.wholparts.person_service.enums.PersonStatus;
import com.wholparts.person_service.enums.PersonType;
import com.wholparts.person_service.model.Person;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PersonRepository extends
        JpaRepository<Person, UUID>,
        JpaSpecificationExecutor<Person> {


    boolean existsByDocument(String document);

    boolean existsByEmail(String email);

    Long countByType(PersonType type);

    Optional<Person> findByEmail(String email);

    Optional<Person> findByDocument(String document);

    List<Person> findByNameContainingIgnoreCase(String name);

    List<Person> findByStatus(PersonStatus status);

    List<Person> findByType(PersonType type);

    List<Person> findByNameContainingIgnoreCaseAndStatus(
            String name,
            PersonStatus status
    );

}