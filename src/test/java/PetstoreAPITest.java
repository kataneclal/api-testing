import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PetstoreAPITest {

    // An instance of the utility class
    PetstoreAPIUtils apiUtils = new PetstoreAPIUtils();

    Random randomNum = new Random();

    /**
     * Test case #1:  Add a new pet
     */
    @Test
    public void testAddPet() {
        // Given: The data of the new pet
        int petID = randomNum.nextInt(9999);
        String petName =   "Boris";
        String petStatus = "available";

        // When: We make a POST request to add a new pet
        Response addResponse = apiUtils.addPet(petID, petName, petStatus);

        // Then: Verify that the pet was added successfully
        String addedPetName = addResponse.jsonPath().getString("name");
        assertEquals(petName, addedPetName, "The pet name should match the added one");

        // Deleting the pet
        Response deleteResponse = apiUtils.deletePet(petID);
        String deleteMessage = deleteResponse.jsonPath().getString("message");
        assertEquals(String.valueOf(petID), deleteMessage, "The deleted pet ID should match,");
    }

    /**
     * Test case #2: Get details of a pet by ID
     */
    @Test
    public void testGetPetByID() {
        // Step 1: Add a pet first so we have a pet to retrieve
        int petID = randomNum.nextInt(1, 9999);
        String petName =   "Sally";
        String petStatus = "available";

        apiUtils.addPet(petID, petName, petStatus); // Adding a pet using the utility function

        // When: Making a GET request to fetch the pet details
        Response getResponse = apiUtils.getPetByID(petID);

        // Then: Verification of pet details
        String retrievedPetName = getResponse.jsonPath().getString("name");
        assertEquals(petName, retrievedPetName, "The added pet name should match the retrieved one.");

        // Delete the pet
        apiUtils.deletePet(petID);
    }

    /**
     * Test case #3: Delete a pet
     */
    @Test
    public void testDeletePet() {
        // Given: A freshly new created pet
        int petID = randomNum.nextInt(1, 9999);
        String petName =   "Yahmar";
        String petStatus = "available";

        apiUtils.addPet(petID, petName, petStatus);

        // When: We make a DELETE request to delete the pet
        Response deleteResponse = apiUtils.deletePet(petID);

        // Then: Verify that the pet was deleted successfully
        String deleteMessage = deleteResponse.jsonPath().getString("message");
        assertEquals(String.valueOf(petID), deleteMessage, "The deleted pet ID should match.");
    }

    /**
     * Test case #4: Update a pet in the store with form data
     */
    @Test
    public void testUpdatePetWithFormData() {
        // Given: A pet ID to update and form data fields
        int petID = randomNum.nextInt(1, 9999);
        String petName =   "Yalee";
        String petStatus = "available";

        apiUtils.addPet(petID, petName, petStatus);

        // When: We make a POST request to update the pet with form data
        String updatedName =   "Paul";
        String updatedStatus = "sold";

        Response updateResponse = apiUtils.updatePetWithFormData(petID, updatedName, updatedStatus);

        // Then: Verify that the pet was updated successfully via checking status code
        int statusCode = updateResponse.getStatusCode();
        assertEquals(200, statusCode); // Assuming this is the expected response message

        // Verify that the pet's name and status were updated
        Response getResponse = apiUtils.getPetByID(petID);
        String responseName = getResponse.jsonPath().getString("name");
        String responseStatus = getResponse.jsonPath().getString("status");
        assertEquals(updatedName, responseName, "The pet name should be updated to 'Paul'.");
        assertEquals(updatedStatus, responseStatus, "The pet status should be updated to 'sold'.");

        //Delete the pet
        apiUtils.deletePet(petID);
    }

    /**
     * Test case #5: Find pet by status
     */
    public void testFindPetsByStatus() {
        // Given: Existing pets with different status

        //Creating pets
        int petID_1 = 1;
        String petName_1 = "Issu";
        String petStatus_1 = "available";

        apiUtils.addPet(petID_1, petName_1, petStatus_1); // available pet added

        int petID_2 = 2;
        String petName_2 = "Vory";
        String petStatus_2 = "pending";

        apiUtils.addPet(petID_2, petName_2, petStatus_2); // pending pet added

        int petID_3 = 3;
        String petName_3 = "Garry";
        String petStatus_3 = "sold";

        apiUtils.addPet(petID_2, petName_2, petStatus_2); // sold pet added

        //todo: finish the test case and proceed to make test store's and user's functions

    }

}
