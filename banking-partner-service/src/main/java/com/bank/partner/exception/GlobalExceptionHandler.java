package com.bank.partner.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<Map<String, Object>> handleHandlerMethodValidationException(
            HandlerMethodValidationException ex, HttpServletRequest request) {

        Map<String, String> errors = ex.getAllErrors().stream()
                .collect(Collectors.toMap(
                        error -> extractFieldName((ObjectError) error),
                        error -> error.getDefaultMessage() != null ? error.getDefaultMessage() : "Validation error",
                        // En cas de clés dupliquées, concaténer les messages
                        (error1, error2) -> error1 + "; " + error2
                ));

        return ResponseEntity.badRequest()
                .body(Map.of(
                        "timestamp", LocalDateTime.now().toString(),
                        "status", HttpStatus.BAD_REQUEST.value(),
                        "message", "Erreur de validation des données",
                        "errors", errors,
                        "path", request.getRequestURI()
                ));
    }

    private String extractFieldName(ObjectError error) {
        if (error instanceof FieldError fieldError) {
            return fieldError.getField();
        }

        if (error.getArguments() != null) {
            return Arrays.stream(error.getArguments())
                    .filter(arg -> arg instanceof String)
                    .map(arg -> (String) arg)
                    .filter(arg -> !arg.isEmpty())
                    .map(this::extractLastSegment)
                    .findFirst()
                    .orElse(error.getObjectName() != null ? error.getObjectName() : "global");
        }

        // Fallback
        return error.getObjectName() != null ? error.getObjectName() : "global";
    }

    private String extractLastSegment(String path) {
        int lastDotIndex = path.lastIndexOf('.');
        return lastDotIndex >= 0 && lastDotIndex < path.length() - 1
                ? path.substring(lastDotIndex + 1)
                : path;
    }

    @ExceptionHandler(PartnerAlreadyExistsException.class)
    public ResponseEntity<PartnerErrorDetails> handlePartnerAlreadyExistsException(
            PartnerAlreadyExistsException ex, HttpServletRequest request) {

        PartnerErrorDetails errorDetails = PartnerErrorDetails.of(
                "Partner Already Exists",
                ex.getMessage(),
                request.getRequestURI(),
                HttpStatus.CONFLICT.value()
        );

        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(PartnerNotFoundException.class)
    public ResponseEntity<PartnerErrorDetails> handlePartnerNotFoundException(
            PartnerNotFoundException ex, HttpServletRequest request) {
        // Reste inchangé
        PartnerErrorDetails errorDetails = PartnerErrorDetails.of(
                "Partner Not Found",
                ex.getMessage(),
                request.getRequestURI(),
                HttpStatus.NOT_FOUND.value()
        );

        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PartnerValidationException.class)
    public ResponseEntity<PartnerErrorDetails> handlePartnerValidationException(
            PartnerValidationException ex, HttpServletRequest request) {
        // Reste inchangé
        PartnerErrorDetails errorDetails = PartnerErrorDetails.of(
                "Validation Error",
                ex.getMessage(),
                request.getRequestURI(),
                HttpStatus.BAD_REQUEST.value()
        );

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<PartnerErrorDetails> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        // Reste inchangé
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));

        PartnerErrorDetails errorDetails = PartnerErrorDetails.of(
                "Validation Error",
                errorMessage,
                request.getRequestURI(),
                HttpStatus.BAD_REQUEST.value()
        );

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<PartnerErrorDetails> handleAllException(
            Exception ex, HttpServletRequest request) {  // Correction du type

        PartnerErrorDetails errorDetails = PartnerErrorDetails.of(
                "Internal Server Error",
                ex.getMessage(),
                request.getRequestURI(),
                HttpStatus.INTERNAL_SERVER_ERROR.value()
        );

        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);  // Correction du statut
    }

}
