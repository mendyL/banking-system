package com.bank.partner.domain.port.spi.partner;

import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface MessagePartnerPort {

    ResponseEntity<Void> deletePartnerById(UUID id);

}
