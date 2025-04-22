package com.bank.partner.infrastructure.adapter.in.endpoint.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PartnerRequestValidator.class)
@Target({ElementType.TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidatedPartnerRequest {
    String message() default "Requête de partenaire invalide";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}