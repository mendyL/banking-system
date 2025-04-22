package com.bank.payment.it;
import com.bank.payment.application.MessageProcessingService;
import com.bank.payment.domain.model.BankingMessage;
import com.bank.payment.domain.port.spi.MessagePersistenceRepository;
import com.bank.payment.exception.MessageProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@Import(TestConfig.class)
@ActiveProfiles("test")
public class MessageProcessingServiceTest {

    @Autowired
    private MessageProcessingService messageProcessingService;

    @MockBean
    private MessagePersistenceRepository messagePersistenceRepository;

    private BankingMessage testMessage;

    @BeforeEach
    void setUp() {
        reset(messagePersistenceRepository);

        testMessage = BankingMessage.create(
                "ID:test-message-id",
                "test payload",
                UUID.fromString("550e8400-e29b-41d4-a716-446655440000")
        );

        when(messagePersistenceRepository.existsByMessageId(anyString())).thenReturn(false);
    }

    @Test
    void shouldSaveMessageWhenNotProcessedBefore() {
        // Given
        when(messagePersistenceRepository.existsByMessageId(testMessage.messageId())).thenReturn(false);

        // When
        messageProcessingService.processMessage(testMessage);

        // Then
        verify(messagePersistenceRepository).save(testMessage);
    }

    @Test
    void shouldSkipProcessingWhenMessageAlreadyExists() {
        // Given
        when(messagePersistenceRepository.existsByMessageId(testMessage.messageId())).thenReturn(true);

        // When
        messageProcessingService.processMessage(testMessage);

        // Then
        verify(messagePersistenceRepository, never()).save(any(BankingMessage.class));
    }

    @Test
    void shouldThrowExceptionWhenDatabaseErrorOccurs() {
        // Given
        doThrow(mock(DataAccessException.class))
                .when(messagePersistenceRepository).save(any(BankingMessage.class));

        // When & Then
        assertThrows(MessageProcessingException.class, () -> {
            messageProcessingService.processMessage(testMessage);
        });
    }
}

