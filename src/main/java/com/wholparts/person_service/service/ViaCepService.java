package com.wholparts.person_service.service;

import com.wholparts.person_service.dto.ViaCepResponseDTO;
import com.wholparts.person_service.model.City;
import com.wholparts.person_service.model.ZipCode;
import com.wholparts.person_service.repository.ZipCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ViaCepService {

    private final RestClient restClient;

    private final ZipCodeRepository zipCodeRepository;

    private final LocationResolverService locationResolverService;

    public ViaCepResponseDTO findByCep(String cep) {

        cep = cep.replaceAll("\\D", "");

        // =========================================
        // CACHE LOCAL
        // =========================================

        Optional<ZipCode> existing =
                zipCodeRepository.findByZipCode(cep);

        if (existing.isPresent()) {

            System.out.println("CEP ENCONTRADO NO CACHE");

            ZipCode zip = existing.get();

            ViaCepResponseDTO dto =
                    new ViaCepResponseDTO();

            dto.setCep(zip.getZipCode());
            dto.setLogradouro(zip.getStreet());
            dto.setBairro(zip.getDistrict());
            dto.setLocalidade(zip.getCity().getName());
            dto.setIbge(
                    String.valueOf(
                            zip.getCity().getIbgeCode()
                    )
            );

            return dto;
        }

        System.out.println("CONSULTANDO VIACEP");

        // =========================================
        // CONSULTA VIACEP
        // =========================================

        ViaCepResponseDTO response =
                restClient.get()
                        .uri("/{cep}/json", cep)
                        .retrieve()
                        .body(ViaCepResponseDTO.class);

        if (
                response == null
                        || Boolean.TRUE.equals(response.getErro())
        ) {

            throw new IllegalStateException(
                    "CEP inválido"
            );
        }

        // =========================================
        // SALVAR CACHE
        // =========================================

        City city =
                locationResolverService.resolveCity(
                        Integer.valueOf(response.getIbge())
                );

        ZipCode zip =
                ZipCode.builder()
                        .zipCode(cep)
                        .street(response.getLogradouro())
                        .district(response.getBairro())
                        .city(city)
                        .createdAt(LocalDateTime.now())
                        .build();

        zipCodeRepository.save(zip);

        return response;
    }
}