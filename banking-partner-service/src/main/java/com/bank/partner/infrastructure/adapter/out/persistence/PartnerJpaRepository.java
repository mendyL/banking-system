package com.bank.partner.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PartnerJpaRepository extends JpaRepository<PartnerEntity, UUID> {

    boolean existsByAlias(String alias);
}
