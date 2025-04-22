package com.bank.payment.domain.port.spi;

import com.bank.payment.domain.model.BankingMessage;

import java.util.UUID;

public interface MessagePersistenceRepository {
    void save(BankingMessage message);
    boolean existsByMessageId(String messageId);
    void deleteMessagesByPartnerId(UUID id);
}
