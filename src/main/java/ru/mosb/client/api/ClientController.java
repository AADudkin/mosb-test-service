package ru.mosb.client.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.mosb.client.dto.ClientDto;
import ru.mosb.client.dto.FilterDto;
import ru.mosb.client.dto.XSource;
import ru.mosb.client.service.ClientService;
import ru.mosb.client.validator.ClientValidator;

import java.util.List;

@RestController
@RequestMapping("/api/client")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;
    private final ClientValidator validator;

    @PostMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ClientDto create(@RequestBody ClientDto request, @RequestHeader("x-source") XSource xSource) {
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
