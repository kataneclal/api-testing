package tests;

import config.TestRunConfig;
import dto.PetDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

import response.CustomResponse;
import services.APIService;
import utils.ConfProperties;
import config.TestConstants;
import utils.TestValueGenerator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <p>This test class contains all the test cases
 * for pet category of Petstore Sample API</p>
 *
 * Link:  <a href="https://docs.google.com/spreadsheets/d/1Aa9B6OfLG3Hz6BBSKFhrd6rHJu1l7ONiVWqeCPAVuuA/edit?usp=sharing">Test cases Google sheet</a>
 */

public class PetCategoryTest extends TestRunConfig {

    private final long TEST_PET_ID = TestValueGenerator.randomId();
    private static final String TEST_PET_NAME = TestConstants.PET_NAME;
    private static final String TEST_PET_STATUS = TestValueGenerator.randomPetStatus();
    private boolean isPetDeleted = false;

    @BeforeEach
    public void setUp() {
        // initializing petDTO
        PetDTO petDTO = PetDTO.builder()
                .id(TEST_PET_ID)
                .name(TEST_PET_NAME)
                .status(TEST_PET_STATUS)
                .build();

        // add a pet
        CustomResponse<PetDTO> addResponse = APIService.addPet(petDTO);
        assertThat(200, is(equalTo(addResponse.getStatusCode())));
        isPetDeleted = false;
    }

    @AfterEach
    public void cleanUp() {
        if (!isPetDeleted) {
            CustomResponse<Void> deleteResponse = APIService.deletePet(TEST_PET_ID);
            assertThat(200, is(equalTo(deleteResponse.getStatusCode())));
            isPetDeleted = true;
        }
    }

    /**
     * Test case #1: Verify "POST - Add a New Pet to the Store" API endpoint
     */
    @Test
    public void testAddPet() {
        CustomResponse<PetDTO> getResponse = APIService.getPetByID(TEST_PET_ID);

        // verify that the pet was added successfully
        assertThat(TEST_PET_NAME, is(equalTo(getResponse.getData().getName())));
    }

    /**
     * Test case #2: Upload an image of the pet
     */
    @Test
    public void testUploadPetImageFromURL() throws Exception {
        // Use a sample image URL
        String imageUrl = ConfProperties.getProperty("sampleImage_URL");

        // Call the utility method to upload the image from the URL
        CustomResponse<Void> uploadResponse = APIService.uploadPetImageFromURL(TEST_PET_ID, imageUrl);

        // Verify the status code is 200 OK
        assertThat(200, is(equalTo(uploadResponse.getStatusCode())));
    }

    /**
     * Test case #3: (Negative) Upload a non-image file to the image form field of the pet
     */
    @Test
    public void testUploadNonImageFileToPet() throws IOException {
        // create a non-image file (e.g., a .txt file)
        String nonImageFilePath = "src/test/java/resources/non-image-file.txt";
        File nonImageFile = new File(nonImageFilePath);

        // if the file doesn't exist, create it
        if (!nonImageFile.exists()) {
            try (FileOutputStream outputStream = new FileOutputStream(nonImageFile)) {
                String fileContent = "This is a test file, not an image.";
                outputStream.write(fileContent.getBytes());
            }
        }

        // upload the non-image file
        CustomResponse<Void> response = APIService.uploadNonImageFileToPet(TEST_PET_ID, nonImageFile);
        assertThat(400, is(equalTo(response.getStatusCode())));
    }

    /**
     * Test case #4: Verify "GET Find Pet by ID" API endpoint
     */
    @Test
    public void testGetPetByID() {
        CustomResponse<PetDTO> getResponse = APIService.getPetByID(TEST_PET_ID);

        // verify that we got the right ID
        assertThat(TEST_PET_NAME, is(equalTo(getResponse.getData().getName())));
    }

    /**
     * Test case #5: Verify "DELETE Delete a Pet" API endpoint
     */
    @Test
    public void testDeletePet() {
        CustomResponse<Void> deleteResponse = APIService.deletePet(TEST_PET_ID);
        isPetDeleted = true; // Mark that the pet is deleted

        // verify that the delete response status code is 200 (success)
        assertThat(200, is(equalTo(deleteResponse.getStatusCode())));

        // verify that the pet no longer exists
        CustomResponse<Void> getResponse = APIService.getPetWithoutStatusCheck(TEST_PET_ID);
        assertThat(404, is(equalTo(getResponse.getStatusCode())));
    }

    /**
     * Test case #6: Verify "POST Update a pet with form data" API endpoint
     */
    @Test
    public void testUpdatePetWithFormData() {
        // form data fields
        String updatedName = "Paul";
        String updatedStatus = "sold";

        CustomResponse<Void> updateResponse = APIService.updatePetWithFormData(TEST_PET_ID, updatedName, updatedStatus);
        assertThat(200, is(equalTo(updateResponse.getStatusCode())));

        // verify that the pet's name and status were updated
        CustomResponse<PetDTO> getResponse = APIService.getPetByID(TEST_PET_ID);
        assertThat(updatedName, is(equalTo(getResponse.getData().getName())));
        assertThat(updatedStatus, is(equalTo(getResponse.getData().getStatus())));
    }

    /**
     * Test case #7: Verify "GET Find Pets by Status" API endpoint works for different pet status values
     */
    @Test
    public void testFindPetsByStatus() {
        // creating pets
        APIService.addPet(PetDTO.builder().id(1L).name("Issu").status("available").build()); // available pet added
        APIService.addPet(PetDTO.builder().id(2L).name("Vory").status("pending").build());   // pending pet added
        APIService.addPet(PetDTO.builder().id(3L).name("Garry").status("sold").build());     // sold pet added

        // put all status values in a list
        List<String> statusList = Arrays.asList("available", "pending", "sold");

        // find pets by status
        for (String status : statusList) {
            CustomResponse<List<PetDTO>> response = APIService.findPetsByStatus(status);
            assertFalse(response.getData().isEmpty(), "No pets found with status: " + status);
        }

        // delete pets (cleanup)
        APIService.deletePet(1L);
        APIService.deletePet(2L);
        APIService.deletePet(3L);
    }

    //todo: add more test cases (also negative ones) of methods where possible
}
