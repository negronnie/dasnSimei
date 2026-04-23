package br.com.negronnie.dasnSimei.configs;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mysql.MySQLContainer;

@Testcontainers
public abstract class DatabaseContainerBase {

    @Container
    @ServiceConnection
    @SuppressWarnings("resource")
    static MySQLContainer mysql = new MySQLContainer("mysql:9.5.0");

//    static {
//        mysql.start();
//    }
    // Com Container, Service Connection e TestContainers comentados, eu posso gerenciar o ciclo de vida do container.
    // Isso pode alterar o tempo de execução dos testes.
    // Senti que dessa forma, o tempo de execução ficou mais rápido no geral. Mas quebrou o meu repository

    @DynamicPropertySource
    static void overrideDatasource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
        registry.add("spring.flyway.enabled", () -> false);
    }
}
