package com.wholparts.person_service.service;

import com.wholparts.person_service.dto.*;
import com.wholparts.person_service.enums.PersonStatus;
import com.wholparts.person_service.enums.PersonType;
import com.wholparts.person_service.mapper.PersonMapper;
import com.wholparts.person_service.model.Person;
import com.wholparts.person_service.repository.PersonRepository;
import com.wholparts.person_service.specification.PersonSpecification;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;

    private final PersonMapper personMapper;

    private final AddressService addressService;

    private final PersonCodeService personCodeService;



    // =========================================================
    // CREATE
    // =========================================================

    @Transactional
    public PersonViewDTO create(PersonCreationDTO dto) {

        validateName(dto.getName());

        validateDocumentFormat(dto.getDocument());

        validateDocument(dto.getDocument());

        validateEmail(dto.getEmail());

        validateEmailUnique(dto.getEmail());

        Person person =
                personMapper.toEntity(dto);

        person.setCode(
                personCodeService.generate(dto.getType())
        );

        person.setStatus(PersonStatus.ACTIVE);

        Person savedPerson =
                personRepository.save(person);

        // =====================================================
        // ADDRESSES
        // =====================================================

        if (
                dto.getAddresses() != null
                        && !dto.getAddresses().isEmpty()
        ) {

            for (AddressCreationDTO addressDTO
                    : dto.getAddresses()) {

                addressService.addAddress(
                        savedPerson.getId(),
                        addressDTO
                );
            }
        }

        // RECARREGA PERSON
        Person updatedPerson =
                personRepository.findById(
                        savedPerson.getId()
                ).orElseThrow();

        return personMapper.toViewDTO(
                updatedPerson
        );
    }

    // =========================================================
    // UPDATE
    // =========================================================

    @CacheEvict(value = "persons", key = "#id")
    @Transactional
    public PersonViewDTO update(
            UUID id,
            PersonUpdateDTO dto
    ) {

        Person person =
                personRepository.findById(id)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Pessoa não encontrada: " + id
                                )
                        );

        validateName(dto.getName());

        validateDocumentFormat(dto.getDocument());

        validateDocumentUpdate(
                id,
                dto.getDocument()
        );

        validateEmail(dto.getEmail());

        validateEmailUpdate(
                id,
                dto.getEmail()
        );

        person.setName(dto.getName());

        person.setTradeName(dto.getTradeName());

        person.setDocument(dto.getDocument());

        person.setEmail(dto.getEmail());

        person.setPhone(dto.getPhone());

        person.setType(dto.getType());

        Person updatedPerson =
                personRepository.save(person);

        return personMapper.toViewDTO(updatedPerson);
    }

    // =========================================================
    // FIND BY ID
    // =========================================================

    @Cacheable(value = "persons", key = "#id")
    public PersonViewDTO findById(UUID id) {

        Person person =
                personRepository.findById(id)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Pessoa não encontrada: " + id
                                )
                        );

        return personMapper.toViewDTO(person);
    }

    // =========================================================
    // FIND BY DOCUMENT
    // =========================================================

    public PersonViewDTO findByDocument(String document) {

        Person person =
                personRepository.findByDocument(document)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Pessoa não encontrada"
                                )
                        );

        return personMapper.toViewDTO(person);
    }

    // =========================================================
    // PAGINATION
    // =========================================================

    public Page<PersonViewDTO> findAllPaged(
            int page,
            int size
    ) {

        Pageable pageable =
                PageRequest.of(page, size);

        return personRepository.findAll(pageable)
                .map(personMapper::toViewDTO);
    }

    // =========================================================
    // SPECIFICATION SEARCH
    // =========================================================

    public Page<PersonViewDTO> search(
            String name,
            PersonStatus status,
            PersonType type,
            int page,
            int size
    ) {

        Pageable pageable =
                PageRequest.of(page, size);

        Specification<Person> spec =
                Specification.allOf();

        if (
                name != null
                        && !name.isBlank()
        ) {

            spec = spec.and(
                    PersonSpecification.nameContains(name)
            );
        }

        if (status != null) {

            spec = spec.and(
                    PersonSpecification.hasStatus(status)
            );
        }

        if (type != null) {

            spec = spec.and(
                    PersonSpecification.hasType(type)
            );
        }

        return personRepository
                .findAll(spec, pageable)
                .map(personMapper::toViewDTO);
    }

    // =========================================================
    // ACTIVATE
    // =========================================================

    @CacheEvict(value = "persons", key = "#id")
    @Transactional
    public void activate(UUID id) {

        Person person =
                personRepository.findById(id)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Pessoa não encontrada: " + id
                                )
                        );

        person.setStatus(PersonStatus.ACTIVE);

        personRepository.save(person);
    }

    // =========================================================
    // DEACTIVATE
    // =========================================================

    @CacheEvict(value = "persons", key = "#id")
    @Transactional
    public void deactivate(UUID id) {

        Person person =
                personRepository.findById(id)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Pessoa não encontrada: " + id
                                )
                        );

        person.setStatus(PersonStatus.INACTIVE);

        personRepository.save(person);
    }

    // =========================================================
    // VALIDATIONS
    // =========================================================

    private void validateName(String name) {

        if (
                name == null
                        || name.isBlank()
        ) {

            throw new IllegalArgumentException(
                    "Nome é obrigatório"
            );
        }
    }

    private void validateDocument(String document) {

        if (
                personRepository.existsByDocument(document)
        ) {

            throw new IllegalArgumentException(
                    "Documento já cadastrado"
            );
        }
    }

    private void validateDocumentUpdate(
            UUID id,
            String document
    ) {

        Optional<Person> existingPerson =
                personRepository.findByDocument(document);

        if (
                existingPerson.isPresent()
                        && !existingPerson.get()
                        .getId()
                        .equals(id)
        ) {

            throw new IllegalArgumentException(
                    "Documento já cadastrado"
            );
        }
    }

    private void validateDocumentFormat(
            String document
    ) {

        String cleanDocument =
                document.replaceAll("\\D", "");

        if (
                cleanDocument.length() != 11
                        && cleanDocument.length() != 14
        ) {

            throw new IllegalArgumentException(
                    "CPF ou CNPJ inválido"
            );
        }
    }

    private void validateEmail(String email) {

        if (
                email != null
                        && !email.isBlank()
                        && !email.matches(
                        "^[A-Za-z0-9+_.-]+@(.+)$"
                )
        ) {

            throw new IllegalArgumentException(
                    "Email inválido"
            );
        }
    }

    private void validateEmailUnique(String email) {

        if (
                email != null
                        && !email.isBlank()
                        && personRepository.existsByEmail(email)
        ) {

            throw new IllegalArgumentException(
                    "Email já cadastrado"
            );
        }
    }

    private void validateEmailUpdate(
            UUID id,
            String email
    ) {

        if (
                email == null
                        || email.isBlank()
        ) {

            return;
        }

        Optional<Person> existingPerson =
                personRepository.findByEmail(email);

        if (
                existingPerson.isPresent()
                        && !existingPerson.get()
                        .getId()
                        .equals(id)
        ) {

            throw new IllegalArgumentException(
                    "Email já cadastrado"
            );
        }
    }
}