package com.wholparts.person_service.service;

import com.wholparts.person_service.dto.*;
import com.wholparts.person_service.mapper.AddressMapper;
import com.wholparts.person_service.model.*;
import com.wholparts.person_service.repository.AddressRepository;
import com.wholparts.person_service.repository.PersonRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;

    private final PersonRepository personRepository;

    private final AddressMapper addressMapper;

    private final LocationResolverService locationResolverService;

    @Transactional
    public AddressViewDTO addAddress(
            UUID personId,
            AddressCreationDTO dto
    ) {

        Person person =
                personRepository.findById(personId)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Pessoa não encontrada"
                                )
                        );

        City city =
                locationResolverService.resolveCity(
                        dto.getCityIbgeCode()
                );

        District district =
                locationResolverService.resolveDistrict(
                        dto.getDistrictName(),
                        "AUTO",
                        city
                );

        Address address =
                addressMapper.toEntity(dto);

        address.setDistrict(district);

        address.setPerson(person);

        person.getAddresses().add(address);

        Address saved =
                addressRepository.save(address);

        return addressMapper.toView(saved);
    }

    @Transactional
    public AddressViewDTO update(
            UUID addressId,
            AddressUpdateDTO dto
    ) {

        Address address =
                addressRepository.findById(addressId)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Endereço não encontrado"
                                )
                        );

        City city =
                locationResolverService.resolveCity(
                        dto.getCityIbgeCode()
                );

        District district =
                locationResolverService.resolveDistrict(
                        dto.getDistrictName(),
                        "AUTO",
                        city
                );

        address.setStreet(dto.getStreet());

        address.setNumber(dto.getNumber());

        address.setComplement(dto.getComplement());

        address.setZipCode(dto.getZipCode());

        address.setDistrict(district);

        Address updated =
                addressRepository.save(address);

        return addressMapper.toView(updated);
    }

    @Transactional
    public void delete(UUID id) {

        Address address =
                addressRepository.findById(id)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Endereço não encontrado"
                                )
                        );

        addressRepository.delete(address);
    }

    public AddressViewDTO findById(UUID id) {

        Address address =
                addressRepository.findById(id)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Endereço não encontrado"
                                )
                        );

        return addressMapper.toView(address);
    }
}
