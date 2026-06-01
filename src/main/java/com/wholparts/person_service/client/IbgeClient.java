package com.wholparts.person_service.client;

import com.wholparts.person_service.dto.IbgeCityDTO;
import com.wholparts.person_service.dto.IbgeStateDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "ibge", url = "https://servicodados.ibge.gov.br/api/v1")
public interface IbgeClient {

    @GetMapping("/localidades/estados")
    List<IbgeStateDTO> fetchStates();

    @GetMapping("/localidades/estados/{uf}/municipios")
    List<IbgeCityDTO> fetchCities(@PathVariable String uf);
}
