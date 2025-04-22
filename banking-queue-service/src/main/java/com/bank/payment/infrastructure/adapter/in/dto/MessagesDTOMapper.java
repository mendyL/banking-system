package com.bank.payment.infrastructure.adapter.in.dto;

import com.bank.payment.domain.model.BankingMessage;
import com.bank.payment.domain.model.Page;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MessagesDTOMapper {

    public BankingMessageDTO toDto(BankingMessage message) {
        return new BankingMessageDTO(
                message.messageId(),
                message.payload(),
                message.partnerId(),
                message.receivedAt()
        );
    }

    public <T, R> PageDTO<R> toDto(Page<T> page, java.util.function.Function<T, R> converter) {
        List<R> content = page.content().stream()
                .map(converter)
                .toList();

        return new PageDTO<>(
                content,
                page.pageNumber(),
                page.pageSize(),
                page.totalElements(),
                page.totalPages()
        );
    }
}
