package tests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import utils.ConfUtils;

public class TestRunConfig {
    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = ConfUtils.getProperty("base_URL");
    }
}
