package com.bank.partner.infrastructure.adapter.in.endpoint;

import com.bank.partner.domain.port.api.partner.PartnerUseCase;
import com.bank.partner.exception.PartnerNotFoundException;
import com.bank.partner.infrastructure.adapter.in.endpoint.dto.PartnerRequest;
import com.bank.partner.infrastructure.adapter.in.endpoint.dto.PartnerResponse;
import com.bank.partner.infrastructure.adapter.in.endpoint.dto.PartnerResponses;
import com.bank.partner.infrastructure.adapter.in.endpoint.mapper.PartnerDtoMapper;
import com.bank.partner.infrastructure.adapter.in.endpoint.validation.ValidatedPartnerRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/partners")
@RequiredArgsConstructor
@Slf4j
public class PartnerController  {

    private final PartnerUseCase partnerUseCase;
    private final PartnerDtoMapper partnerDtoMapper;

    @PostMapping
    public ResponseEntity<PartnerResponse> createPartner(
            @ValidatedPartnerRequest @RequestBody PartnerRequest partnerRequest) {
        log.info("Création d'un nouveau partenaire avec l'alias: {}", partnerRequest.alias());

        var partnerToCreate = partnerDtoMapper.toDomain(partnerRequest);
        var createdPartner = partnerUseCase.createPartner(partnerToCreate);
        var response = partnerDtoMapper.toResponseDto(createdPartner);

        log.info("Partenaire créé avec succès: {} (ID: {})", createdPartner.alias(), createdPartner.id());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<PartnerResponses> getAllPartners() {
        log.info("Récupération de tous les partenaires");

        var partners = partnerUseCase.getAllPartners();
        var partnerDtoList = partners.stream()
                .map(partnerDtoMapper::toResponseDto)
                .toList();

        log.info("Récupération réussie de {} partenaires", partnerDtoList.size());
        return ResponseEntity.ok(PartnerResponses.create(partnerDtoList));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PartnerResponse> getPartnerById(@PathVariable UUID id) {
        log.info("Récupération du partenaire avec l'ID: {}", id);

        var partnerId = parsePartnerId(id.toString());
        var partner = partnerUseCase.getPartnerById(partnerId)
                .orElseThrow(() -> {
                    log.warn("Partenaire non trouvé avec l'ID: {}", id);
                    return new PartnerNotFoundException("Partenaire non trouvé avec l'ID: " + id);
                });

        var response = partnerDtoMapper.toResponseDto(partner);
        log.info("Partenaire récupéré avec succès: {}", partner.alias());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PartnerResponse> deletePartnerById(@PathVariable UUID id) {
        log.info("Récupération du partenaire avec l'ID: {}", id);

        var partnerId = parsePartnerId(id.toString());
         partnerUseCase.deletePartnerById(partnerId);

        return ResponseEntity.noContent().build();
    }

    private UUID parsePartnerId(String id) {
        try {
            return UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            log.error("ID de partenaire invalide: {}", id, e);
            throw new IllegalArgumentException("ID de partenaire invalide: " + id);
        }
    }
}
