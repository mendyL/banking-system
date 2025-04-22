package com.bank.partner.infrastructure.adapter.in.endpoint.dto;

public record PartnerRequest (
        String alias,
        String type,
        DirectionDTO direction,
        String application,
        ProcessedFlowTypeDTO processedFlowType,
        String description
){
}
