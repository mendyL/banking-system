package com.bank.partner.infrastructure.adapter.in.endpoint.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ProcessedFlowTypeValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidProcessedFlowType {
    String message() default "Le type de flux traité doit être MESSAGE, ALERTING ou NOTIFICATION";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
