package com.bank.partner.application;

import com.bank.partner.domain.model.Partner;
import com.bank.partner.domain.port.api.partner.PartnerUseCase;
import com.bank.partner.domain.port.spi.partner.MessagePartnerPort;
import com.bank.partner.domain.port.spi.partner.PartnerPort;
import com.bank.partner.exception.PartnerAlreadyExistsException;
import com.bank.partner.exception.PartnerValidationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PartnerManagementService implements PartnerUseCase {

    private final PartnerPort partnerPort;
    private final MessagePartnerPort messagePartnerPort;

    public PartnerManagementService(PartnerPort partnerPort, MessagePartnerPort messagePartnerPort) {
        this.partnerPort = partnerPort;
        this.messagePartnerPort = messagePartnerPort;
    }

    @Override
    @Transactional
    public Partner createPartner(Partner partner) {
        validateNewPartner(partner);
        return partnerPort.save(partner);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Partner> getAllPartners() {
        return partnerPort.findAll();
    }

    @Override
    @Transactional
    public void deletePartnerById(UUID id) {
        messagePartnerPort.deletePartnerById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Partner> getPartnerById(UUID id) {
        return partnerPort.findById(id);
    }

    private void validateNewPartner(Partner partner) {

        if (partner.alias() == null || partner.alias().isBlank()) {
            throw new PartnerValidationException("Partner alias is required");
        }

        if(partnerPort.existsByAlias(partner.alias())) {
            throw new PartnerAlreadyExistsException("Partner whith alias "+partner.alias()+" already exists ");

        }

        if (partner.type() == null) {
            throw new PartnerValidationException("Partner type is required");
        }

        if (partner.description() == null || partner.description().isBlank()) {
            throw new PartnerValidationException("Partner description is required");
        }

    }
}
