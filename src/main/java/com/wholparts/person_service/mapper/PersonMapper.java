package com.wholparts.person_service.mapper;

import com.wholparts.person_service.dto.PersonCreationDTO;
import com.wholparts.person_service.dto.PersonViewDTO;
import com.wholparts.person_service.mapper.AddressMapper;
import com.wholparts.person_service.model.Person;
import org.springframework.stereotype.Component;

@Component
public class PersonMapper {

    private final AddressMapper addressMapper;

    public PersonMapper(AddressMapper addressMapper) {
        this.addressMapper = addressMapper;
    }

    // =========================
    // DTO -> ENTITY
    // =========================

    public Person toEntity(PersonCreationDTO dto) {

        return Person.builder()
                .name(dto.getName())
                .tradeName(dto.getTradeName())
                .document(dto.getDocument())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .type(dto.getType())
                .build();
    }

    // =========================
    // ENTITY -> DTO
    // =========================

    public PersonViewDTO toViewDTO(Person person) {

        return PersonViewDTO.builder()
                .id(person.getId())
                .code(person.getCode())
                .name(person.getName())
                .tradeName(person.getTradeName())
                .document(person.getDocument())
                .email(person.getEmail())
                .phone(person.getPhone())
                .type(person.getType())
                .status(person.getStatus())
                .createdAt(person.getCreatedAt())
                .updatedAt(person.getUpdatedAt())
                .addresses(
                        person.getAddresses()
                                .stream()
                                .map(addressMapper::toView)
                                .toList()
                )
                .build();
    }
}