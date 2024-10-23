package response;

public class CustomResponse<T> {
    private int statusCode; // HTTP status code (e.g., 200, 404)
    private String message; // Any response message from the API
    private T data; // The actual data returned (generic)

    public CustomResponse(int statusCode, String message, T data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    // todo: add a createCustomResponse method
}
