package com.bank.payment.infrastructure.adapter.in;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

@Slf4j
@Component
public class MessagePayloadMapper {

    public static final String PARTNER_ID = "PARTNER_ID";
    public static final String PAYLOAD = "PAYLOAD";
    private final ObjectMapper objectMapper;

    public MessagePayloadMapper() {
        this.objectMapper = JsonMapper.builder()
                .findAndAddModules()
                .build();
    }

    public MessageProperties extractProperties(String messageContent) {
        return deserializeMessageContent(messageContent)
                .map(properties -> new MessageProperties(
                        extractProperty(properties, PARTNER_ID, UUID::fromString).orElse(null),
                        extractProperty(properties, PAYLOAD, Function.identity()).orElse(null)
                ))
                .orElse(new MessageProperties(null, null));
    }

    private Optional<Map<String, String>> deserializeMessageContent(String messageContent) {
        try {
            return Optional.ofNullable(
                    objectMapper.readValue(messageContent,
                            objectMapper.getTypeFactory().constructMapType(Map.class, String.class, String.class))
            );
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize message content: {}", e.getMessage(), e);
            return Optional.empty();
        }
    }

    private <T> Optional<T> extractProperty(Map<String, String> properties, String key,
                                            Function<String, T> converter) {
        return Optional.ofNullable(properties.get(key))
                .map(value -> {
                    try {
                        return converter.apply(value);
                    } catch (Exception e) {
                        log.error("Failed to convert property {}: {}", key, e.getMessage());
                        return null;
                    }
                });
    }

    public record MessageProperties(UUID partnerId, String payload) {
        public boolean isValid() {
            return partnerId != null && payload != null;
        }
    }
}
