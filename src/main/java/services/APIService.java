package services;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.http.ContentType;

import org.springframework.web.client.RestTemplate;

import org.json.JSONObject;

import response.CustomResponse;
import utils.ConfUtils;

import dto.*;

import java.io.File;
import java.time.Instant;
import java.util.*;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public final class APIService {

    // Creating an object of RestTemplate
    static String baseURL = ConfUtils.getProperty("base_URL");
    private final RestTemplate restTemplate = new RestTemplate();


    /*
    Add a pet
     */
    public static CustomResponse<PetDTO> addPet(PetDTO petDTO) {
        Response restAssuredResponse = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(petDTO)
                .when()
                .post("/pet")
                .then()
                .statusCode(200)
                .extract()
                .response();

        PetDTO responseData = restAssuredResponse.as(PetDTO.class);
        return new CustomResponse<>(
                restAssuredResponse.getStatusCode(),
                restAssuredResponse.getStatusLine(),
                responseData
        );
    }
    /*
    Upload pet image using URL
     */
    public static CustomResponse<Void> uploadPetImageFromURL(long petId, String imageUrl) throws Exception {
        // Step 1: Download the image from the given URL
        URL url = new URL(imageUrl);
        InputStream in = url.openStream();

        // create a temporary file
        Path tempFile = Files.createTempFile("petImage", ".png");
        FileOutputStream out = new FileOutputStream(tempFile.toFile());

        // copy the contents of the image URL to the temp file
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = in.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
        }

        in.close();
        out.close();

        // Step 2: Upload the image file to the pet
        Response restAssuredResponse = RestAssured.given()
                .contentType(ContentType.MULTIPART)
                .multiPart("file", tempFile.toFile())
                .when()
                .post("/pet/" + petId + "/uploadImage")
                .then()
                .extract().response();

        // delete the temporary file after upload
        Files.delete(tempFile);

        return new CustomResponse<>(
                restAssuredResponse.getStatusCode(),
                restAssuredResponse.getStatusLine(),
                null
        );
    }

    /*
    Upload non-image file to pet
     */
    public static CustomResponse<Void> uploadNonImageFileToPet(long petId, File nonImageFile) {
        Response restAssuredResponse = RestAssured.given()
                .contentType("multipart/form-data")
                .multiPart("file", nonImageFile)
                .post("/pet/" + petId + "/uploadImage")
                .then()
                .extract()
                .response();

        return new CustomResponse<>(
                restAssuredResponse.getStatusCode(),
                restAssuredResponse.getStatusLine(),
                null
        );
    }

    /*
    Delete pet by ID
     */
    public static CustomResponse<Void> deletePet(long petID) {
        Response restAssuredResponse = RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/pet/" + petID)
                .then()
                .extract().response();

        return new CustomResponse<>(
                restAssuredResponse.getStatusCode(),
                restAssuredResponse.getStatusLine(),
                null
        );
    }

    /*
    Get pet by ID
     */
    public static CustomResponse<PetDTO> getPetByID(long petID) {
        Response restAssuredResponse = RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .get("/pet/" + petID)
                .then()
                .extract()
                .response();

        PetDTO petDTO = restAssuredResponse.as(PetDTO.class);
        return new CustomResponse<>(
                restAssuredResponse.getStatusCode(),
                restAssuredResponse.getStatusLine(),
                petDTO
        );
    }

    /*
    Update pet using form data
     */
    public static CustomResponse<Void> updatePetWithFormData(long petID, String petName, String petStatus) {
        Response restAssuredResponse = RestAssured.given()
                .contentType(ContentType.URLENC)
                .formParam("name", petName)
                .formParam("status", petStatus)
                .when()
                .post("/pet/" + petID)
                .then()
                .extract().response();

        return new CustomResponse<>(
                restAssuredResponse.getStatusCode(),
                restAssuredResponse.getStatusLine(),
                null
        );
    }

    /*
    Find pet by status
     */
    public static CustomResponse<List<PetDTO>> findPetsByStatus (String status) {
        Response restAssuredResponse = RestAssured.given()
                .contentType(ContentType.JSON)
                .queryParam("status", status)
                .when()
                .get("/pet/findByStatus")
                .then()
                .extract()
                .response();

        List<PetDTO> petList = Arrays.asList(restAssuredResponse.as(PetDTO[].class));
        return new CustomResponse<>(
                restAssuredResponse.getStatusCode(),
                restAssuredResponse.getStatusLine(),
                petList
        );
    }

    /*
    Get pet without status check
     */
    public static CustomResponse<Void> getPetWithoutStatusCheck(long petID) {
        Response restAssuredResponse = RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .get("/pet/" + petID)
                .andReturn();

        PetDTO petDTO = restAssuredResponse.as(PetDTO.class);
        return new CustomResponse<>(
                restAssuredResponse.getStatusCode(),
                restAssuredResponse.getStatusLine(),
                null
        );
    }

    /*
    Get store inventory
     */
    public static CustomResponse<Map<String, Integer>> getStoreInventory() {
        Response restAssuredResponse = RestAssured.given()
                .baseUri(baseURL)
                .when()
                .get("/store/inventory")
                .then()
                .extract()
                .response();

        Map<String, Integer> inventory = restAssuredResponse.as(Map.class);
        return new CustomResponse<>(
                restAssuredResponse.getStatusCode(),
                restAssuredResponse.getStatusLine(),
                inventory
        );
    }

    /*
    Place order for a pet
     */
    public static CustomResponse<OrderDTO> placeOrderForPet(long petId, int quantity) {
        // Generate random unique orderId
        int orderId = new Random().nextInt(1, 10000);

        // Create the order details dynamically
        Map<String, Object> orderDetails = new HashMap<>();
        orderDetails.put("id", orderId);
        orderDetails.put("petId", petId);
        orderDetails.put("quantity", quantity);
        orderDetails.put("shipDate", Instant.now().toString());
        orderDetails.put("status", "placed");
        orderDetails.put("complete", true);

        // send POST request to place the order
        Response restAssuredResponse = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(orderDetails)
                .when()
                .post("/store/order")
                .then()
                .extract().response();

        OrderDTO orderDTO = restAssuredResponse.as(OrderDTO.class);
        return new CustomResponse<>(
                restAssuredResponse.getStatusCode(),
                restAssuredResponse.getStatusLine(),
                orderDTO
        );
    }

    /*
    Get order by ID
     */
    public static CustomResponse<OrderDTO> getOrderById(long orderId) {
        Response restAssuredResponse = RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .get("/store/order/" + orderId)
                .then()
                .extract().response();

        OrderDTO orderDTO = restAssuredResponse.as(OrderDTO.class);
        return new CustomResponse<>(
                restAssuredResponse.getStatusCode(),
                restAssuredResponse.getStatusLine(),
                orderDTO
        );
    }

    /*
    Delete order by ID
     */
    public static CustomResponse<Void> deleteOrderById(long orderId) {
        Response restAssuredResponse = RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/store/order/" + orderId)
                .then()
                .extract().response();

        return new CustomResponse<>(
                restAssuredResponse.getStatusCode(),
                restAssuredResponse.getStatusLine(),
                null  // No data for a DELETE request
        );

//    public CustomCustomResponse<Void><Void>Entity<String> createUser(UserDTO userDTO) {
//        String url = baseURL + "/user";
//
//        // Set the headers
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        // Create the HttpEntity object with headers and the user payload
//        HttpEntity<UserDTO> requestEntity = new HttpEntity<>(userDTO, headers);
//
//        // Make the POST request to create the user
//        return restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
    }
}
