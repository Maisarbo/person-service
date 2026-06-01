package com.wholparts.person_service.dto;

import com.wholparts.person_service.enums.PersonStatus;
import com.wholparts.person_service.enums.PersonType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PersonUpdateDTO {

    // Nome ou Razão Social
    @NotBlank(message = "Nome é obrigatório")
    private String name;

    // Nome fantasia
    private String tradeName;

    // CPF ou CNPJ
    @NotBlank(message = "Documento é obrigatório")
    private String document;

    @Email(message = "E-mail inválido")
    private String email;

    private String phone;

    @NotNull(message = "Tipo da pessoa é obrigatório")
    private PersonType type;

    @NotNull(message = "Status é obrigatório")
    private PersonStatus status;
}