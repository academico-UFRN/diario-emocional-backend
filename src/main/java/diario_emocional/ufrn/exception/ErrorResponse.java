package diario_emocional.ufrn.exception;

import java.time.LocalDateTime;

public class ErrorResponse {
    final private LocalDateTime timestamp;
    final private int status;
    final private String error;
    final private String message;

    public ErrorResponse(int status, String error, String message) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
        this.message = message;
    }

    // Getters e Setters
    public LocalDateTime getTimestamp() { return timestamp; }
    public int getStatus() { return status; }
    public String getError() { return error; }
    public String getMessage() { return message; }
}
