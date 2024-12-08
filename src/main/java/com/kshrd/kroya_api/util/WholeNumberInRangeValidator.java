package com.kshrd.kroya_api.util;

import com.kshrd.kroya_api.util.WholeNumberInRange;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class WholeNumberInRangeValidator implements ConstraintValidator<WholeNumberInRange, Number> {

    @Override
    public void initialize(WholeNumberInRange constraintAnnotation) {}

    @Override
    public boolean isValid(Number value, ConstraintValidatorContext context) {
        if (value == null) return false;

        // Check if the value is within range and is a whole number
        double doubleValue = value.doubleValue();
        return doubleValue >= 1 && doubleValue <= 5 && doubleValue % 1 == 0;
    }
}
