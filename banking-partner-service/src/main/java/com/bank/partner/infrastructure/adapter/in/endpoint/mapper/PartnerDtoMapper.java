package com.bank.partner.infrastructure.adapter.in.endpoint.mapper;

import com.bank.partner.domain.model.Partner;
import com.bank.partner.domain.model.Partner.Direction;
import com.bank.partner.domain.model.Partner.ProcessedFlowType;
import com.bank.partner.infrastructure.adapter.in.endpoint.dto.*;
import org.springframework.stereotype.Component;
import java.util.Optional;


@Component
public class PartnerDtoMapper {

    public PartnerResponse toResponseDto(Partner partner) {
        return Optional.ofNullable(partner)
                .map(p -> new PartnerResponse(p.id(),
                        p.alias(),
                        p.type(),
                        p.direction().name(),
                        p.processedFlowType().name(),
                        p.application(),
                        p.description()))
                .orElse(null);
    }

    public Partner toDomain(PartnerRequest request) {
        return Optional.ofNullable(request)
                .map(dto -> Partner.create(
                        dto.alias(),
                        dto.type(),
                        mapDirection(dto.direction()),
                        dto.application(),
                        mapProcessedFlowType(dto.processedFlowType()),
                        dto.description()))
                .orElse(null);
    }


    private Direction mapDirection(DirectionDTO directionEnum) {
        return Optional.ofNullable(directionEnum)
                .map(direction -> Direction.valueOf(direction.name()))
                .orElseThrow(() -> new IllegalArgumentException("Direction de partenaire invalide"));
    }

    private ProcessedFlowType mapProcessedFlowType(ProcessedFlowTypeDTO flowTypeEnum) {
        return Optional.ofNullable(flowTypeEnum)
                .map(flowType -> ProcessedFlowType.valueOf(flowType.getValue()))
                .orElseThrow(() -> new IllegalArgumentException("Type de flux traité invalide"));
    }
}
