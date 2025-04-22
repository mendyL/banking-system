package com.bank.payment.infrastructure.adapter.out.persistence;

import com.bank.payment.domain.model.BankingMessage;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class MessageMapper {

    public MessageEntity toEntity(BankingMessage message) {

        return MessageEntity.builder()
                .id(UUID.randomUUID())
                .messageId(message.messageId())
                .payload(message.payload())
                .partnerId(message.partnerId())
                .receivedAt(message.receivedAt())
                .build();
    }

    public static BankingMessage mapToDomain(MessageEntity entity) {
        return new  BankingMessage(
                entity.getMessageId(),
                entity.getPayload(),
                entity.getPartnerId(),
                entity.getCreatedAt());
    }


}
