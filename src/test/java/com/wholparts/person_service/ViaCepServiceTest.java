package com.wholparts.person_service;


import com.wholparts.person_service.dto.ViaCepResponseDTO;
import com.wholparts.person_service.model.City;
import com.wholparts.person_service.model.ZipCode;
import com.wholparts.person_service.repository.ZipCodeRepository;
import com.wholparts.person_service.service.LocationResolverService;
import com.wholparts.person_service.service.ViaCepService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ViaCepServiceTest {

    @Mock
    private RestClient restClient;

    @Mock
    private ZipCodeRepository zipCodeRepository;

    @Mock
    private LocationResolverService locationResolverService;

    @InjectMocks
    private ViaCepService viaCepService;

    @Test
    void shouldReturnCepFromCache() {

        System.out.println("\n================================");
        System.out.println("TESTE CEP NO CACHE");
        System.out.println("================================");

        City city =
                City.builder()
                        .name("Divinópolis")
                        .ibgeCode(3122306)
                        .build();

        ZipCode zip =
                ZipCode.builder()
                        .zipCode("35500008")
                        .street("Rua Pernambuco")
                        .district("Centro")
                        .city(city)
                        .createdAt(LocalDateTime.now())
                        .build();

        when(zipCodeRepository.findByZipCode("35500008"))
                .thenReturn(Optional.of(zip));

        ViaCepResponseDTO result =
                viaCepService.findByCep("35500-008");

        assertNotNull(result);

        assertEquals(
                "Rua Pernambuco",
                result.getLogradouro()
        );

        assertEquals(
                "Centro",
                result.getBairro()
        );

        assertEquals(
                "Divinópolis",
                result.getLocalidade()
        );

        System.out.println("CEP........: " + result.getCep());
        System.out.println("Rua........: " + result.getLogradouro());
        System.out.println("Bairro.....: " + result.getBairro());
        System.out.println("Cidade.....: " + result.getLocalidade());

        verify(zipCodeRepository)
                .findByZipCode("35500008");

        verifyNoMoreInteractions(locationResolverService);

        System.out.println("CACHE FUNCIONOU COM SUCESSO");
    }

    @Test
    void shouldThrowExceptionWhenCepInvalid() {

        System.out.println("\n================================");
        System.out.println("TESTE CEP INVÁLIDO");
        System.out.println("================================");

        when(zipCodeRepository.findByZipCode("00000000"))
                .thenReturn(Optional.empty());

        RestClient.RequestHeadersUriSpec request =
                mock(RestClient.RequestHeadersUriSpec.class);

        RestClient.RequestHeadersSpec requestSpec =
                mock(RestClient.RequestHeadersSpec.class);

        RestClient.ResponseSpec responseSpec =
                mock(RestClient.ResponseSpec.class);

        ViaCepResponseDTO response =
                new ViaCepResponseDTO();

        response.setErro(true);

        when(restClient.get())
                .thenReturn(request);

        when(request.uri("/{cep}/json", "00000000"))
                .thenReturn(requestSpec);

        when(requestSpec.retrieve())
                .thenReturn(responseSpec);

        when(responseSpec.body(ViaCepResponseDTO.class))
                .thenReturn(response);

        IllegalStateException exception =
                assertThrows(
                        IllegalStateException.class,
                        () -> viaCepService.findByCep("00000-000")
                );

        System.out.println(
                "Mensagem: "
                        + exception.getMessage()
        );

        assertEquals(
                "CEP inválido",
                exception.getMessage()
        );
    }

    @Test
    void shouldConsultViaCepAndSaveCache() {

        System.out.println("\n================================");
        System.out.println("TESTE CONSULTA VIACEP");
        System.out.println("================================");

        when(zipCodeRepository.findByZipCode("35500008"))
                .thenReturn(Optional.empty());

        RestClient.RequestHeadersUriSpec request =
                mock(RestClient.RequestHeadersUriSpec.class);

        RestClient.RequestHeadersSpec requestSpec =
                mock(RestClient.RequestHeadersSpec.class);

        RestClient.ResponseSpec responseSpec =
                mock(RestClient.ResponseSpec.class);

        ViaCepResponseDTO response =
                new ViaCepResponseDTO();

        response.setCep("35500-008");
        response.setLogradouro("Rua Pernambuco");
        response.setBairro("Centro");
        response.setLocalidade("Divinópolis");
        response.setIbge("3122306");

        City city =
                City.builder()
                        .name("Divinópolis")
                        .ibgeCode(3122306)
                        .build();

        when(restClient.get())
                .thenReturn(request);

        when(request.uri("/{cep}/json", "35500008"))
                .thenReturn(requestSpec);

        when(requestSpec.retrieve())
                .thenReturn(responseSpec);

        when(responseSpec.body(ViaCepResponseDTO.class))
                .thenReturn(response);

        when(locationResolverService.resolveCity(3122306))
                .thenReturn(city);

        ViaCepResponseDTO result =
                viaCepService.findByCep("35500-008");

        assertNotNull(result);

        verify(zipCodeRepository)
                .save(any(ZipCode.class));

        System.out.println("CEP........: " + result.getCep());
        System.out.println("Rua........: " + result.getLogradouro());
        System.out.println("Bairro.....: " + result.getBairro());
        System.out.println("Cidade.....: " + result.getLocalidade());

        System.out.println("CACHE SALVO COM SUCESSO");
    }
}