package com.bank.payment.infrastructure.adapter.out.persistence;

import com.bank.payment.domain.model.BankingMessage;
import com.bank.payment.domain.model.Page;
import com.bank.payment.domain.port.spi.MessageFetcherRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class MessageReaderAdapter implements MessageFetcherRepository {

    private final PostgresMessageRepositoryReader messageRepositoryReader;

    public MessageReaderAdapter(PostgresMessageRepositoryReader messageRepositoryReader) {
        this.messageRepositoryReader = messageRepositoryReader;
    }

    @Override
    public Page<BankingMessage> findAllMessages(int pageNumber, int pageSize) {
        var pageable = PageRequest.of(pageNumber, pageSize);
        var messagePage = messageRepositoryReader.findAll(pageable);

        var messages = messagePage.getContent().stream()
                .map(MessageMapper::mapToDomain)
                .collect(Collectors.toList());

        return new Page<>(
                messages,
                messagePage.getNumber(),
                messagePage.getSize(),
                messagePage.getTotalElements(),
                messagePage.getTotalPages()
        );
    }
}
