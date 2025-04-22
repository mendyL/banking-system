package com.bank.partner.domain.port.api.partner;

import com.bank.partner.domain.model.Partner;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PartnerUseCase {

    Partner createPartner(Partner partner);
    Optional<Partner> getPartnerById(UUID id);
    List<Partner> getAllPartners();
    void deletePartnerById( UUID id);
}
