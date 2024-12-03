package services;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.http.ContentType;

import lombok.Setter;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
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

    static String baseURL = ConfProperties.getProperty("base_URL");
    @Setter
    private static String authToken = null;
    private static final RestTemplate restTemplate = new RestTemplate();

    /**
    "Add a pet" method
     */
    public static CustomResponse<PetDTO> addPet(PetDTO petDTO) {
        Response response = RestAssured.given()
                .log().headers()
                .log().body()
                .contentType(ContentType.JSON)
                .body(petDTO)
                .post("/pet")
                .then()
                .log().status()
                .extract().response();

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
                .log().headers()
                .log().body()
                .contentType(ContentType.MULTIPART)
                .multiPart("file", tempFile.toFile())
                .post("/pet/" + petId + "/uploadImage")
                .then()
                .log().status()
                .extract().response();


        // delete the temporary file after upload
        Files.delete(tempFile);

        return CustomResponse.fromRestAssuredResponse(response, Void.class);
    }

    /**
    "Upload non-image file to pet" method
     */
    public static CustomResponse<Void> uploadNonImageFileToPet(long petId, File nonImageFile) {
        Response response = RestAssured.given()
                .log().headers()
                .log().body()
                .contentType("multipart/form-data")
                .multiPart("file", nonImageFile)
                .post("/pet/" + petId + "/uploadImage")
                .then()
                .log().status()
                .extract().response();

        return CustomResponse.fromRestAssuredResponse(response, Void.class);
    }

    /**
    "Delete pet by ID" method
     */
    public static CustomResponse<Void> deletePet(long petID) {
        Response response = RestAssured.given()
                .log().headers()
                .delete("/pet/" + petID)
                .then()
                .log().status()
                .extract().response();

        return CustomResponse.fromRestAssuredResponse(response, Void.class);
    }

    /**
    "Get pet by ID" method
     */
    public static CustomResponse<PetDTO> getPetByID(long petID) {
        Response response = RestAssured.given()
                .log().headers()
                .get("/pet/" + petID)
                .then()
                .log().status()
                .extract().response();

        return CustomResponse.fromRestAssuredResponse(response, PetDTO.class);
    }

    /**
    "Update pet using form data" method
     */
    public static CustomResponse<Void> updatePetWithFormData(long petID, String petName, String petStatus) {
        Response response = RestAssured.given()
                .log().headers()
                .log().body()
                .contentType(ContentType.URLENC)
                .formParam("name", petName)
                .formParam("status", petStatus)
                .post("/pet/" + petID)
                .then()
                .log().status()
                .extract().response();

        return CustomResponse.fromRestAssuredResponse(response, Void.class);
    }

    /**
    "Find pet by status" method
     */
    public static CustomResponse<List<PetDTO>> findPetsByStatus(String status) {
        Response response = RestAssured.given()
                .log().headers()
                .queryParam("status", status)
                .get("/pet/findByStatus")
                .then()
                .log().status()
                .extract().response();

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
                .log().headers()
                .log().body()
                .contentType(ContentType.JSON)
                .body(orderDTO)
                .post("/store/order")
                .then()
                .log().status()
                .extract().response();

        return CustomResponse.fromRestAssuredResponse(response, OrderDTO.class);
    }

    /**
    "Get order by ID" method
     */
    public static CustomResponse<OrderDTO> getOrderById(long orderId) {
        Response response = RestAssured.given()
                .log().headers()
                .get("/store/order/" + orderId)
                .then()
                .log().status()
                .extract().response();

        return CustomResponse.fromRestAssuredResponse(response, OrderDTO.class);
    }

    /**
    "Delete order by ID" method
     */
    public static CustomResponse<Void> deleteOrderById(long orderId) {
        Response response = RestAssured.given()
                .log().headers()
                .delete("/store/order/" + orderId)
                .then()
                .log().status()
                .extract().response();

        return CustomResponse.fromRestAssuredResponse(response, Void.class);
    }

    /**
     "Get user by username" method
     */
    public static CustomResponse<UserDTO> getUserByUsername(String username) {
        String url = baseURL + "/user/" + username;

        try {
            UserDTO userDTO = restTemplate.getForObject(url, UserDTO.class);
            return CustomResponse.<UserDTO>builder()
                    .statusCode(200)
                    .message("User retrieved successfully")
                    .data(userDTO)
                    .build();
        } catch (Exception ex) {
            return CustomResponse.<UserDTO>builder()
                    .statusCode(404)
                    .message("User not found: " + ex.getMessage())
                    .data(null)
                    .build();
        }
    }

    public static CustomResponse<UserDTO> createUser(UserDTO userDTO) {
        String url = baseURL + "/user";

        try {
            ResponseEntity<Void> responseEntity = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    new HttpEntity<>(userDTO),
                    Void.class
            );
            return CustomResponse.<UserDTO>builder()
                    .statusCode(responseEntity.getStatusCode().value())
                    .message("User created successfully.")
                    .data(null)
                    .build();
        } catch (HttpStatusCodeException ex) {
            return CustomResponse.<UserDTO>builder()
                    .statusCode(ex.getStatusCode().value())
                    .message("Failed to create user: " + ex.getMessage())
                    .data(null)
                    .build();
        }
    }

    public static CustomResponse<String> loginUser(String username, String password) {
        String url = baseURL + "/user/login?username={username}&password={password}";

        try {
            // Sending GET request for login
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class, username, password);

            // Extracting token or successful message
            HttpHeaders headers = responseEntity.getHeaders();
            String token = headers.getFirst("Authorization");
            String body = responseEntity.getBody();

            return CustomResponse.<String>builder()
                    .statusCode(responseEntity.getStatusCode().value())
                    .message(body != null ? body : "Login successful")
                    .data(token)
                    .build();
        } catch (HttpClientErrorException.BadRequest ex) {
            return CustomResponse.<String>builder()
                    .statusCode(400)
                    .message("Invalid username/password supplied: " + ex.getMessage())
                    .data(null)
                    .build();
        } catch (Exception ex) {
            return CustomResponse.<String>builder()
                    .statusCode(500)
                    .message("An unexpected error occurred: " + ex.getMessage())
                    .data(null)
                    .build();
        }
    }

    public static CustomResponse<Void> logoutUser() {
        String url = baseURL + "/user/logout";

        try {
            // Perform the GET request for logout
            restTemplate.exchange(url, HttpMethod.GET, null, Void.class);

            return CustomResponse.<Void>builder()
                    .statusCode(200)
                    .message("User logged out successfully.")
                    .build();
        } catch (HttpClientErrorException ex) {
            return CustomResponse.<Void>builder()
                    .statusCode(ex.getStatusCode().value())
                    .message("Failed to log out: " + ex.getMessage())
                    .build();
        } catch (Exception ex) {
            return CustomResponse.<Void>builder()
                    .statusCode(500)
                    .message("An unexpected error occurred: " + ex.getMessage())
                    .build();
        }
    }



    public static CustomResponse<Void> deleteUser(String username) {
        String url = baseURL + "/user/" + username;

        try {
            HttpHeaders headers = createAuthHeaders();
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            // Perform the DELETE request
            restTemplate.exchange(url, HttpMethod.DELETE, entity, Void.class);

            return CustomResponse.<Void>builder()
                    .statusCode(200)
                    .message("User deleted.")
                    .build();
        } catch (HttpClientErrorException.BadRequest ex) {
            return CustomResponse.<Void>builder()
                    .statusCode(400)
                    .message("Invalid username supplied.")
                    .build();
        } catch (HttpClientErrorException.NotFound ex) {
            return CustomResponse.<Void>builder()
                    .statusCode(404)
                    .message("User not found.")
                    .build();
        } catch (Exception ex) {
            return CustomResponse.<Void>builder()
                    .statusCode(500)
                    .message("An unexpected error occurred: " + ex.getMessage())
                    .build();
        }
    }


    public static CustomResponse<UserDTO> updateUser(String username, UserDTO updatedUser) {
        String url = baseURL + "/user/{username}";
        HttpHeaders headers = createAuthHeaders();
        HttpEntity<UserDTO> entity = new HttpEntity<>(updatedUser, headers);

        try {
            restTemplate.exchange(url, HttpMethod.PUT, entity, Void.class, username);

            return CustomResponse.<UserDTO>builder()
                    .statusCode(200)
                    .message("User updated successfully.")
                    .data(updatedUser)
                    .build();
        } catch (HttpClientErrorException.BadRequest ex) {
            return CustomResponse.<UserDTO>builder()
                    .statusCode(400)
                    .message("Invalid user supplied: " + ex.getMessage())
                    .data(null)
                    .build();
        } catch (HttpClientErrorException.NotFound ex) {
            return CustomResponse.<UserDTO>builder()
                    .statusCode(404)
                    .message("User not found: " + ex.getMessage())
                    .data(null)
                    .build();
        } catch (Exception ex) {
            return CustomResponse.<UserDTO>builder()
                    .statusCode(500)
                    .message("An unexpected error occurred: " + ex.getMessage())
                    .data(null)
                    .build();
        }
    }

    private static HttpHeaders createAuthHeaders() {
        HttpHeaders headers = new HttpHeaders();
        if (authToken != null) {
            headers.set("Authorization", "Bearer " + authToken);
        }
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

}

