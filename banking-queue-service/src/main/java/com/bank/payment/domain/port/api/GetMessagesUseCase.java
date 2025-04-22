package com.bank.payment.domain.port.api;

import com.bank.payment.domain.model.BankingMessage;
import com.bank.payment.domain.model.Page;

public interface GetMessagesUseCase {
    Page<BankingMessage> getMessages(int pageNumber, int pageSize);

}
