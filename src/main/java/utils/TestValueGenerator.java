package utils;

import java.util.Random;

public class TestValueGenerator {

    private static final Random RANDOM = new Random();
    private static final String[] PET_STATUS_VALUES = {"available", "pending", "sold"};

    /**
     * Generates a random ID
     *
     * @return a random long ID.
     */
    public static long randomId() {
        return 1L + (long) (RANDOM.nextDouble() * (999L - 1L));
    }

    /**
     * Generates a random integer within a specified range.
     *
     * @param min the minimum value (inclusive)
     * @param max the maximum value (exclusive)
     * @return a random integer.
     */
    public static int randomInt(int min, int max) {
        return RANDOM.nextInt(max - min) + min;
    }

    /**
     * Generates a random string with alphanumeric characters.
     *
     * @param length the length of the string
     * @return a random string.
     */
    public static String randomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder result = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            result.append(characters.charAt(RANDOM.nextInt(characters.length())));
        }
        return result.toString();
    }

    /**
     * Generates a random pet status.
     *
     * @return a random pet status as a string.
     */
    public static String randomPetStatus() {
        int index = RANDOM.nextInt(PET_STATUS_VALUES.length);
        return PET_STATUS_VALUES[index];
    }
}
