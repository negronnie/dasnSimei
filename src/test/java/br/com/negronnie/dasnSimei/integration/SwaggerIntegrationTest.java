package br.com.negronnie.dasnSimei.integration;

import br.com.negronnie.dasnSimei.configs.AbstractIntegrationTest;
import br.com.negronnie.dasnSimei.configs.TestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SwaggerIntegrationTest extends AbstractIntegrationTest {

	@Test
	@DisplayName("Publicação da Página do Swagger")
	void testDeveCarregarPaginaDoSwagger() {

		var conteudo = given()
			.basePath("/swagger-ui/index.html")
			.port(TestConfig.SERVER_PORT)
			.when().get()
			.then().statusCode(200)
			.extract().body().asString();

		assertTrue(conteudo.contains("Swagger UI"));

	}

}
