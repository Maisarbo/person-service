package com.wholparts.person_service;

import com.wholparts.person_service.dto.AddressCreationDTO;
import com.wholparts.person_service.dto.AddressViewDTO;
import com.wholparts.person_service.mapper.AddressMapper;
import com.wholparts.person_service.model.Address;
import com.wholparts.person_service.model.City;
import com.wholparts.person_service.model.District;
import com.wholparts.person_service.model.Person;
import com.wholparts.person_service.repository.AddressRepository;
import com.wholparts.person_service.repository.PersonRepository;
import com.wholparts.person_service.service.AddressService;
import com.wholparts.person_service.service.LocationResolverService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private AddressMapper addressMapper;

    @Mock
    private LocationResolverService locationResolverService;

    @InjectMocks
    private AddressService addressService;

    @Test
    void shouldAddAddressSuccessfully() {

        System.out.println("====================================");
        System.out.println("TESTE ADD ADDRESS");
        System.out.println("====================================");

        UUID personId = UUID.randomUUID();

        AddressCreationDTO dto =
                AddressCreationDTO.builder()
                        .street("Rua Pernambuco")
                        .number("500")
                        .complement("Galpão")
                        .zipCode("35500-008")
                        .districtName("Centro")
                        .cityIbgeCode(3122306)
                        .build();

        Person person =
                Person.builder()
                        .id(personId)
                        .addresses(new ArrayList<>())
                        .build();

        City city =
                City.builder()
                        .name("Divinópolis")
                        .ibgeCode(3122306)
                        .build();

        District district =
                District.builder()
                        .name("Centro")
                        .city(city)
                        .build();

        Address address =
                Address.builder()
                        .street(dto.getStreet())
                        .number(dto.getNumber())
                        .complement(dto.getComplement())
                        .zipCode(dto.getZipCode())
                        .build();

        AddressViewDTO expected =
                AddressViewDTO.builder()
                        .street("Rua Pernambuco")
                        .number("500")
                        .district("Centro")
                        .city("Divinópolis")
                        .build();

        when(personRepository.findById(personId))
                .thenReturn(Optional.of(person));

        when(locationResolverService.resolveCity(3122306))
                .thenReturn(city);

        when(locationResolverService.resolveDistrict(
                "Centro",
                "AUTO",
                city
        )).thenReturn(district);

        when(addressMapper.toEntity(dto))
                .thenReturn(address);

        when(addressRepository.save(any(Address.class)))
                .thenReturn(address);

        when(addressMapper.toView(address))
                .thenReturn(expected);

        AddressViewDTO result =
                addressService.addAddress(personId, dto);

        ArgumentCaptor<Address> captor =
                ArgumentCaptor.forClass(Address.class);

        verify(addressRepository)
                .save(captor.capture());

        Address savedAddress =
                captor.getValue();

        System.out.println();
        System.out.println("ENDEREÇO SALVO");
        System.out.println("Rua: " + savedAddress.getStreet());
        System.out.println("Número: " + savedAddress.getNumber());
        System.out.println("CEP: " + savedAddress.getZipCode());
        System.out.println("Bairro: " + savedAddress.getDistrict().getName());
        System.out.println();

        assertNotNull(result);

        verify(addressRepository, times(1))
                .save(any(Address.class));

        System.out.println("TESTE FINALIZADO COM SUCESSO");
        System.out.println("====================================");
    }

    @Test
    void shouldThrowExceptionWhenPersonNotFound() {

        System.out.println("====================================");
        System.out.println("TESTE PERSON NOT FOUND");
        System.out.println("====================================");

        UUID personId = UUID.randomUUID();

        AddressCreationDTO dto =
                AddressCreationDTO.builder()
                        .street("Rua A")
                        .number("10")
                        .zipCode("35500-000")
                        .districtName("Centro")
                        .cityIbgeCode(3122306)
                        .build();

        when(personRepository.findById(personId))
                .thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> addressService.addAddress(personId, dto)
        );

        System.out.println("EXCEPTION CAPTURADA COM SUCESSO");
        System.out.println("====================================");
    }

    @Test
    void shouldDeleteAddressSuccessfully() {

        System.out.println("====================================");
        System.out.println("TESTE DELETE ADDRESS");
        System.out.println("====================================");

        UUID addressId = UUID.randomUUID();

        Address address =
                Address.builder()
                        .id(addressId)
                        .build();

        when(addressRepository.findById(addressId))
                .thenReturn(Optional.of(address));

        addressService.delete(addressId);

        verify(addressRepository)
                .delete(address);

        System.out.println("Endereço removido com sucesso");
        System.out.println("====================================");
    }

    @Test
    void shouldFindAddressByIdSuccessfully() {

        System.out.println("====================================");
        System.out.println("TESTE FIND ADDRESS");
        System.out.println("====================================");

        UUID addressId = UUID.randomUUID();

        Address address =
                Address.builder()
                        .id(addressId)
                        .street("Rua Pernambuco")
                        .build();

        AddressViewDTO view =
                AddressViewDTO.builder()
                        .id(addressId)
                        .street("Rua Pernambuco")
                        .build();

        when(addressRepository.findById(addressId))
                .thenReturn(Optional.of(address));

        when(addressMapper.toView(address))
                .thenReturn(view);

        AddressViewDTO result =
                addressService.findById(addressId);

        assertNotNull(result);

        assertEquals(
                "Rua Pernambuco",
                result.getStreet()
        );

        System.out.println("ID: " + result.getId());
        System.out.println("Rua: " + result.getStreet());
        System.out.println("====================================");
    }
}
