package br.com.negronnie.dasnSimei.configs;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AbstractIntegrationTest extends DatabaseContainerBase {

    @BeforeEach
    void setUpRestAssured() {
        RestAssured.baseURI = "http://localhost:" + TestConfig.SERVER_PORT;
    }
}