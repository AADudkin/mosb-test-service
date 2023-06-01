package ru.mosb.client.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import ru.mosb.client.AbstractIT;
import ru.mosb.client.dto.ClientDto;
import ru.mosb.client.dto.ErrorResponse;
import ru.mosb.client.dto.FilterDto;
import ru.mosb.client.model.Client;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ClientControllerTest extends AbstractIT {

    @Autowired
    private TestRestTemplate restTemplate;

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

        HttpHeaders headers = new HttpHeaders();
        headers.add("x-source", "mail");
        ClientDto created = restTemplate
                .exchange("/api/client/", HttpMethod.POST, new HttpEntity<>(clientDto, headers), ClientDto.class)
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
    public void shouldFindClientByFilter() {
        repository.saveAll(
                Arrays.asList(
                        new Client()
                                .setFirstName("Alex")
                                .setPatronymic("Petrovitch")
                                .setEmail("apa@mail.ru"),
                        new Client()
                                .setFirstName("Mikola")
                                .setPatronymic("Petrovich")
                                .setEmail("apa@mail.ru"),
                        new Client()
                                .setFirstName("Alex")
                                .setPatronymic("Ivanovich")
                                .setEmail("apa@mail.ru")
                )
        );

        List<ClientDto> response = restTemplate.exchange(
                "/api/client/filter?firstName={firstName}&patronymic={patronymic}&email={email}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ClientDto>>() {},
                "Alex",
                "Petrovitch",
                "apa@mail.ru"
        ).getBody();

        assertEquals(1, response.size());
        ClientDto actual = response.get(0);
        assertEquals("Alex", actual.getFirstName());
        assertEquals("Petrovitch", actual.getPatronymic());
        assertEquals("apa@mail.ru", actual.getEmail());
    }

    @Test
    public void shouldReturnEmptyListIfEmptyFilter() {
        repository.saveAll(
                Arrays.asList(
                        new Client()
                                .setFirstName("Alex")
                                .setPatronymic("Petrovitch")
                                .setEmail("apa@mail.ru"),
                        new Client()
                                .setFirstName("Mikola")
                                .setPatronymic("Petrovich")
                                .setEmail("apa@mail.ru")
                )
        );

        List<ClientDto> response = restTemplate.exchange(
                "/api/client/filter",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ClientDto>>() {}
        ).getBody();

        assertEquals(0, response.size());
    }

    @Test
    public void shouldReturn404IfNoClientById() {

        ResponseEntity<ErrorResponse> err = restTemplate
                .getForEntity("/api/client/{id}", ErrorResponse.class, 11);

        assertEquals(HttpStatus.NOT_FOUND, err.getStatusCode());
        assertEquals("Клиент с ИД = 11 не найден", err.getBody().getMessage());
    }

    @Test
    public void shouldReturn400IfMalformedPhoneNumber() {
        ClientDto clientDto = new ClientDto()
                .setPhoneNumber("something wrong with that phone");

        HttpHeaders headers = new HttpHeaders();
        headers.add("x-source", "mobile");
        ResponseEntity<ErrorResponse> err = restTemplate
                .exchange("/api/client/", HttpMethod.POST, new HttpEntity<>(clientDto, headers), ErrorResponse.class);

        assertEquals(HttpStatus.BAD_REQUEST, err.getStatusCode());
        assertEquals(Map.of("phoneNumber", "phone should have 7XXXXXXXXXX format"), err.getBody().getFieldErrors());
    }

    @Test
    public void shouldReturn400IfMalformedPassport() {
        ClientDto clientDto = new ClientDto()
                .setPhoneNumber("79887678765")
                .setPassport("something wrong with that passport");

        HttpHeaders headers = new HttpHeaders();
        headers.add("x-source", "mobile");
        ResponseEntity<ErrorResponse> err = restTemplate
                .exchange("/api/client/", HttpMethod.POST, new HttpEntity<>(clientDto, headers), ErrorResponse.class);

        assertEquals(HttpStatus.BAD_REQUEST, err.getStatusCode());
        assertEquals(Map.of("passport", "passport should have XXXX XXXXXX format"), err.getBody().getFieldErrors());
    }
}