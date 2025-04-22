package com.bank.payment.infrastructure.adapter.out.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PostgresMessageRepositoryReader extends JpaRepository<MessageEntity, UUID> {
    Page<MessageEntity> findAll(Pageable pageable);

}
