package br.com.negronnie.dasnSimei.controllers;

import br.com.negronnie.dasnSimei.configs.AbstractIntegrationTest;
import br.com.negronnie.dasnSimei.configs.TestConfig;
import br.com.negronnie.dasnSimei.dtos.MovimentoFinanceiroDTO;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MovimentoFinanceiroControllerIntegrationTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objMapper;

    @BeforeAll
    static void setup() {
        objMapper = new ObjectMapper();
        objMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        specification = new RequestSpecBuilder()
                .setBasePath("/v1/movimentos")
                .setPort(TestConfig.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
    }

    @Test
    @Order(1)
    @DisplayName("Upload de CSV de Movimentos")
    void uploadCSV() {
        File csv = new File("src/test/resources/nu_teste.csv");
        given().spec(specification)
                .multiPart("file", csv) // com o multipart o rest assured assume o content-type automaticamente
                .when().post("/upload")
                .then().statusCode(200);
    }

    @Test
    @Order(2)
    @DisplayName("Retornar Lista Completa de Movimentos")
    void obterMovimentos() {
        MovimentoFinanceiroDTO[] movimentos = given().spec(specification)
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .when().get()
                .then().statusCode(200)
                .extract().body().as(MovimentoFinanceiroDTO[].class);

        assertNotNull(movimentos);
        assertTrue(movimentos.length > 0);
    }

    @Test
    @Order(3)
    @DisplayName("Retornar Movimento pelo ID")
    void obterMovimentoPorID() {
        MovimentoFinanceiroDTO movimento = given().spec(specification)
                .pathParam("id", 9)
                .when().get("/{id}")
                .then().statusCode(200)
                .extract().body().as(MovimentoFinanceiroDTO.class);

        assertNotNull(movimento);
        assertEquals(new BigDecimal("980.00"), movimento.valor());
        assertEquals(LocalDate.of(2025,6,3) ,movimento.data());
        assertTrue(movimento.descricao().contains("Consult"));
    }

}