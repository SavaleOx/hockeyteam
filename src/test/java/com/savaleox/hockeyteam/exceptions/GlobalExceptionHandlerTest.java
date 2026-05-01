package com.savaleox.hockeyteam.exceptions;

import com.savaleox.hockeyteam.dto.ErrorResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.Min;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;
    private MockHttpServletRequest request;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
        request = new MockHttpServletRequest();
        request.setRequestURI("/api/test");
    }

    @Test
    void handleValidationExceptions_shouldReturn400() throws Exception {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "request");
        bindingResult.addError(new FieldError("request", "name", "Name is required"));

        Method method = GlobalExceptionHandler.class.getDeclaredMethod(
                "handleValidationExceptions",
                MethodArgumentNotValidException.class,
                HttpServletRequest.class
        );
        MethodParameter parameter = new MethodParameter(method, 0);
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(parameter, bindingResult);

        ResponseEntity<ErrorResponseDto> response = handler.handleValidationExceptions(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().getStatus());
        assertEquals("Validation Failed", response.getBody().getError());
        assertEquals("Invalid input data. Please check the errors.", response.getBody().getMessage());
        assertEquals("Name is required", response.getBody().getValidationErrors().get("name"));
    }

    @Test
    void handleConstraintViolation_shouldReturn400() {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        InvalidPayload payload = new InvalidPayload(0);
        ConstraintViolationException ex = new ConstraintViolationException(validator.validate(payload));

        ResponseEntity<ErrorResponseDto> response = handler.handleConstraintViolation(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Constraint Violation", response.getBody().getError());
        assertTrue(response.getBody().getValidationErrors().containsKey("amount"));
    }

    @Test
    void handleDataIntegrityViolation_shouldReturn409() {
        RuntimeException rootCause = new RuntimeException("duplicate key value violates unique constraint");
        DataIntegrityViolationException ex = new DataIntegrityViolationException("Integrity", rootCause);

        ResponseEntity<ErrorResponseDto> response = handler.handleDataIntegrityViolation(ex, request);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(409, response.getBody().getStatus());
    }

    @Test
    void handleIllegalState_shouldReturn409() {
        ResponseEntity<ErrorResponseDto> response =
                handler.handleIllegalState(new IllegalStateException("Team already has a coach"), request);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Conflict", response.getBody().getError());
        assertEquals("Team already has a coach", response.getBody().getMessage());
    }

    @Test
    void handleNoSuchElement_shouldReturn404() {
        ResponseEntity<ErrorResponseDto> response =
                handler.handleNoSuchElement(new NoSuchElementException("Player not found"), request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Resource Not Found", response.getBody().getError());
        assertEquals("Player not found", response.getBody().getMessage());
    }

    @Test
    void handleIllegalArgument_shouldReturn400() {
        ResponseEntity<ErrorResponseDto> response =
                handler.handleIllegalArgument(new IllegalArgumentException("Invalid age"), request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Bad Request", response.getBody().getError());
        assertEquals("Invalid age", response.getBody().getMessage());
    }

    @Test
    void handleTypeMismatch_shouldReturn400() throws Exception {
        Method method = GlobalExceptionHandler.class.getDeclaredMethod(
                "handleIllegalArgument",
                IllegalArgumentException.class,
                HttpServletRequest.class
        );
        MethodParameter parameter = new MethodParameter(method, 0);
        MethodArgumentTypeMismatchException ex =
                new MethodArgumentTypeMismatchException("abc", Long.class, "id", parameter, null);

        ResponseEntity<ErrorResponseDto> response = handler.handleTypeMismatch(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Type Mismatch", response.getBody().getError());
        assertTrue(response.getBody().getMessage().contains("Parameter 'id' should be of type Long"));
    }

    @Test
    void handleJsonParseError_shouldReturn400() {
        HttpMessageNotReadableException ex = new HttpMessageNotReadableException(
                "Malformed JSON",
                new HttpInputMessage() {
                    @Override
                    public HttpHeaders getHeaders() {
                        return new HttpHeaders();
                    }
                    @Override
                    public InputStream getBody() {
                        return InputStream.nullInputStream();
                    }
                }
        );

        ResponseEntity<ErrorResponseDto> response = handler.handleJsonParseError(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Malformed JSON Request", response.getBody().getError());
    }

    @Test
    void handleRuntimeException_shouldReturn500() {
        ResponseEntity<ErrorResponseDto> response =
                handler.handleRuntimeException(new RuntimeException("Something went wrong"), request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Internal Server Error", response.getBody().getError());
    }

    @Test
    void handleGenericException_shouldReturn500() {
        ResponseEntity<ErrorResponseDto> response =
                handler.handleGenericException(new Exception("Unexpected error"), request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Internal Server Error", response.getBody().getError());
        assertEquals("An unexpected error occurred", response.getBody().getMessage());
    }

    private static class InvalidPayload {
        @Min(1)
        private final int amount;

        private InvalidPayload(int amount) {
            this.amount = amount;
        }
    }
}