package ru.mosb.client.api;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import ru.mosb.client.AbstractIT;
import ru.mosb.client.dto.ClientDto;
import ru.mosb.client.dto.ErrorResponse;
import ru.mosb.client.model.Client;
import ru.mosb.client.repository.ClientRepository;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ClientControllerTest extends AbstractIT {

    @Autowired
    private ClientRepository repository;
    @Autowired
    private TestRestTemplate restTemplate;

    @AfterEach
    public void cleanUp() {
        repository.deleteAll();
    }

    @Test
    public void shouldCreateClient() {
        ClientDto clientDto = new ClientDto()
                .setBankId("bankId")
                .setFirstName("fn")
                .setLastName("ln")
                .setPatronymic("pt")
                .setEmail("aaa@aaa.a")
                .setDateOfBirth(new Date())
                .setPassport("1234 432123")
                .setPhoneNumber("79887678765");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("x-source", "mail");
        ClientDto created = restTemplate
                .exchange("/api/client/", HttpMethod.POST, new HttpEntity<>(clientDto, httpHeaders), ClientDto.class)
                .getBody();

        assertNotNull(created.getId());

        Client actual = repository.findById(created.getId()).get();


        assertEquals(clientDto.getBankId(), actual.getBankId());
        assertEquals(clientDto.getFirstName(), actual.getFirstName());
        assertEquals(clientDto.getLastName(), actual.getLastName());
        assertEquals(clientDto.getPassport(), actual.getPassport());
        assertEquals(clientDto.getPatronymic(), actual.getPatronymic());
        assertEquals(clientDto.getEmail(), actual.getEmail());
        assertEquals(clientDto.getPhoneNumber(), actual.getPhoneNumber());
        assertEquals(clientDto.getDateOfBirth(), actual.getDateOfBirth());
    }

    @Test
    public void shouldGetClientById() {
        Client saved = repository.save(
                new Client()
                        .setFirstName("firstName")
        );

        ClientDto actual = restTemplate
                .getForEntity("/api/client/{id}", ClientDto.class, saved.getId())
                .getBody();

        assertNotNull(actual);
        assertEquals(saved.getFirstName(), actual.getFirstName());
    }

    @Test
    public void shouldReturn404IfNoClientById() {

        ResponseEntity<ErrorResponse> err = restTemplate
                .getForEntity("/api/client/{id}", ErrorResponse.class, 11);

        assertEquals(HttpStatus.NOT_FOUND, err.getStatusCode());
        assertEquals("Клиент с ИД = 11 не найден", err.getBody().getMessage());
    }
}