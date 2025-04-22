package com.bank.payment.application;

import com.bank.payment.domain.port.api.MessageProcessingUseCase;
import com.bank.payment.domain.port.spi.MessagePersistenceRepository;
import com.bank.payment.domain.model.BankingMessage;
import com.bank.payment.exception.MessageProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class MessageProcessingService implements MessageProcessingUseCase {

    private final MessagePersistenceRepository persistenceService;

    public MessageProcessingService(MessagePersistenceRepository persistenceService) {
        this.persistenceService = persistenceService;
    }

    @Override
    @Transactional
    public void processMessage(BankingMessage message) {
        try {
            log.info("Processing message: {}", message.messageId());

            if (persistenceService.existsByMessageId(message.messageId())) {
                log.info("Message already processed, skipping: {}", message.messageId());
                return;
            }

            persistenceService.save(message);
            log.info("Successfully processed and persisted message: {}", message.messageId());

        } catch (DataAccessException ex) {
            log.error("Database error while processing message: {}", message.messageId(), ex);
            throw new MessageProcessingException("Database error while processing message: " + message.messageId(), ex);
        } catch (Exception ex) {
            log.error("Unexpected error processing message: {}", message.messageId(), ex);
            throw new MessageProcessingException("Failed to process message: " + message.messageId(), ex);
        }
    }

}