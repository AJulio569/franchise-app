package co.com.franchise.api.utils;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCodeEnum {
    INVALID_ERROR("INVALID_ERROR", "Bad Request","Validation error", HttpStatus.BAD_REQUEST),
    NOT_FOUND("NOT_FOUND", "Not Found","Not Found", HttpStatus.NOT_FOUND),
    ALREADY_EXISTS("ALREADY_EXISTS", "Conflict","Conflict", HttpStatus.CONFLICT),
    DATABASE_ERROR("DATABASE_ERROR", "Internal Database Error","Database error", HttpStatus.SERVICE_UNAVAILABLE),
    INTERNAL_ERROR("INTERNAL_ERROR", "Internal Server Error","Unexpected server error", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String error;
    private final String message;
    private final HttpStatus httpStatus;

    ErrorCodeEnum(String code, String error, String message, HttpStatus httpStatus) {
        this.code = code;
        this.error = error;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
