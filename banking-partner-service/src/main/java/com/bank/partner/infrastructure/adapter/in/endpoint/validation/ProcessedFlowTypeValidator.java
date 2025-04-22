package com.bank.partner.infrastructure.adapter.in.endpoint.validation;

import com.bank.partner.infrastructure.adapter.in.endpoint.dto.ProcessedFlowTypeDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class ProcessedFlowTypeValidator  implements ConstraintValidator<ValidProcessedFlowType, ProcessedFlowTypeDTO> {

    @Override
    public boolean isValid(ProcessedFlowTypeDTO value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        return Arrays.asList(ProcessedFlowTypeDTO.values()).contains(value);
    }
}
