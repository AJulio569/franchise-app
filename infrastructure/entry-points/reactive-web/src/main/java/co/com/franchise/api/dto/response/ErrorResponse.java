package co.com.franchise.api.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import co.com.franchise.api.utils.FieldErrorDetail;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ErrorResponse {
    private int status;
    private String error;
    private String message;
    private LocalDateTime timestamp;
    private String path;
    private List<FieldErrorDetail> fieldErrors;

    public ErrorResponse(int status, String error, String message, LocalDateTime timestamp, String path, List<FieldErrorDetail> fieldErrors) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.timestamp = timestamp;
        this.path = path;
        this.fieldErrors = fieldErrors;
    }

    public ErrorResponse() {
    }
}
