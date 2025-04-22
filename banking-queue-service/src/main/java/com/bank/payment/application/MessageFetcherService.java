package com.bank.payment.application;

import com.bank.payment.domain.model.BankingMessage;
import com.bank.payment.domain.model.Page;
import com.bank.payment.domain.port.api.GetMessagesUseCase;
import com.bank.payment.domain.port.spi.MessageFetcherRepository;
import org.springframework.stereotype.Service;

@Service
public class MessageFetcherService implements GetMessagesUseCase {

    private final MessageFetcherRepository messageFetcherRepository;
    public MessageFetcherService(MessageFetcherRepository messageFetcherRepository) {
        this.messageFetcherRepository = messageFetcherRepository;
    }

    @Override
    public Page<BankingMessage> getMessages(int pageNumber, int pageSize) {
        return messageFetcherRepository.findAllMessages(pageNumber, pageSize);
    }
}
