package com.bank.payment.domain.port.spi;

import com.bank.payment.domain.model.BankingMessage;
import com.bank.payment.domain.model.Page;

public interface MessageFetcherRepository {
    Page<BankingMessage> findAllMessages(int pageNumber, int pageSize);
}
