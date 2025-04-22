package com.bank.partner.domain.model;

import java.util.UUID;

public record Partner(
        UUID id,
        String alias,
        String type,
        Direction direction,
        String application,
        ProcessedFlowType processedFlowType,
        String description
) {
    public enum Direction {
        INBOUND, OUTBOUND
    }

    public enum ProcessedFlowType {
        MESSAGE, ALERTING, NOTIFICATION
    }


    public static Partner create(
            String alias,
            String type,
            Direction direction,
            String application,
            ProcessedFlowType processedFlowType,
            String description
    ) {
        return new Partner(
                UUID.randomUUID(),
                alias,
                type,
                direction,
                application,
                processedFlowType,
                description
        );
    }

}
