package ru.mosb.client.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mosb.client.dto.ClientDto;
import ru.mosb.client.dto.FilterDto;
import ru.mosb.client.exception.UnknownEntityException;
import ru.mosb.client.mapper.ClientMapper;
import ru.mosb.client.model.Client;
import ru.mosb.client.repository.ClientRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository repository;
    private final ClientMapper mapper;

    public ClientDto save(ClientDto dto) {
        Client saved = repository.save(mapper.toEntity(dto));
        return mapper.toDto(saved);
    }

    public ClientDto findById(Long id) {
        Client found = repository.findById(id)
                .orElseThrow(() -> new UnknownEntityException("entity not found by id", id));
        return mapper.toDto(found);
    }

    public List<ClientDto> findByFilter(FilterDto filterDto) {
        List<Client> clients = repository.findAll(transformFilterToSpecification(filterDto));
        return mapper.toDtoList(clients);
    }

    private Specification<Client> transformFilterToSpecification(FilterDto filterDto) {
        List<Specification<Client>> specs = new ArrayList<>();

        if (StringUtils.isNotBlank(filterDto.getFirstName())) {
            specs.add(fieldEqualsSpec("firstName", filterDto.getFirstName()));
        }
        if (StringUtils.isNotBlank(filterDto.getLastName())) {
            specs.add(fieldEqualsSpec("lastName", filterDto.getLastName()));
        }
        if (StringUtils.isNotBlank(filterDto.getPatronymic())) {
            specs.add(fieldEqualsSpec("patronymic", filterDto.getPatronymic()));
        }
        if (StringUtils.isNotBlank(filterDto.getPhoneNumber())) {
            specs.add(fieldEqualsSpec("phoneNumber", filterDto.getPhoneNumber()));
        }
        if (StringUtils.isNotBlank(filterDto.getEmail())) {
            specs.add(fieldEqualsSpec("email", filterDto.getEmail()));
        }

        if (specs.isEmpty()) {
            return (client, cq, cb) -> cb.disjunction();
        } else {
            return specs.stream().reduce(
                    (client, cq, cb) -> cb.conjunction(),
                    Specification::and
            );
        }
    }

    private Specification<Client> fieldEqualsSpec(String field, String value) {
        return (client, cq, cb) -> cb.equal(client.get(field), value);
    }
}
