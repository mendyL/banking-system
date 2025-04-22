package com.bank.payment.infrastructure.adapter.in.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record BankingMessageDTO(
        String messageId,
        String payload,
        UUID partnerId,
        LocalDateTime receivedAt
) {}
