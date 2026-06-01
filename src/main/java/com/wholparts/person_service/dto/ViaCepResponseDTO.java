package com.wholparts.person_service.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ViaCepResponseDTO {

    private String cep;

    private String logradouro;

    private String bairro;

    private String localidade;

    private String uf;

    private String ibge;

    private Boolean erro;
}