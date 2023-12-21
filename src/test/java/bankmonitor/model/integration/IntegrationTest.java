package bankmonitor.model.integration;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Testcontainers
public class IntegrationTest {

    @Container
    public static PostgreSQLContainer<PostgresqlContainer> postgreSQLContainer = PostgresqlContainer.getInstance();

    @Autowired
    protected WebApplicationContext context;

    @Autowired
    protected MockMvc mvc;

    @Autowired
    protected DatabaseCleaner databaseCleaner;

    @BeforeEach
    protected void cleanDatabase() {
        databaseCleaner.clean();
    }

}
