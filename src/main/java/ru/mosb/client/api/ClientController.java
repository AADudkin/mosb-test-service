package ru.mosb.client.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.mosb.client.dto.ClientDto;
import ru.mosb.client.dto.FilterDto;
import ru.mosb.client.dto.XSource;
import ru.mosb.client.service.ClientService;
import ru.mosb.client.validator.ClientValidator;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/client")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;
    private final ClientValidator validator;

    @PostMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ClientDto create(@Valid @RequestBody ClientDto request, @RequestHeader("x-source") XSource xSource) {
        log.debug("Received request from source = {} for client creation. Payload {}", xSource, request);
        validator.validate(request, xSource);
        return clientService.save(request);
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ClientDto getById(@PathVariable Long id) {
        return clientService.findById(id);
    }

    @GetMapping(path = "/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ClientDto> getByFilter(@ModelAttribute FilterDto filter) {
        return clientService.findByFilter(filter);
    }
}
