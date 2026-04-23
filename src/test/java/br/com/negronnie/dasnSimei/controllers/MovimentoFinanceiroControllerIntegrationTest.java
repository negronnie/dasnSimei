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
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
    @DisplayName("Upload de Objeto de Venda")
    void uploadVenda() {
        File csv = new File("src/test/resources/vendaTeste.csv");
        given().spec(specification)
                .multiPart("file", csv) // com o multipart o rest assured assume o content-type automaticamente
                .when().post("/upload")
                .then().statusCode(200);
    }

    @Test
    @Order(3)
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
    @Order(4)
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

    @Test
    @DisplayName("Retornar Movimentos Contendo String na Descrição")
    void findByDescricaoContem(){
        MovimentoFinanceiroDTO[] movimentos = given().spec(specification)
                .pathParam("contem", "Consult")
            .when().get("/contem/{contem}")
            .then().statusCode(200)
            .extract().body().as(MovimentoFinanceiroDTO[].class);

        List<MovimentoFinanceiroDTO> movs = Arrays.stream(movimentos).toList();

        assertNotNull(movimentos);
        assertTrue(movs
                .getFirst()
                .descricao()
                .contains("Consult"));
        assertEquals(LocalDate.of(2025,6,3) ,movs.getFirst().data());
        assertEquals(new BigDecimal("980.00"), movs.getFirst().valor());
    }

    @Test
    @DisplayName("Retornar o Valor Anual")
    void obterTotalAnual() {
        BigDecimal totalAnual = given().spec(specification)
            .pathParam("ano", 2025)
            .when().get("/totais/{ano}")
            .then().statusCode(200)
            .extract().body().as(BigDecimal.class);

        assertNotNull(totalAnual);
        assertEquals(new BigDecimal("3241.70"), totalAnual);
    }

    @Test
    @DisplayName("Retornar o Valor do Mês Selecionado")
    void obterTotalMensal() {
        BigDecimal totalMensal = given().spec(specification)
                .pathParam("ano", 2025)
                .pathParam("mes", 2)
                .when().get("/totais/{ano}/{mes}")
                .then().statusCode(200)
                .extract().body().as(BigDecimal.class);

        assertNotNull(totalMensal);
        assertEquals(new BigDecimal("200.50"), totalMensal);
    }

    @Test
    @DisplayName("Retornar o Valor Total do Trimestre Selecionado")
    void obterTotalTrimestre() {
        BigDecimal totalTrimestre = given().spec(specification)
                .pathParam("ano", 2025)
                .pathParam("trimestre", 1)
                .when().get("/totais/{ano}/Q{trimestre}")
                .then().statusCode(200)
                .extract().body().as(BigDecimal.class);

        assertNotNull(totalTrimestre);
        assertEquals(new BigDecimal("760.40"), totalTrimestre);
    }

    @Test
    @DisplayName("Retornar o Valor Total de Todos os Meses")
    void obterTodosOsMeses() {
        Map mensais = given().spec(specification)
                .pathParam("ano", 2025)
                .when().get("/totais/{ano}/")
                .then().statusCode(200)
                .extract().body().as(Map.class);

        assertNotNull(mensais);
        assertEquals(new BigDecimal("150.0"), new BigDecimal(mensais.get("Janeiro").toString()));
        assertEquals(new BigDecimal("3241.7"), new BigDecimal(mensais.get("Total 2025").toString()));
        assertNull(mensais.get("Setembro"));
    }

    @Test
    @DisplayName("Retornar o Valor Total de Todos os Trimestres")
    void obterTodosOsTrimestres() {
        Map trimestrais = given().spec(specification)
                .pathParam("ano", 2025)
                .when().get("/totais/{ano}/Q")
                .then().statusCode(200)
                .extract().body().as(Map.class);

        assertNotNull(trimestrais);
        assertEquals(new BigDecimal("760.4"), new BigDecimal(trimestrais.get("Trimestre 1").toString()));
        assertNull(trimestrais.get("Trimestre 4"));
    }

    @Test
    @DisplayName("Obter valor Total da Categoria Informada")
    void obterTotalCategoria() {
        BigDecimal totalCategoria = given().spec(specification)
                .pathParam("categoria", "vendas")
                .when().get("/totais/tipo/{categoria}")
                .then().statusCode(200)
                .extract().body().as(BigDecimal.class);

        assertNotNull(totalCategoria);
        assertEquals(new BigDecimal("230.00"), totalCategoria);
    }

    @Test
    @DisplayName("Retornar Relatório Completo")
    void obterRelatorioCompleto() {
        Map completo = given().spec(specification)
                .pathParam("ano", 2025)
                .when().get("/relatorio/{ano}")
                .then().statusCode(200)
                .extract().body().as(Map.class);

        assertNotNull(completo);

        Map trimestres = (Map) completo.get("Trimestres");
        assertEquals(new BigDecimal("760.4"), new BigDecimal(trimestres.get("Trimestre 1").toString()));

        Map mensais = (Map) completo.get("Mensais");
        assertEquals(new BigDecimal("409.9"), new BigDecimal(mensais.get("Março").toString()));

        assertEquals(new BigDecimal("230.0"), new BigDecimal(completo.get("Valor à Venda").toString()));
        assertEquals(new BigDecimal("3241.7"), new BigDecimal(completo.get("Total 2025").toString()));
        assertNull(completo.get("Previsao de Faturamento"));
    }

}