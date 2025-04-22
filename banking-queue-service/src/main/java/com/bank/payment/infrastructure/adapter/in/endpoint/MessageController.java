package com.bank.payment.infrastructure.adapter.in.endpoint;

import com.bank.payment.domain.port.api.DeleteMessagesByPartnerUseCase;
import com.bank.payment.domain.port.api.GetMessagesUseCase;
import com.bank.payment.infrastructure.adapter.in.dto.BankingMessageDTO;
import com.bank.payment.infrastructure.adapter.in.dto.MessagesDTOMapper;
import com.bank.payment.infrastructure.adapter.in.dto.PageDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api")
public class MessageController {

    private final GetMessagesUseCase getMessagesUseCase;
    private final DeleteMessagesByPartnerUseCase deleteMessagesByPartnerUseCase;
    private final MessagesDTOMapper messageDtoMapper;

    public MessageController(GetMessagesUseCase getMessagesUseCase, DeleteMessagesByPartnerUseCase deleteMessagesByPartnerUseCase, MessagesDTOMapper messageDtoMapper) {
        this.getMessagesUseCase = getMessagesUseCase;
        this.deleteMessagesByPartnerUseCase = deleteMessagesByPartnerUseCase;
        this.messageDtoMapper = messageDtoMapper;
    }

    @GetMapping("/messages")
    public ResponseEntity<PageDTO<BankingMessageDTO>> getMessages(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        var messagePage = getMessagesUseCase.getMessages(page, size);
        var pageDto = messageDtoMapper.toDto(messagePage, messageDtoMapper::toDto);

        return ResponseEntity.ok(pageDto);
    }

    @DeleteMapping("/messages/partners/{partnerId}")
    public ResponseEntity<Void> deleteMessagesForPartner(@PathVariable
                                                             UUID partnerId) {
        deleteMessagesByPartnerUseCase.deleteMessageByPartner(partnerId);

        return ResponseEntity.noContent().build();
    }
}

