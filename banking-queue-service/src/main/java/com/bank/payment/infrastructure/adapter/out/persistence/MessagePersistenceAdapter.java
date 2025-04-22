package com.bank.payment.infrastructure.adapter.out.persistence;

import com.bank.payment.domain.model.BankingMessage;
import com.bank.payment.domain.port.spi.MessagePersistenceRepository;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class MessagePersistenceAdapter implements MessagePersistenceRepository {

    private final PostgresMessageRepository messageRepository;
    private final MessageMapper messageMapper;

    public MessagePersistenceAdapter(PostgresMessageRepository messageRepository, MessageMapper messageMapper) {
        this.messageRepository = messageRepository;
        this.messageMapper = messageMapper;
    }

    @Override
    public void save(BankingMessage message) {
        MessageEntity entity = messageMapper.toEntity(message);
        messageRepository.save(entity);
    }


    @Override
    public boolean existsByMessageId(String messageId) {
        return messageRepository.existsByMessageId(messageId);
    }

    @Override
    public void deleteMessagesByPartnerId(UUID id) {
         messageRepository.deleteByPartnerId(id);
    }
}
