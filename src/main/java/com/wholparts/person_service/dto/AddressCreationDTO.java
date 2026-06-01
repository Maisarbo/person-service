package com.wholparts.person_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressCreationDTO {

    @NotBlank(message = "Rua é obrigatória")
    private String street;

    @NotBlank(message = "Número é obrigatório")
    private String number;

    private String complement;

    @NotBlank(message = "CEP é obrigatório")
    @Pattern(
            regexp = "\\d{5}-?\\d{3}",
            message = "CEP inválido"
    )
    private String zipCode;

    @NotBlank(message = "Bairro é obrigatório")
    private String districtName;

    @NotNull(message = "Cidade é obrigatória")
    private Integer cityIbgeCode;
}