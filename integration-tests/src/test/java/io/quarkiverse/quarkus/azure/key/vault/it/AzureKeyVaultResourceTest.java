package io.quarkiverse.quarkus.azure.key.vault.it;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class AzureKeyVaultResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
                .when().get("/azure-key-vault")
                .then()
                .statusCode(200)
                .body(is("Hello azure-key-vault coucou=Passw0rd@Thu Apr 22 19:38:44 CEST 2021 titi=defaulttiti"));
    }
}
