package com.wholparts.person_service.mapper;

import com.wholparts.person_service.dto.AddressCreationDTO;
import com.wholparts.person_service.dto.AddressUpdateDTO;
import com.wholparts.person_service.dto.AddressViewDTO;
import com.wholparts.person_service.model.Address;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    // =====================================================
    // CREATE DTO -> ENTITY
    // =====================================================

    public Address toEntity(AddressCreationDTO dto) {

        return Address.builder()
                .street(dto.getStreet())
                .number(dto.getNumber())
                .complement(dto.getComplement())
                .zipCode(dto.getZipCode())
                .build();
    }

    // =====================================================
    // UPDATE DTO -> ENTITY
    // =====================================================

    public void updateEntity(
            Address address,
            AddressUpdateDTO dto
    ) {

        address.setStreet(dto.getStreet());

        address.setNumber(dto.getNumber());

        address.setComplement(dto.getComplement());

        address.setZipCode(dto.getZipCode());
    }

    // =====================================================
    // ENTITY -> VIEW DTO
    // =====================================================

    public AddressViewDTO toView(
            Address address
    ) {

        return AddressViewDTO.builder()
                .id(address.getId())

                .street(address.getStreet())

                .number(address.getNumber())

                .complement(address.getComplement())

                .zipCode(address.getZipCode())

                .district(
                        address.getDistrict().getName()
                )

                .city(
                        address.getDistrict()
                                .getCity()
                                .getName()
                )

                .state(
                        address.getDistrict()
                                .getCity()
                                .getState()
                                .getName()
                )

                .build();
    }
}