package io.quarkiverse.quarkus.azure.keyvault.it;

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
                .body(is("Hello azure-key-vault foo=s3cr3t bar=missing"));
    }
}
