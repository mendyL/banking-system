package com.bank.payment.application;


import com.bank.payment.domain.port.api.DeleteMessagesByPartnerUseCase;
import com.bank.payment.infrastructure.adapter.out.persistence.PostgresMessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class PartnerMessagesService implements DeleteMessagesByPartnerUseCase {

    private final PostgresMessageRepository postgresMessageRepository;

    public PartnerMessagesService(PostgresMessageRepository postgresMessageRepository) {
        this.postgresMessageRepository = postgresMessageRepository;
    }

    @Override
    @Transactional
    public void deleteMessageByPartner(UUID uuid) {
        postgresMessageRepository.deleteByPartnerId(uuid);
    }
}
