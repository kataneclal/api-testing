package services;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.http.ContentType;

import org.springframework.web.client.RestTemplate;

import response.CustomResponse;
import utils.ConfProperties;

import dto.*;
import utils.TestValueGenerator;

import java.io.File;
import java.time.Instant;
import java.util.*;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * <p>This class contains all the Petstore Sample API methods</p>
 * <p>See: <a href="petstore.swagger.io/">https://petstore.swagger.io/</a></p>
 */


public final class APIService {

    // Creating an object of RestTemplate
    static String baseURL = ConfProperties.getProperty("base_URL");
    private final RestTemplate restTemplate = new RestTemplate();   // Creating an object of RestTemplate


    /**
    "Add a pet" method
     */
    public static CustomResponse<PetDTO> addPet(PetDTO petDTO) {
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(petDTO)
                .post("/pet");

        return CustomResponse.fromRestAssuredResponse(response, PetDTO.class);
    }
    /**
    "Upload pet image using URL" method
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
        Response response = RestAssured.given()
                .contentType(ContentType.MULTIPART)
                .multiPart("file", tempFile.toFile())
                .post("/pet/" + petId + "/uploadImage");

        // delete the temporary file after upload
        Files.delete(tempFile);

        return CustomResponse.fromRestAssuredResponse(response, Void.class);
    }

    /**
    "Upload non-image file to pet" method
     */
    public static CustomResponse<Void> uploadNonImageFileToPet(long petId, File nonImageFile) {
        Response response = RestAssured.given()
                .contentType("multipart/form-data")
                .multiPart("file", nonImageFile)
                .post("/pet/" + petId + "/uploadImage");

        return CustomResponse.fromRestAssuredResponse(response, Void.class);
    }

    /**
    "Delete pet by ID" method
     */
    public static CustomResponse<Void> deletePet(long petID) {
        Response response = RestAssured.given()
                .delete("/pet/" + petID);

        return CustomResponse.fromRestAssuredResponse(response, Void.class);
    }

    /**
    "Get pet by ID" method
     */
    public static CustomResponse<PetDTO> getPetByID(long petID) {
        Response response = RestAssured.given()
                .get("/pet/" + petID);

        return CustomResponse.fromRestAssuredResponse(response, PetDTO.class);
    }

    /**
    "Update pet using form data" method
     */
    public static CustomResponse<Void> updatePetWithFormData(long petID, String petName, String petStatus) {
        Response response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .formParam("name", petName)
                .formParam("status", petStatus)
                .post("/pet/" + petID);

        return CustomResponse.fromRestAssuredResponse(response, Void.class);
    }

    /**
    "Find pet by status" method
     */
    public static CustomResponse<List<PetDTO>> findPetsByStatus(String status) {
        Response response = RestAssured.given()
                .queryParam("status", status)
                .get("/pet/findByStatus");

        // сonvert the response to an array of PetDTO
        CustomResponse<PetDTO[]> arrayResponse = CustomResponse.fromRestAssuredResponse(response, PetDTO[].class);

        // сonvert the array to a List
        List<PetDTO> petList = arrayResponse.getData() != null ? Arrays.asList(arrayResponse.getData()) : null;

        // return a new CustomResponse with the list instead of an array
        return CustomResponse.<List<PetDTO>>builder()
                .statusCode(arrayResponse.getStatusCode())
                .message(arrayResponse.getMessage())
                .data(petList)
                .build();
    }

    /**
    "Get pet without status check" method
     */
    public static CustomResponse<Void> getPetWithoutStatusCheck(long petID) {
        Response response = RestAssured.given()
                .get("/pet/" + petID);

        return CustomResponse.fromRestAssuredResponse(response, Void.class);
    }

    /**
    "Get store inventory" method
     */
    public static CustomResponse<Map<String, Integer>> getStoreInventory() {
        Response response = RestAssured.given()
                .baseUri(baseURL)
                .get("/store/inventory");

        @SuppressWarnings("unchecked")
        Map<String, Integer> inventory = (Map<String, Integer>)response.as(Map.class);

        return CustomResponse.<Map<String, Integer>>builder()
                .statusCode(response.getStatusCode())
                .message(response.getStatusLine())
                .data(inventory)
                .build();

        //todo: read about LOG().ALL() and implement it in Service methods (where needed)
        //      refactor methods according to refactored CustomResponse
    }

    /**
    "Place order for a pet" method
     */
    public static CustomResponse<OrderDTO> placeOrderForPet(long petId, int quantity) {
        long orderId = TestValueGenerator.randomId();

        OrderDTO orderDTO = OrderDTO.builder()
                .id(orderId)
                .petId(petId)
                .quantity(quantity)
                .shipDate(Instant.now().toString())
                .status("placed")
                .complete(true)
                .build();

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(orderDTO)
                .post("/store/order");

        return CustomResponse.fromRestAssuredResponse(response, OrderDTO.class);
    }

    /**
    "Get order by ID" method
     */
    public static CustomResponse<OrderDTO> getOrderById(long orderId) {
        Response response = RestAssured.given()
                .get("/store/order/" + orderId);

        return CustomResponse.fromRestAssuredResponse(response, OrderDTO.class);
    }

    /**
    "Delete order by ID" method
     */
    public static CustomResponse<Void> deleteOrderById(long orderId) {
        Response response = RestAssured.given()
                .delete("/store/order/" + orderId);

        return CustomResponse.fromRestAssuredResponse(response, Void.class);
    }
//    public CustomResponse<Void><Void>Entity<String> createUser(UserDTO userDTO) {
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
