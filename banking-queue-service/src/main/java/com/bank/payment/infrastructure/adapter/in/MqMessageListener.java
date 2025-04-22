package com.bank.payment.infrastructure.adapter.in;

import com.bank.payment.domain.port.api.MessageProcessingUseCase;
import com.bank.payment.domain.model.BankingMessage;
import jakarta.jms.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.TextMessage;

import java.util.Optional;

@Component
@Slf4j
public class MqMessageListener {

    private final MessageProcessingUseCase messageProcessingUseCase;
    private final MessagePayloadMapper messagePayloadMapper;

    public MqMessageListener(
            MessageProcessingUseCase messageProcessingUseCase,
            MessagePayloadMapper messagePayloadMapper) {
        this.messageProcessingUseCase = messageProcessingUseCase;
        this.messagePayloadMapper = messagePayloadMapper;
    }

    @JmsListener(destination = "${ibm.mq.queue-name}")
    public void onMessage(Message message, Session session) {
        try {
            log.debug("Received message: {}", message.getJMSMessageID());
            createBankingMessage(message)
                    .ifPresent(messageProcessingUseCase::processMessage);
            message.acknowledge();
        } catch (Exception e) {
            log.error("Error processing message: {}", e.getMessage(), e);
            rollbackIfPossible(session);
        }
    }

    private Optional<BankingMessage> createBankingMessage(Message message) {
        try {
            final String messageId = message.getJMSMessageID();
            final String messageContent = extractMessageBody(message);
            final var properties = messagePayloadMapper.extractProperties(messageContent);

            if (!properties.isValid()) {
                log.error("Invalid message format, missing required properties");
                return Optional.empty();
            }

            return Optional.of(BankingMessage.create(
                    messageId,
                    properties.payload(),
                    properties.partnerId()
            ));
        } catch (Exception e) {
            log.error("Error creating banking message: {}", e.getMessage(), e);
            return Optional.empty();
        }
    }

    private String extractMessageBody(Message message) throws JMSException {
        if (message instanceof TextMessage textMessage) {
            return textMessage.getText();
        }
        throw new UnsupportedOperationException("Unsupported message type: " + message.getClass().getName());
    }

    private void rollbackIfPossible(Session session) {
        try {
            if (session.getTransacted()) {
                session.rollback();
                log.info("Session rolled back");
            }
        } catch (JMSException e) {
            log.error("Failed to rollback session", e);
        }
    }
}
