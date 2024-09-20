import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;

public class PetstoreAPIUtils {

    // Setting the base URL of the API
    private static final String BASE_URL = "https://petstore.swagger.io/v2";

    public PetstoreAPIUtils() {
        RestAssured.baseURI = BASE_URL;
    }

    public Response addPet(int petID, String petName, String petStatus) {
        String newPetJson = "{"
                + "\"id\": " + petID + ","
                + "\"name\": \"" + petName + "\","
                + "\"status\": \"" + petStatus + "\""
                + "}";

        Response response;
        response = given()
                .contentType(ContentType.JSON)
                .body(newPetJson)
                .when()
                .post("/pet")
                .then()
                .statusCode(200)
                .extract().response();

        return response;
    }

    public Response deletePet(int petID) {
        return given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/pet/" + petID)
                .then()
                .statusCode(200)
                .extract().response();
    }

    public Response getPetByID(int petID) {
        return given()
                .contentType(ContentType.JSON)
                .when()
                .get("/pet/" + petID)
                .then()
                .statusCode(200)  // Assuming 200 OK status for valid request
                .extract().response();
    }

    public Response updatePetWithFormData(int petID, String petName, String petStatus) {
        return given()
                .contentType(ContentType.URLENC)
                .formParam("name", petName)
                .formParam("status", petStatus)
                .when()
                .post("/pet/" + petID)
                .then()
                .statusCode(200)  // Assuming 200 OK is the expected status for successful updates
                .extract().response();
    }
}
