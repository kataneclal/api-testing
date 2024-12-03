package utils;

import dto.TagDTO;
import net.datafaker.Faker;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Utility class for generating random test values.
 */
public class TestValueGenerator {

    private static final Random RANDOM = new Random();
    private static final Faker faker = new Faker();
    private static final String[] PET_STATUS_VALUES = {"available", "pending", "sold"};
    private static final String[] PET_PHOTO_URLS =
            {"https://s3-media0.fl.yelpcdn.com/bphoto/pYD7JNi046dvdayh5ebVDA/258s.jpg",
             "https://images.pexels.com/photos/2071882/pexels-photo-2071882.jpeg?cs=srgb&dl=pexels-wojciech-kumpicki-1084687-2071882.jpg&fm=jpg",
             "https://i.pinimg.com/736x/cc/6c/83/cc6c83caf8f25ce60dba6b577a8bd9ab.jpg",
             "https://cdn4.volusion.store/kapts-nrbqf/v/vspfiles/photos/FRESHGOOSE20-2.jpg?v-cache=1590745950",
             "https://organicfeeds.com/wp-content/uploads/2021/03/How-To-Raise-A-Baby-Duck-scaled-1.jpg"};


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

    /**
     * Generates a list of random photo URLs.
     *
     * @param count the number of photo URLs to generate
     * @return a list of random photo URLs.
     */
    public static List<String> randomPhotoUrls(int count) {
        List<String> urls = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int index = RANDOM.nextInt(PET_PHOTO_URLS.length);
            urls.add(PET_PHOTO_URLS[index]);
        }
        return urls;
    }

    /**
     * Generates a list of random TagDTO objects.
     *
     * @param count the number of tags to generate
     * @return a list of random TagDTO objects.
     */
    public static List<TagDTO> randomTags(int count) {
        List<TagDTO> tags = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            tags.add(new TagDTO(randomId(), randomString(5)));
        }
        return tags;
    }


}
