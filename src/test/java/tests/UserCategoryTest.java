package tests;


import config.TestRunConfig;
import dto.UserDTO;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.qameta.allure.junit5.AllureJunit5;
import net.datafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import response.CustomResponse;
import services.APIService;
import org.junit.jupiter.api.Test;
import utils.TestValueGenerator;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * This test class contains all the test cases
 * for user category of Petstore Sample API
 * <p>
 * Link:  
 * <a href="https://docs.google.com/spreadsheets/d/1Aa9B6OfLG3Hz6BBSKFhrd6rHJu1l7ONiVWqeCPAVuuA/edit?usp=sharing">Test cases sheet</a>
 */
@ExtendWith(AllureJunit5.class)
@Epic("Petstore API Tests")
@Feature("User category")
public class UserCategoryTest extends TestRunConfig {

    Faker faker = new Faker();

    private final long TEST_USER_ID = TestValueGenerator.randomId();
    private final String TEST_USERNAME = faker.internet().username();
    private final String TEST_FIRST_NAME = faker.name().firstName();
    private final String TEST_LAST_NAME = faker.name().lastName();
    private final String TEST_EMAIL = faker.internet().emailAddress();
    private final String TEST_PHONE = faker.phoneNumber().phoneNumber();
    private final String TEST_PASSWORD = faker.internet().password();
    private final int testUserStatus = TestValueGenerator.randomInt(1, 3);
    private boolean isUserDeleted = false;

    private static UserDTO userDTO;

    @BeforeEach
    @Step("Setup for each test - creating a user")
    public void setUp() {
        // create a user
        userDTO = UserDTO.builder()
                .id(TEST_USER_ID)
                .username(TEST_USERNAME)
                .firstName(TEST_FIRST_NAME)
                .lastName(TEST_LAST_NAME)
                .email(TEST_EMAIL)
                .phone(TEST_PHONE)
                .password(TEST_PASSWORD)
                .userStatus(testUserStatus)
                .build();

        APIService.createUser(userDTO);

        // log in with created user's details

    }

    @AfterEach
    @Step("Cleanup for each test - deleting the user")
    public void cleanUp() {
        if (!isUserDeleted) {
            CustomResponse<Void> deleteResponse = APIService.deleteUser(TEST_USERNAME); // ЗДЕСЬ ОШИБКА.
            assertThat(200, is(equalTo(deleteResponse.getStatusCode())));
            isUserDeleted = true;
        }
    }

    @Test
    public void testCreateUserValid() {
        UserDTO validUserDTO = UserDTO.builder()
                .id(TestValueGenerator.randomId())
                .username(faker.internet().username())
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .email(faker.internet().emailAddress())
                .password(faker.internet().password())
                .phone(faker.phoneNumber().phoneNumber())
                .build();

        CustomResponse<UserDTO> response = APIService.createUser(validUserDTO);
        assertThat(200, is(equalTo(response.getStatusCode())));
    }

    @Test
    public void testGetUserByUsername() {
        CustomResponse<UserDTO> getResponse = APIService.getUserByUsername(TEST_USERNAME);
        assertThat(200, is(equalTo(getResponse.getStatusCode())));
        assertThat(getResponse.getData().getUsername(), is(equalTo(TEST_USERNAME)));
    }

    @Test
    void testUpdateUser() {
        // user log in
        CustomResponse<String> loginResponse = APIService.loginUser(userDTO.getUsername(), userDTO.getPassword());
        assertThat(loginResponse.getStatusCode(), is(equalTo(200)));
        APIService.setAuthToken(loginResponse.getData());

        UserDTO updatedUserDTO = UserDTO.builder()
                .id(TestValueGenerator.randomId())
                .username(faker.internet().username())
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .email(faker.internet().emailAddress())
                .password(TestValueGenerator.randomString(8))
                .phone(faker.phoneNumber().phoneNumber())
                .build();

        CustomResponse<UserDTO> updateResponse = APIService.updateUser(userDTO.getUsername(), updatedUserDTO);
        assertThat(updateResponse.getStatusCode(), is(equalTo(200)));

    }

    @Test
    public void testUpdateUserSpecificFields() {
        // user log in
        CustomResponse<String> loginResponse = APIService.loginUser(userDTO.getUsername(), userDTO.getPassword());
        assertThat(loginResponse.getStatusCode(), is(equalTo(200)));
        APIService.setAuthToken(loginResponse.getData());

        UserDTO updatedUserDTO = userDTO.toBuilder()
                .firstName(faker.name().firstName())
                .password(TestValueGenerator.randomString(8))
                .build();

        CustomResponse<UserDTO> updateResponse = APIService.updateUser(userDTO.getUsername(), updatedUserDTO);

        assertThat(updateResponse.getStatusCode(), is(equalTo(200)));
        assertThat(updateResponse.getMessage(), is(equalTo("User updated successfully.")));
        assertNotNull(updateResponse.getData());

        // Ensure that only the updated fields are changed
        assertThat(updateResponse.getData().getFirstName(), is(equalTo(updatedUserDTO.getFirstName())));
        assertThat(updateResponse.getData().getPassword(), is(equalTo(updatedUserDTO.getPassword())));

        // Validate that other fields remain unchanged
        assertThat(updateResponse.getData().getEmail(), is(equalTo(userDTO.getEmail())));
        assertThat(updateResponse.getData().getUsername(), is(equalTo(userDTO.getUsername())));

    }

    @Test
    public void testLogIn() {
        // user log in
        CustomResponse<String> loginResponse = APIService.loginUser(userDTO.getUsername(), userDTO.getPassword());
        assertThat(loginResponse.getStatusCode(), is(equalTo(200)));
        APIService.setAuthToken(loginResponse.getData());

        // Validate the response
        
    }

    @Test
    @Step("Test logging out a user")
    public void testLogoutUser() {
        // Call the logout API
        CustomResponse<Void> logoutResponse = APIService.logoutUser();

        // Validate the response
        assertThat(logoutResponse.getStatusCode(), is(equalTo(200)));
        assertThat(logoutResponse.getMessage(), is(equalTo("User logged out successfully.")));
    }

    @Test
    public void deleteUser() {
        // user log in

        CustomResponse<Void> deleteResponse = APIService.deleteUser(TEST_USERNAME);
        assertThat(200, is(equalTo(deleteResponse.getStatusCode())));

        CustomResponse<UserDTO> getResponse = APIService.getUserByUsername(TEST_USERNAME);
        assertThat(404, is(equalTo(getResponse.getStatusCode())));
        isUserDeleted = true;
    }



    //todo: add test cases
}
