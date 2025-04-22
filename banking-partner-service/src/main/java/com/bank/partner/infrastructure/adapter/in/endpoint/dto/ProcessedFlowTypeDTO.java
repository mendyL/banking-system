package com.bank.partner.infrastructure.adapter.in.endpoint.dto;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ProcessedFlowTypeDTO {

    MESSAGE("MESSAGE"),
    ALERTING("ALERTING"),
    NOTIFICATION("NOTIFICATION");

    private final String value;

    ProcessedFlowTypeDTO(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

}
