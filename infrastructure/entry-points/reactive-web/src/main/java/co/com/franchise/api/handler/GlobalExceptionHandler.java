package co.com.franchise.api.handler;

import co.com.franchise.api.dto.response.ErrorResponse;
import co.com.franchise.api.exception.CustomValidationException;
import co.com.franchise.api.utils.FieldErrorDetail;
import co.com.franchise.usecase.franchise.exception.EntityAlreadyExistsException;
import co.com.franchise.usecase.franchise.exception.EntityNotFoundException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Order(-2)
@Slf4j
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {
    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String error = "Internal Server Error";
        String message = "An unexpected error occurred";
        List<FieldErrorDetail> fieldErrors = new ArrayList<>();

        if (ex instanceof WebExchangeBindException bindException) {
            status = HttpStatus.BAD_REQUEST;
            error = "Bad Request";
            message = "Validation error";
            fieldErrors = bindException.getFieldErrors().stream()
                    .map(fieldError -> new FieldErrorDetail(
                            fieldError.getField(),
                            fieldError.getDefaultMessage()))
                    .collect(Collectors.toList());

        } else if (ex instanceof CustomValidationException customEx) {
            status = HttpStatus.BAD_REQUEST;
            error = "Bad Request";
            message ="Validation error";
            fieldErrors = customEx.getFieldErrors();

        }else if (ex instanceof EntityNotFoundException entityNotFoundException) {
            status = HttpStatus.NOT_FOUND;
            error = "Not Found";
            message = entityNotFoundException.getMessage();

        }else if (ex instanceof EntityAlreadyExistsException entityAlreadyExistsException) {
            status = HttpStatus.CONFLICT;
            error = "Conflict";
            message = entityAlreadyExistsException.getMessage();

        }else if (ex instanceof org.springframework.web.server.ServerWebInputException inputEx) {
            Throwable cause = inputEx.getCause();
            if (cause instanceof com.fasterxml.jackson.databind.exc.MismatchedInputException mismatchedInputEx) {
                String field = mismatchedInputEx.getPath().stream()
                        .map(JsonMappingException.Reference::getFieldName)
                        .filter(Objects::nonNull)
                        .reduce((first, second) -> second)
                        .orElse("body");

                status = HttpStatus.BAD_REQUEST;
                error = "Bad Request";
                message = String.format(
                        "Format error in field '%s'. Expected type '%s', but received an invalid value.",
                        field,
                        mismatchedInputEx.getTargetType().getSimpleName()
                );

                fieldErrors = List.of(new FieldErrorDetail(field, String.format(
                        "The provided value is not of the expected type (%s).",
                        mismatchedInputEx.getTargetType().getSimpleName()
                )));

            } else if (cause != null) {
                status = HttpStatus.BAD_REQUEST;
                error = "Bad Request";
                message = "Request format error. Ensure that the data is of the correct type.";
                fieldErrors = List.of(new FieldErrorDetail("body", cause.getMessage()));
            }
        }

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(status.value())
                .error(error)
                .message(message)
                .timestamp(LocalDateTime.now())
                .path(exchange.getRequest().getPath().value())
                .fieldErrors((fieldErrors != null && !fieldErrors.isEmpty()) ? fieldErrors : new ArrayList<>())
                .build();

        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        try {
            byte[] bytes = objectMapper.writeValueAsBytes(errorResponse);
            return exchange.getResponse().writeWith(Mono.just(exchange.getResponse()
                    .bufferFactory().wrap(bytes)));

        } catch (Exception e) {
            byte[] fallback = "{\"error\": \"Error serializing the response\"}".getBytes(StandardCharsets.UTF_8);
            return exchange.getResponse().writeWith(Mono.just(exchange.getResponse()
                    .bufferFactory().wrap(fallback)));
        }
    }
}
