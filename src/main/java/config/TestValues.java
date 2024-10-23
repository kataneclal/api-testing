package config;

import java.util.Random;

public class TestValues {

    /**
     * Here are all the constants used in the tests
     * (e.g. Base_URL, test pet details, order categories, user details)
     */

    /*
    Base URL for the API
     */
    public static final String BASE_URL = "https://petstore.swagger.io/v2";

    /*
     Test pet details
     */
    public static long petID = new Random().nextLong(1, 999);
    public static final String PET_NAME = "Yalee";
    public static final String PET_STATUS = "available";
//    public static final String randomPetStatus = ;

    /*
     Test order details
     */
//    public static final Long petId = new Random().nextLong(1, 1000);
    public static final int quantity = new Random().nextInt(1, 10);

    /*
     User details (TBD)
     */

}
