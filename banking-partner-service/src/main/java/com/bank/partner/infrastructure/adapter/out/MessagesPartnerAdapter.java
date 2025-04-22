package com.bank.partner.infrastructure.adapter.out;


import com.bank.partner.domain.port.spi.partner.MessagePartnerPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.UUID;

@Component
public class MessagesPartnerAdapter implements MessagePartnerPort {

    private final WebClient webClient;
    private final String messagesPath;

    public MessagesPartnerAdapter(
            @Value("${service.message-queue.url}") String baseUrl,
            @Value("${service.message-queue.messagesByPartner}") String messagesPath) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
        this.messagesPath = messagesPath;
    }

    public ResponseEntity<Void> deletePartner(UUID id) {
        return webClient.delete()
                .uri(messagesPath + "/{partnerId}", id)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    @Override
    public ResponseEntity<Void> deletePartnerById(UUID id) {
        return deletePartner(id);
    }
}

