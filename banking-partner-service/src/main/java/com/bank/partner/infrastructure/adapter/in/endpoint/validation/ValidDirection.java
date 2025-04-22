package com.bank.partner.infrastructure.adapter.in.endpoint.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DirectionValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDirection {
    String message() default "La direction doit être INBOUND ou OUTBOUND";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
