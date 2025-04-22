package com.bank.partner.infrastructure.adapter.in.endpoint.validation;

import com.bank.partner.infrastructure.adapter.in.endpoint.dto.DirectionDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class DirectionValidator implements ConstraintValidator<ValidDirection,DirectionDTO> {

    @Override
    public boolean isValid(DirectionDTO value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        return Arrays.asList(DirectionDTO.values()).contains(value);
    }
}
