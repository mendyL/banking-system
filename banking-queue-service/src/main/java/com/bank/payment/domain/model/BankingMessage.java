package com.bank.payment.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public record BankingMessage(
        String messageId,
        String payload,
        UUID partnerId,
        LocalDateTime receivedAt
){

public static BankingMessage create(String messageId, String payload, UUID partnerId) {
    return new BankingMessage(
            messageId,
            payload,
            partnerId,
            LocalDateTime.now()
    );
}

}
