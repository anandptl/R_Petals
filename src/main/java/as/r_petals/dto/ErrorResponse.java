package as.r_petals.dto;

import java.time.LocalDateTime;

public class ErrorResponse {

    private boolean success;
    private String message;
    private String path;
    private LocalDateTime timestamp;


    public ErrorResponse() {
    }


    public ErrorResponse(
            boolean success,
            String message,
            String path,
            LocalDateTime timestamp) {

        this.success = success;
        this.message = message;
        this.path = path;
        this.timestamp = timestamp;
    }


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}