package dev.stockman.functions.validation;

import jakarta.validation.Validator;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ValidationService {


    private final Validator validator;

    public ValidationService(Validator validator) {
        this.validator = validator;
    }

    public void validate(Object obj) {
        var violationConstraints = validator.validate(obj);
        if (!violationConstraints.isEmpty()) {
            throw new ValidationException("Input is invalid!", violationConstraints.stream().map(Violation::new).collect(Collectors.toSet()));
        }
    }

}
