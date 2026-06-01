package com.wholparts.person_service;


import com.wholparts.person_service.model.City;
import com.wholparts.person_service.model.District;
import com.wholparts.person_service.repository.CityRepository;
import com.wholparts.person_service.repository.DistrictRepository;
import com.wholparts.person_service.service.LocationResolverService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocationResolverServiceTest {

    @Mock
    private CityRepository cityRepository;

    @Mock
    private DistrictRepository districtRepository;

    @InjectMocks
    private LocationResolverService locationResolverService;

    @Test
    void shouldResolveCitySuccessfully() {

        System.out.println("\n========================================");
        System.out.println("TESTE RESOLVE CITY");
        System.out.println("========================================");

        City city = City.builder()
                .id(null)
                .name("Divinópolis")
                .ibgeCode(3122306)
                .build();

        when(cityRepository.findByIbgeCode(3122306))
                .thenReturn(Optional.of(city));

        City result =
                locationResolverService.resolveCity(3122306);

        assertNotNull(result);

        assertEquals(
                "Divinópolis",
                result.getName()
        );

        System.out.println("Cidade encontrada");
        System.out.println("Nome: " + result.getName());
        System.out.println("IBGE: " + result.getIbgeCode());

        verify(cityRepository)
                .findByIbgeCode(3122306);

        System.out.println("TESTE FINALIZADO COM SUCESSO");
    }

    @Test
    void shouldThrowExceptionWhenCityNotFound() {

        System.out.println("\n========================================");
        System.out.println("TESTE CITY NOT FOUND");
        System.out.println("========================================");

        when(cityRepository.findByIbgeCode(9999999))
                .thenReturn(Optional.empty());

        IllegalStateException exception =
                assertThrows(
                        IllegalStateException.class,
                        () -> locationResolverService.resolveCity(9999999)
                );

        System.out.println(
                "Mensagem retornada: "
                        + exception.getMessage()
        );

        assertTrue(
                exception.getMessage()
                        .contains("Cidade não encontrada")
        );

        System.out.println("EXCEPTION VALIDADA COM SUCESSO");
    }

    @Test
    void shouldReturnExistingDistrict() {

        System.out.println("\n========================================");
        System.out.println("TESTE DISTRICT EXISTENTE");
        System.out.println("========================================");

        City city = City.builder()
                .name("Divinópolis")
                .ibgeCode(3122306)
                .build();

        District district = new District();

        district.setName("Centro");
        district.setCode("CTR");
        district.setCity(city);

        when(
                districtRepository.findByNameAndCity(
                        "Centro",
                        city
                )
        ).thenReturn(Optional.of(district));

        District result =
                locationResolverService.resolveDistrict(
                        "Centro",
                        "CTR",
                        city
                );

        assertNotNull(result);

        assertEquals(
                "Centro",
                result.getName()
        );

        System.out.println("Bairro encontrado");
        System.out.println("Nome: " + result.getName());
        System.out.println("Código: " + result.getCode());

        verify(districtRepository, never())
                .save(any());

        System.out.println("NENHUM NOVO BAIRRO FOI CRIADO");
    }

    @Test
    void shouldCreateDistrictWhenNotExists() {

        System.out.println("\n========================================");
        System.out.println("TESTE CRIAÇÃO DE BAIRRO");
        System.out.println("========================================");

        City city = City.builder()
                .name("Divinópolis")
                .ibgeCode(3122306)
                .build();

        when(
                districtRepository.findByNameAndCity(
                        "Novo Bairro",
                        city
                )
        ).thenReturn(Optional.empty());

        when(
                districtRepository.save(any(District.class))
        ).thenAnswer(invocation -> invocation.getArgument(0));

        District result =
                locationResolverService.resolveDistrict(
                        "Novo Bairro",
                        "AUTO",
                        city
                );

        assertNotNull(result);

        assertEquals(
                "Novo Bairro",
                result.getName()
        );

        assertEquals(
                "AUTO",
                result.getCode()
        );

        ArgumentCaptor<District> captor =
                ArgumentCaptor.forClass(District.class);

        verify(districtRepository)
                .save(captor.capture());

        District saved =
                captor.getValue();

        System.out.println("Novo bairro criado");
        System.out.println("Nome: " + saved.getName());
        System.out.println("Código: " + saved.getCode());
        System.out.println("Cidade: " + saved.getCity().getName());

        assertEquals(
                "Novo Bairro",
                saved.getName()
        );

        assertEquals(
                "AUTO",
                saved.getCode()
        );

        System.out.println("BAIRRO CRIADO COM SUCESSO");
    }
}