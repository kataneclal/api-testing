package tests;


import config.TestRunConfig;
import dto.UserDTO;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import utils.ConfProperties;
import org.junit.jupiter.api.Test;

/**
 * This test class contains all the test cases
 * for user category of Petstore Sample API
 * <p>
 * Link:  
 * <a href="https://docs.google.com/spreadsheets/d/1Aa9B6OfLG3Hz6BBSKFhrd6rHJu1l7ONiVWqeCPAVuuA/edit?usp=sharing">Test cases sheet</a>
 */

public class UserCategoryTest extends TestRunConfig {
//    @Test
//    void createUser() {
//        // Create the RestTemplate object
//        RestTemplate restTemplate = new RestTemplate();
//
//        // Define the endpoint URL for user creation
//        String url = ConfProperties.getProperty("base_URL") + "/user";
//
//        // Create an HttpEntity object with the user data and headers
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<UserDTO> requestEntity = new HttpEntity<>(userDTO, headers);
//
//        // Send POST request using RestTemplate
//        ResponseEntity<String> responseEntity = restTemplate.exchange(
//                url, HttpMethod.POST, requestEntity, String.class
//        );
//
//        // Convert response body to a JSONObject
//        JSONObject responseObject = new JSONObject(responseEntity.getBody());
//
//        // Create UserDTO from response
//        UserDTO createdUser = DTOUtils.createUserDTO(
//                responseObject.getLong("id"),
//                responseObject.getString("username"),
//                responseObject.getString("firstName"),
//                responseObject.getString("lastName"),
//                responseObject.getString("email"),
//                responseObject.getString("password"),
//                responseObject.getString("phone"),
//                responseObject.getInt("userStatus")
//        );
//
//        return new CustomResponse<>(
//                responseEntity.getStatusCodeValue(),
//                responseEntity.getStatusCode().getReasonPhrase(),
//                createdUser
//        );
//    }
    //todo: add test cases
}
