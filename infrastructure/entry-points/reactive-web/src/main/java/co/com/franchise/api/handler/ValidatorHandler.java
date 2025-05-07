package co.com.franchise.api.handler;

import co.com.franchise.api.exception.CustomValidationException;
import co.com.franchise.api.utils.FieldErrorDetail;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ValidatorHandler {
    private final Validator validator;

    public <T> Mono<T> validate(T target) {
        Set<ConstraintViolation<T>> violations = validator.validate(target);
        if (!violations.isEmpty()) {
            List<FieldErrorDetail> fieldErrors = violations.stream()
                    .map(v -> new FieldErrorDetail(
                            v.getPropertyPath().toString(),
                            v.getMessage()))
                    .collect(Collectors.toList());
            return Mono.error(new CustomValidationException(fieldErrors));
        }
        return Mono.just(target);
    }
}
