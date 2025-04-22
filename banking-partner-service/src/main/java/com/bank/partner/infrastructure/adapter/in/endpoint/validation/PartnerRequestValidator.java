package com.bank.partner.infrastructure.adapter.in.endpoint.validation;

import com.bank.partner.infrastructure.adapter.in.endpoint.dto.DirectionDTO;
import com.bank.partner.infrastructure.adapter.in.endpoint.dto.PartnerRequest;
import com.bank.partner.infrastructure.adapter.in.endpoint.dto.ProcessedFlowTypeDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class PartnerRequestValidator implements ConstraintValidator<ValidatedPartnerRequest, PartnerRequest> {

    @Override
    public boolean isValid(PartnerRequest request, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        boolean isValid = true;

        if (request.alias() == null || request.alias().isBlank()) {
            addConstraintViolation(context, "L'alias est obligatoire", "alias");
            isValid = false;
        }

        if (request.type() == null) {
            addConstraintViolation(context, "Le type est obligatoire", "type");
            isValid = false;
        }

        if (request.direction() == null) {
            addConstraintViolation(context, "La direction est obligatoire", "direction");
            isValid = false;
        } else if (!Arrays.asList(DirectionDTO.values()).contains(request.direction())) {
            addConstraintViolation(context, "La direction doit être INBOUND ou OUTBOUND", "direction");
            isValid = false;
        }

        if (request.processedFlowType() == null) {
            addConstraintViolation(context, "Le type de flux traité est obligatoire", "processedFlowType");
            isValid = false;
        } else if (!Arrays.asList(ProcessedFlowTypeDTO.values()).contains(request.processedFlowType())) {
            addConstraintViolation(context, "Le type de flux traité doit être MESSAGE, ALERTING ou NOTIFICATION", "processedFlowType");
            isValid = false;
        }

        if (request.description() == null || request.description().isBlank()) {
            addConstraintViolation(context, "La description est obligatoire", "description");
            isValid = false;
        }

        return isValid;
    }

    private void addConstraintViolation(ConstraintValidatorContext context, String message, String property) {
        context.buildConstraintViolationWithTemplate(message)
                .addPropertyNode(property)
                .addConstraintViolation();
    }
}
