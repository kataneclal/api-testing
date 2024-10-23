package tests;

import config.TestValues;
import dto.OrderDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import response.CustomResponse;
import services.APIService;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * This test class contains all the test cases
 * for store category of Petstore Sample API
 *
 * Link:  <a href="https://docs.google.com/spreadsheets/d/1Aa9B6OfLG3Hz6BBSKFhrd6rHJu1l7ONiVWqeCPAVuuA/edit?usp=sharing">Test cases sheet</a>
 */

public class StoreCategoryTest extends TestRunConfig {

    private OrderDTO orderDTO;
    private boolean isOrderDeleted = false;

    /*
     Test case #8: Verify that the pet inventory is returned by status
     */
    @Test
    public void testGetStoreInventory() {
        CustomResponse<Map<String, Integer>> response = APIService.getStoreInventory();

        // verify that the response body is not empty
        assertNotNull(response.getData(), "The response should not be null.");

        // status counts (e.g., available, pending, sold) presence check
        Integer availableCount = response.getData().get("available");
        Integer pendingCount = response.getData().get("pending");
        Integer soldCount = response.getData().get("sold");

        // assertions to ensure counts are correct (or not null)
        assertNotNull(availableCount, "The available count should not be null.");
        assertNotNull(pendingCount, "The pending count should not be null.");
        assertNotNull(soldCount, "The sold count should not be null.");
    }

    /*
     Test case #9: Verify "POST Place an order for a pet" API Endpoint
     */
    @Test
    public void testPlaceOrderForPet() {
        // generate a random petId and quantity
        long petId = TestValues.petID;
        int quantity = TestValues.quantity;

        // place an order and verify the response status code
        CustomResponse<OrderDTO> orderResponse = APIService.placeOrderForPet(petId, quantity);
        assertThat(200, is(equalTo(orderResponse.getStatusCode())));

        // verify that the order details are all set
        assertThat(petId, is(equalTo(orderResponse.getData().getPetId())));
        assertThat(quantity, is(equalTo(orderResponse.getData().getQuantity())));
    }

    @Nested
    class OrderTests {
        private long orderID;
        private final long petID = TestValues.petID;
        private final int quantity = TestValues.quantity;

        @BeforeEach
        public void setUp() {
            CustomResponse<OrderDTO> orderResponse = APIService.placeOrderForPet(petID, quantity);
            assertThat(200, is(equalTo(orderResponse.getStatusCode())));

            orderID = orderResponse.getData().getId(); // Fetch the ID from the response
        }

        @AfterEach
        public void cleanUp() {
            if (!isOrderDeleted) {
                APIService.deleteOrderById(orderID);
                isOrderDeleted = true;
            }
        }

        /*
         Test case #10: Verify "GET /store/order/{orderId} Find purchase order by ID" API Endpoint
         */
        @Test
        public void testGetOrderByID() {
            // get the order and verify response status
            CustomResponse<OrderDTO> getResponse = APIService.getOrderById(orderID);
            assertThat(200, is(equalTo(getResponse.getStatusCode())));

            // also verify order details
            assertThat(orderID, is(equalTo(getResponse.getData().getId())));
            assertThat(petID, is(equalTo(getResponse.getData().getPetId())));
        }

        /*
         Test case #11: Verify "DELETE /store/order/{orderId} Delete purchase order by ID" API Endpoint
         */
        @Test
        public void testDeleteOrderByID() {
            // Delete the order using the utility method
            CustomResponse<Void> deleteResponse = APIService.deleteOrderById(orderID);
            assertThat(200, is(equalTo(deleteResponse.getStatusCode())));
            isOrderDeleted = true;  // flag indicating that order is deleted

            // Verify that the order no longer exists
            CustomResponse<OrderDTO> getDeletedOrderResponse = APIService.getOrderById(orderID);
            assertThat(404, is(equalTo(getDeletedOrderResponse.getStatusCode())));
        }
    }

    // todo: de-nest the @Nested code
    //       add more test cases
}
