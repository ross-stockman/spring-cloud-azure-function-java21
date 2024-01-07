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

    public <T> void validate(T obj, LogicalValidator<T> logic) {
        var violationConstraints = validator.validate(obj).stream().map(Violation::new).collect(Collectors.toSet());
        violationConstraints.addAll(logic.apply(obj));
        if (!violationConstraints.isEmpty()) {
            throw new ValidationException("Input is invalid!", violationConstraints);
        }
    }

    public void validate(Object obj) {
        validate(obj, (o) -> Set.of());
    }

}
