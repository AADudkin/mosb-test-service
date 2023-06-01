package ru.mosb.client;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import ru.mosb.client.config.testcontainer.TestContainerInitializer;
import ru.mosb.client.repository.ClientRepository;

@ActiveProfiles("integration-test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = {TestContainerInitializer.class})
public class AbstractIT {

    @Autowired
    protected ClientRepository repository;

    @AfterEach
    void cleanUp() {
        repository.deleteAll();
    }
}
