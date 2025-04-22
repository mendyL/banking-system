package com.bank.payment.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PostgresMessageRepository extends JpaRepository<MessageEntity, UUID> {
    boolean existsByMessageId(String messageId);
    void deleteByPartnerId(UUID uuid);
}
