package com.bank.partner.infrastructure.adapter.out.persistence;

import com.bank.partner.domain.model.Partner;
import org.springframework.stereotype.Component;

@Component
public class PartnerEntityMapper {

    public PartnerEntity toEntity(Partner domain) {
        return PartnerEntity.builder()
                .id(domain.id())
                .alias(domain.alias())
                .partnerType(domain.type())
                .direction(domain.direction())
                .application(domain.application())
                .processedFlowType(domain.processedFlowType() != null ? domain.processedFlowType() : null)
                .description(domain.description())
                .build();
    }

    public Partner toDomain(PartnerEntity entity) {
        return new Partner(
                entity.getId(),
                entity.getAlias(),
                entity.getPartnerType(),
                Partner.Direction.valueOf(entity.getDirection().name()),
                entity.getApplication(),
                entity.getProcessedFlowType() != null ? Partner.ProcessedFlowType.valueOf(entity.getProcessedFlowType().name()) : null,
                entity.getDescription()
        );
    }
}

