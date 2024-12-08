package com.kshrd.kroya_api.util;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = WholeNumberInRangeValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface WholeNumberInRange {
    String message() default "Rating must be a whole number between 1 and 5.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
