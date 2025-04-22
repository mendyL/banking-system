package com.bank.partner.infrastructure.adapter.out.persistence;


import com.bank.partner.domain.model.Partner;
import com.bank.partner.domain.port.spi.partner.PartnerPort;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class PartnerAdapter implements PartnerPort {
    private final PartnerJpaRepository partnerRepository;
    private final PartnerEntityMapper partnerMapper;

    public PartnerAdapter(PartnerJpaRepository partnerRepository, PartnerEntityMapper partnerMapper) {
        this.partnerRepository = partnerRepository;
        this.partnerMapper = partnerMapper;
    }

    @Override
    public Partner save(Partner partner) {
        PartnerEntity entity = partnerMapper.toEntity(partner);
        PartnerEntity savedEntity = partnerRepository.save(entity);
        return partnerMapper.toDomain(savedEntity);
    }

    @Override
    public List<Partner> findAll() {
        return partnerRepository.findAll().stream()
                .map(partnerMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Partner> findById(UUID id) {
        return partnerRepository.findById(id)
                .map(partnerMapper::toDomain);
    }

    @Override
    public boolean existsByAlias(String alias) {
        return partnerRepository.existsByAlias(alias);
    }
}
