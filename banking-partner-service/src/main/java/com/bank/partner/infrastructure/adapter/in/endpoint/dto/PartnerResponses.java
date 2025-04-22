package com.bank.partner.infrastructure.adapter.in.endpoint.dto;

import java.util.List;

public record PartnerResponses(List<PartnerResponse> partners) {

    public static PartnerResponses create(List<PartnerResponse> partners) {
        return new PartnerResponses(partners);
    }
}
