package com.wholparts.person_service.controller;

import com.wholparts.person_service.dto.ViaCepResponseDTO;
import com.wholparts.person_service.service.ViaCepService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/addresses")
@RequiredArgsConstructor
public class ViaCepController {

    private final ViaCepService viaCepService;

    @GetMapping("/cep/{cep}")
    public ViaCepResponseDTO findByCep(
            @PathVariable String cep
    ) {

        return viaCepService.findByCep(cep);
    }
}