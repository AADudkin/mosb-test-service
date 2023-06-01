package ru.mosb.client.config.testcontainer;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

public class PostgresContainerHolder {

    private static PostgreSQLContainer<?> container;

    public static PostgreSQLContainer<?> getInstance() {
        initializeIfNeeded();
        return container;
    }

    private static void initializeIfNeeded() {
        if (container == null) {
            container = new PostgreSQLContainer<>(
                DockerImageName.parse("postgres:15.3-alpine"))
                .withDatabaseName("integration-tests-db")
                .withUsername("mosb")
                .withPassword("mosb")
                .withCreateContainerCmdModifier(cmd -> cmd.withName("testcontainer-postgres"));

            container.start();
        }
    }

}
