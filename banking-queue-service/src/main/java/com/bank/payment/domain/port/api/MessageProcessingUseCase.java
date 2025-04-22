package com.bank.payment.domain.port.api;

import com.bank.payment.domain.model.BankingMessage;

public interface MessageProcessingUseCase {
    void processMessage(BankingMessage message);
}
