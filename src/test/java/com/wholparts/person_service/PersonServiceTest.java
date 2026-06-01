package com.wholparts.person_service;

import com.wholparts.person_service.dto.PersonCreationDTO;
import com.wholparts.person_service.dto.PersonViewDTO;
import com.wholparts.person_service.enums.PersonStatus;
import com.wholparts.person_service.enums.PersonType;
import com.wholparts.person_service.mapper.PersonMapper;
import com.wholparts.person_service.model.Person;
import com.wholparts.person_service.repository.PersonRepository;
import com.wholparts.person_service.service.AddressService;
import com.wholparts.person_service.service.PersonCodeService;
import com.wholparts.person_service.service.PersonService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private PersonMapper personMapper;

    @Mock
    private AddressService addressService;

    @Mock
    private PersonCodeService personCodeService;

    @InjectMocks
    private PersonService personService;

    private Person person;
    private PersonCreationDTO creationDTO;
    private PersonViewDTO viewDTO;

    @BeforeEach
    void setup() {

        UUID id = UUID.randomUUID();

        person = Person.builder()
                .id(id)
                .code("C0001")
                .name("João Silva")
                .document("12345678901")
                .email("joao@email.com")
                .type(PersonType.CUSTOMER)
                .status(PersonStatus.ACTIVE)
                .addresses(new ArrayList<>())
                .build();

        creationDTO = PersonCreationDTO.builder()
                .name("João Silva")
                .document("12345678901")
                .email("joao@email.com")
                .type(PersonType.CUSTOMER)
                .addresses(new ArrayList<>())
                .build();

        viewDTO = PersonViewDTO.builder()
                .id(id)
                .code("C0001")
                .name("João Silva")
                .document("12345678901")
                .email("joao@email.com")
                .type(PersonType.CUSTOMER)
                .status(PersonStatus.ACTIVE)
                .build();
    }

    @Test
    void deveCriarPessoaComSucesso() {

        System.out.println("=== TESTE CREATE SUCESSO ===");

        when(personRepository.existsByDocument(anyString()))
                .thenReturn(false);

        when(personRepository.existsByEmail(anyString()))
                .thenReturn(false);

        when(personMapper.toEntity(any()))
                .thenReturn(person);

        when(personCodeService.generate(any()))
                .thenReturn("C0001");

        when(personRepository.save(any()))
                .thenReturn(person);

        when(personRepository.findById(person.getId()))
                .thenReturn(Optional.of(person));

        when(personMapper.toViewDTO(person))
                .thenReturn(viewDTO);

        PersonViewDTO result =
                personService.create(creationDTO);

        assertNotNull(result);

        System.out.println("ID: " + result.getId());
        System.out.println("Código: " + result.getCode());
        System.out.println("Nome: " + result.getName());

        verify(personRepository).save(any());
    }

    @Test
    void deveLancarErroQuandoDocumentoJaExiste() {

        System.out.println("=== TESTE DOCUMENTO DUPLICADO ===");

        when(personRepository.existsByDocument(anyString()))
                .thenReturn(true);

        IllegalArgumentException ex =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> personService.create(creationDTO)
                );

        System.out.println(ex.getMessage());

        assertEquals(
                "Documento já cadastrado",
                ex.getMessage()
        );
    }

    @Test
    void deveLancarErroQuandoEmailJaExiste() {

        System.out.println("=== TESTE EMAIL DUPLICADO ===");

        when(personRepository.existsByDocument(anyString()))
                .thenReturn(false);

        when(personRepository.existsByEmail(anyString()))
                .thenReturn(true);

        IllegalArgumentException ex =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> personService.create(creationDTO)
                );

        System.out.println(ex.getMessage());

        assertEquals(
                "Email já cadastrado",
                ex.getMessage()
        );
    }

    @Test
    void deveLancarErroDocumentoInvalido() {

        System.out.println("=== TESTE DOCUMENTO INVÁLIDO ===");

        creationDTO.setDocument("123");

        IllegalArgumentException ex =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> personService.create(creationDTO)
                );

        System.out.println(ex.getMessage());

        assertEquals(
                "CPF ou CNPJ inválido",
                ex.getMessage()
        );
    }

    @Test
    void deveBuscarPorId() {

        System.out.println("=== TESTE FIND BY ID ===");

        when(personRepository.findById(person.getId()))
                .thenReturn(Optional.of(person));

        when(personMapper.toViewDTO(person))
                .thenReturn(viewDTO);

        PersonViewDTO result =
                personService.findById(person.getId());

        assertNotNull(result);

        System.out.println("Pessoa encontrada:");
        System.out.println(result.getName());
    }

    @Test
    void deveBuscarPorDocumento() {

        System.out.println("=== TESTE FIND BY DOCUMENT ===");

        when(personRepository.findByDocument(
                person.getDocument()))
                .thenReturn(Optional.of(person));

        when(personMapper.toViewDTO(person))
                .thenReturn(viewDTO);

        PersonViewDTO result =
                personService.findByDocument(
                        person.getDocument()
                );

        assertNotNull(result);

        System.out.println(result.getDocument());
    }

    @Test
    void deveAtivarPessoa() {

        System.out.println("=== TESTE ACTIVATE ===");

        person.setStatus(PersonStatus.INACTIVE);

        when(personRepository.findById(person.getId()))
                .thenReturn(Optional.of(person));

        personService.activate(person.getId());

        assertEquals(
                PersonStatus.ACTIVE,
                person.getStatus()
        );

        System.out.println("Status: " +
                person.getStatus());
    }

    @Test
    void deveDesativarPessoa() {

        System.out.println("=== TESTE DEACTIVATE ===");

        when(personRepository.findById(person.getId()))
                .thenReturn(Optional.of(person));

        personService.deactivate(person.getId());

        assertEquals(
                PersonStatus.INACTIVE,
                person.getStatus()
        );

        System.out.println("Status: " +
                person.getStatus());
    }

    @Test
    void deveLancarErroPessoaNaoEncontrada() {

        System.out.println("=== TESTE PESSOA NÃO ENCONTRADA ===");

        UUID id = UUID.randomUUID();

        when(personRepository.findById(id))
                .thenReturn(Optional.empty());

        EntityNotFoundException ex =
                assertThrows(
                        EntityNotFoundException.class,
                        () -> personService.findById(id)
                );

        System.out.println(ex.getMessage());

        assertTrue(
                ex.getMessage()
                        .contains("Pessoa não encontrada")
        );
    }
    @Test
    void naoDevePermitirPessoaSemEndereco() {

        System.out.println("=== TESTE PESSOA SEM ENDEREÇO ===");

        PersonCreationDTO dto = PersonCreationDTO.builder()
                .name("João Silva")
                .document("12345678901")
                .email("joao@email.com")
                .type(PersonType.CUSTOMER)
                .addresses(new ArrayList<>())
                .build();

        ValidatorFactory factory =
                Validation.buildDefaultValidatorFactory();

        Validator validator =
                factory.getValidator();

        Set<ConstraintViolation<PersonCreationDTO>> violations =
                validator.validate(dto);

        violations.forEach(v ->
                System.out.println(
                        v.getPropertyPath() + " -> "
                                + v.getMessage()
                )
        );

        assertFalse(violations.isEmpty());
    }
}