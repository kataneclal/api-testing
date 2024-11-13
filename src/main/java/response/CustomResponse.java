package response;

import io.restassured.response.Response;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CustomResponse<T> {
    private int statusCode; // HTTP status code (e.g., 200, 404)
    private String message; // Any response message from the API
    private T data; // The actual data returned (generic)

    /**
     * Static factory method to create a CustomResponse from a RestAssured Response.
     * This method extracts the status code, status line (message), and deserializes the body into the specified data
     * type.
     *
     * @param restAssuredResponse The RestAssured response object
     * @param dataClass           The class type of the data to be deserialized
     * @param <T>                 The type of data in the response
     * @return A new CustomResponse object with extracted data
     */
    public static <T> CustomResponse<T> fromRestAssuredResponse(@NonNull Response restAssuredResponse,
                                                                Class<T> dataClass) {
        int statusCode = restAssuredResponse.getStatusCode();
        String message = restAssuredResponse.getStatusLine();

        T data = null;
        if (statusCode == 200) {
            // Parse response data as the specified DTO class
            data = restAssuredResponse.as(dataClass);
        }

        return CustomResponse.<T>builder()
                .statusCode(statusCode)
                .message(message)
                .data(data)
                .build();
    }

    // todo: add JSON schema validation

}
