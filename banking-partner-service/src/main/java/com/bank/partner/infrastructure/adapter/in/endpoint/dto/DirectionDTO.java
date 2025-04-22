package com.bank.partner.infrastructure.adapter.in.endpoint.dto;

import com.fasterxml.jackson.annotation.JsonValue;

public enum DirectionDTO {

    INBOUND("INBOUND"),
    OUTBOUND("OUTBOUND");

    private final String value;

    DirectionDTO(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
