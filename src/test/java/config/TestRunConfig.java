package config;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import utils.ConfProperties;

public class TestRunConfig {
    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = ConfProperties.getProperty("base_URL");
    }
}
