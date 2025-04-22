package com.bank.partner.domain.port.spi.partner;

import com.bank.partner.domain.model.Partner;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PartnerPort {

    Partner save(Partner partner);

    List<Partner> findAll();

    Optional<Partner> findById(UUID id);

    boolean existsByAlias(String alias);

}
