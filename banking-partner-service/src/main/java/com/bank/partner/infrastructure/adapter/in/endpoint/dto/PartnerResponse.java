package com.bank.partner.infrastructure.adapter.in.endpoint.dto;

import java.util.UUID;

public record PartnerResponse (
        UUID id,
        String alias,
        String type,
        String direction,
        String application,
        String processedFlowType,
        String description
) {
}
