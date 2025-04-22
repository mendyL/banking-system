package com.bank.partner.infrastructure.adapter.out.persistence;

import com.bank.partner.domain.model.Partner;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "partners")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartnerEntity {

    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private String alias;

    private String application;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Partner.ProcessedFlowType processedFlowType;

    @Column(nullable = false)
    private String partnerType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Partner.Direction direction;

    @Column(nullable = false)
    private String description;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
