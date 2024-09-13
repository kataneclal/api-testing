import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PetstoreAPITest {

    private static final String BASE_URL = "https://petstore.swagger.io/v2";

    @BeforeEach
    public void setup() {
        RestAssured.baseURI = BASE_URL;
    }

    /**
     * Test case #1: Get details of a pet by ID
     */
    @Test
    public void testGetPetByID() {
        // Given: a pet id
        int petID = 1;

        // When: Making a GET request to fetch the pet details
        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/pet/" + petID)
                .then()
                .statusCode(200)
                .extract().response();

        // Then: Verification of pet details
        String petName = response.jsonPath().getString("name");
        assertNotNull("Pet name: " + petName);
    }

    /**
     * Test case #2:  Add a new pet
     */
    @Test
    public void testAddPet() {
        // Given: The JSON body of the new pet
        String newPetJson = "{"
                + "\"id\": 12345,"
                + "\"name\": \"Boris\","
                + "\"status\": \"available\""
                + "}";

        // When: We make a POST request to add a new pet
        Response response = given()
                .contentType(ContentType.JSON)
                .body(newPetJson)
                .when()
                .post("/pet")
                .then()
                .statusCode(200)
                .extract().response();

        // Then: Verify that the pet was added successfully
        String petName = response.jsonPath().getString("name");
        assertEquals("Boris", petName);
    }

    /**
     * Test case #3: Delete a pet.
     */
    @Test
    public void testDeletePet() {
        // Given: A pet ID that exists (12345 is the ID added earlier)
        int petID = 12345;

        // When: We make a DELETE request to delete the pet
        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/pet/" + petID)
                .then()
                .statusCode(200)
                .extract().response();

        // Then: Verify that the pet was deleted successfully
        String message = response.jsonPath().getString("message");
        assertEquals("12345", message);
    }
}
