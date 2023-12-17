package dev.stockman.functions.validation;

import jakarta.validation.ConstraintViolation;

import java.util.Optional;

public record Violation(
        String path,
        String message,
        Object invalidValue
) {
    Violation(ConstraintViolation<?> constraintViolation) {
        this(Optional.ofNullable(constraintViolation.getPropertyPath()).isPresent()
                ? Optional.ofNullable(constraintViolation.getPropertyPath()).get().toString()
                : null, constraintViolation.getMessage(), constraintViolation.getInvalidValue()
        );
    }
}
