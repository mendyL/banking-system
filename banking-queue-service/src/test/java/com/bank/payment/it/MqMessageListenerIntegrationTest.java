package com.bank.payment.it;

import com.bank.payment.application.MessageProcessingService;
import com.bank.payment.domain.model.BankingMessage;
import com.bank.payment.domain.port.spi.MessagePersistenceRepository;
import com.bank.payment.infrastructure.adapter.in.MessagePayloadMapper;
import com.bank.payment.infrastructure.adapter.in.MqMessageListener;
import jakarta.jms.JMSException;
import jakarta.jms.Session;
import jakarta.jms.TextMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@Import(TestConfig.class)
@ActiveProfiles("test")
public class MqMessageListenerIntegrationTest {

    @Autowired
    private MqMessageListener mqMessageListener;

    @Autowired
    private MessageProcessingService messageProcessingService;

    @MockBean
    private MessagePersistenceRepository messagePersistenceRepository;

    @Autowired
    private MessagePayloadMapper messagePayloadMapper;

    private final UUID PARTNER_ID = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
    private final String PAYLOAD = "partner message";
    private final String CORRELATION_ID = "ID:414d5120514d312020202020202020207e5d0e62028b0040";
    private final String MESSAGE_ID = "ID:test-message-id";

    @BeforeEach
    void setUp() {
        reset(messagePersistenceRepository);
        when(messagePersistenceRepository.existsByMessageId(anyString())).thenReturn(false);
    }

    @Test
    void shouldProcessAndPersistValidMessage() throws JMSException {
        // Given
        String jsonContent = "{\"PARTNER_ID\":\"550e8400-e29b-41d4-a716-446655440000\",\"PAYLOAD\":\"partner message\"}";

        TextMessage textMessage = mock(TextMessage.class);
        when(textMessage.getText()).thenReturn(jsonContent);
        when(textMessage.getJMSMessageID()).thenReturn(MESSAGE_ID);
        when(textMessage.getJMSCorrelationID()).thenReturn(CORRELATION_ID);

        Session session = mock(Session.class);
        when(session.getTransacted()).thenReturn(true);

        // When
        mqMessageListener.onMessage(textMessage, session);

        // Then
        ArgumentCaptor<BankingMessage> messageCaptor = ArgumentCaptor.forClass(BankingMessage.class);
        verify(messagePersistenceRepository).save(messageCaptor.capture());

        BankingMessage capturedMessage = messageCaptor.getValue();
        assertThat(capturedMessage.messageId()).isEqualTo(MESSAGE_ID);
        assertThat(capturedMessage.partnerId()).isEqualTo(PARTNER_ID);
        assertThat(capturedMessage.payload()).isEqualTo(PAYLOAD);

        verify(textMessage).acknowledge();
    }

    @Test
    void shouldSkipProcessingWhenMessageAlreadyExists() throws JMSException {
        // Given
        String jsonContent = "{\"PARTNER_ID\":\"550e8400-e29b-41d4-a716-446655440000\",\"PAYLOAD\":\"partner message\"}";

        TextMessage textMessage = mock(TextMessage.class);
        when(textMessage.getText()).thenReturn(jsonContent);
        when(textMessage.getJMSMessageID()).thenReturn(MESSAGE_ID);
        when(textMessage.getJMSCorrelationID()).thenReturn(CORRELATION_ID);

        Session session = mock(Session.class);
        when(session.getTransacted()).thenReturn(true);

        // Message already exists
        when(messagePersistenceRepository.existsByMessageId(MESSAGE_ID)).thenReturn(true);

        // When
        mqMessageListener.onMessage(textMessage, session);

        // Then
        verify(messagePersistenceRepository, never()).save(any(BankingMessage.class));
        verify(textMessage).acknowledge();
    }

}
