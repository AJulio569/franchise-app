package co.com.franchise.api.exception;

import co.com.franchise.api.utils.FieldErrorDetail;
import lombok.Getter;
import java.util.List;

@Getter
public class CustomValidationException extends RuntimeException {
    private final List<FieldErrorDetail> fieldErrors;

    public CustomValidationException(List<FieldErrorDetail> fieldErrors) {
        super("Validation error");
        this.fieldErrors = fieldErrors;
    }
}
