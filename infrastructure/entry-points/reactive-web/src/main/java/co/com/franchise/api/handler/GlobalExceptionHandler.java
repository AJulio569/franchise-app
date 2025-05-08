package co.com.franchise.api.handler;

import co.com.franchise.api.dto.response.ErrorResponse;
import co.com.franchise.api.exception.CustomValidationException;
import co.com.franchise.api.utils.FieldErrorDetail;
import co.com.franchise.usecase.franchise.exception.EntityAlreadyExistsException;
import co.com.franchise.usecase.franchise.exception.EntityNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mongodb.MongoException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import static co.com.franchise.api.utils.ErrorCodeEnum.INTERNAL_ERROR;
import static co.com.franchise.api.utils.ErrorCodeEnum.INVALID_ERROR;
import static co.com.franchise.api.utils.ErrorCodeEnum.NOT_FOUND;
import static co.com.franchise.api.utils.ErrorCodeEnum.ALREADY_EXISTS;
import static co.com.franchise.api.utils.ErrorCodeEnum.DATABASE_ERROR;


@Component
@Order(-2)
@Slf4j
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {
    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        HttpStatus status;
        ErrorResponse errorResponse = null;
        List<FieldErrorDetail> fieldErrors = new ArrayList<>();

        if (ex instanceof WebExchangeBindException bindException) {
            status = INVALID_ERROR.getHttpStatus();
            fieldErrors = bindException.getFieldErrors().stream()
                    .map(fieldError -> new FieldErrorDetail(
                            fieldError.getField(),
                            fieldError.getDefaultMessage()))
                    .collect(Collectors.toList());

            errorResponse = ErrorResponse.builder()
                    .status(status.value())
                    .error(INVALID_ERROR.getError())
                    .message(INVALID_ERROR.getMessage())
                    .timestamp(LocalDateTime.now())
                    .path(exchange.getRequest().getPath().value())
                    .fieldErrors(!fieldErrors.isEmpty() ? fieldErrors : new ArrayList<>())
                    .build();

            exchange.getResponse().setStatusCode(status);
            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        } else if (ex instanceof CustomValidationException customEx) {
            status = INVALID_ERROR.getHttpStatus();
            fieldErrors = customEx.getFieldErrors();

            errorResponse = ErrorResponse.builder()
                    .status(status.value())
                    .error(INVALID_ERROR.getError())
                    .message(INVALID_ERROR.getMessage())
                    .timestamp(LocalDateTime.now())
                    .path(exchange.getRequest().getPath().value())
                    .fieldErrors(!fieldErrors.isEmpty() ? fieldErrors : new ArrayList<>())
                    .build();

            exchange.getResponse().setStatusCode(status);
            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        }else if (ex instanceof EntityNotFoundException e) {
            status = NOT_FOUND.getHttpStatus();
            fieldErrors.add(new FieldErrorDetail("entity", e.getMessage()));

            errorResponse = ErrorResponse.builder()
                    .status(status.value())
                    .error(NOT_FOUND.getError())
                    .message(e.getMessage())
                    .timestamp(LocalDateTime.now())
                    .path(exchange.getRequest().getPath().value())
                    .fieldErrors(fieldErrors)
                    .build();

            exchange.getResponse().setStatusCode(status);
            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        }else if (ex instanceof EntityAlreadyExistsException e) {
            status = ALREADY_EXISTS.getHttpStatus();
            fieldErrors.add(new FieldErrorDetail("entity", e.getMessage()));

            errorResponse = ErrorResponse.builder()
                    .status(status.value())
                    .error(ALREADY_EXISTS.getError())
                    .message(e.getMessage())
                    .timestamp(LocalDateTime.now())
                    .path(exchange.getRequest().getPath().value())
                    .fieldErrors(fieldErrors)
                    .build();

            exchange.getResponse().setStatusCode(status);
            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        }else if (ex instanceof ServerWebInputException inputEx) {
            Throwable cause = inputEx.getCause();
            if (cause instanceof MismatchedInputException mismatchedInputEx) {
                String field = mismatchedInputEx.getPath().stream()
                        .map(JsonMappingException.Reference::getFieldName)
                        .filter(Objects::nonNull)
                        .reduce((first, second) -> second)
                        .orElse("body");

               status = INVALID_ERROR.getHttpStatus();
                fieldErrors = List.of(new FieldErrorDetail(
                        field,
                        String.format("Expected type: %s", mismatchedInputEx.getTargetType().getSimpleName())
                ));

                errorResponse = ErrorResponse.builder()
                        .status(status.value())
                        .error(INVALID_ERROR.getError())
                        .message(INVALID_ERROR.getMessage())
                        .timestamp(LocalDateTime.now())
                        .path(exchange.getRequest().getPath().value())
                        .fieldErrors(fieldErrors)
                        .build();

                exchange.getResponse().setStatusCode(status);
                exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

            } else if (cause != null) {
                status = INVALID_ERROR.getHttpStatus();
                fieldErrors = List.of(new FieldErrorDetail("body", cause.getMessage()));

                errorResponse = ErrorResponse.builder()
                        .status(status.value())
                        .error(INVALID_ERROR.getError())
                        .message(INVALID_ERROR.getMessage())
                        .timestamp(LocalDateTime.now())
                        .path(exchange.getRequest().getPath().value())
                        .fieldErrors(fieldErrors)
                        .build();

                exchange.getResponse().setStatusCode(status);
                exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
            }
        }else if (ex instanceof MongoException || ex instanceof DataAccessException){
            status = DATABASE_ERROR.getHttpStatus();
            fieldErrors = List.of(new FieldErrorDetail("body", ex.getMessage()));

            errorResponse = ErrorResponse.builder()
                    .status(status.value())
                    .error(DATABASE_ERROR.getCode())
                    .message(DATABASE_ERROR.getMessage())
                    .timestamp(LocalDateTime.now())
                    .path(exchange.getRequest().getPath().value())
                    .fieldErrors(fieldErrors)
                    .build();

            exchange.getResponse().setStatusCode(status);
            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        }else{
            status = INTERNAL_ERROR.getHttpStatus();
            errorResponse = ErrorResponse.builder()
                .status(status.value())
                .error(INTERNAL_ERROR.getCode())
                .message(INVALID_ERROR.getMessage())
                .timestamp(LocalDateTime.now())
                .path(exchange.getRequest().getPath().value())
                .fieldErrors(new ArrayList<>())
                .build();

            exchange.getResponse().setStatusCode(status);
            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        }

        try {
            byte[] bytes = objectMapper.writeValueAsBytes(errorResponse);
            return exchange.getResponse().writeWith(Mono.just(exchange.getResponse()
                    .bufferFactory().wrap(bytes)));

        } catch (JsonProcessingException e) {
            byte[] fallback = "{\"error\": \"Error serializing response\"}".getBytes(StandardCharsets.UTF_8);
            return exchange.getResponse().writeWith(Mono.just(exchange.getResponse()
                    .bufferFactory().wrap(fallback)));
        }
    }
}
