package com.bank.payment.domain.port.api;

import java.util.UUID;

public interface DeleteMessagesByPartnerUseCase {

    void deleteMessageByPartner(UUID uuid);
}
