package com.bank.partner.infrastructure.adapter.in.endpoint;

import com.bank.partner.domain.model.Partner;
import com.bank.partner.domain.port.api.partner.PartnerUseCase;
import com.bank.partner.infrastructure.adapter.in.endpoint.dto.DirectionDTO;
import com.bank.partner.infrastructure.adapter.in.endpoint.dto.PartnerRequest;
import com.bank.partner.infrastructure.adapter.in.endpoint.dto.PartnerResponse;
import com.bank.partner.infrastructure.adapter.in.endpoint.dto.ProcessedFlowTypeDTO;
import com.bank.partner.infrastructure.adapter.in.endpoint.mapper.PartnerDtoMapper;
import com.bank.partner.infrastructure.adapter.out.persistence.PartnerAdapter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PartnerController.class)
class PartnerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PartnerUseCase partnerUseCase;

    @MockBean
    private PartnerDtoMapper partnerDtoMapper;

    @MockBean
    private PartnerAdapter partnerPersistenceAdapter;


    @Test
    @DisplayName("Devrait créer un partenaire avec succès quand les données sont valides")
    void shouldCreatePartnerSuccessfullyWhenDataIsValid() throws Exception {
        // Given
        var partnerId = UUID.randomUUID();
        var partnerRequest = createValidPartnerRequest();

        var partnerDomain = new Partner(
                partnerId,
                "Test Partner",
                "BANK",
                Partner.Direction.INBOUND,
                "Test App",
                Partner.ProcessedFlowType.MESSAGE,
                "Test Description"
        );

        var partnerResponse = new PartnerResponse(
                partnerId,
                "Test Partner",
                "BANK",
                Partner.Direction.INBOUND.name(),
                "Test App",
                Partner.ProcessedFlowType.MESSAGE.name(),
                "Test Description"
        );

        when(partnerDtoMapper.toDomain(any(PartnerRequest.class))).thenReturn(partnerDomain);
        when(partnerUseCase.createPartner(any(Partner.class))).thenReturn(partnerDomain);
        when(partnerDtoMapper.toResponseDto(any(Partner.class))).thenReturn(partnerResponse);

        // When & Then
        mockMvc.perform(post("/api/partners")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(partnerRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(partnerId.toString()))
                .andExpect(jsonPath("$.alias").value("Test Partner"))
                .andExpect(jsonPath("$.type").value("BANK"))
                .andExpect(jsonPath("$.direction").value("INBOUND"))
                .andExpect(jsonPath("$.application").value("Test App"))
                .andExpect(jsonPath("$.processedFlowType").value("MESSAGE"))
                .andExpect(jsonPath("$.description").value("Test Description"));
    }

    @ParameterizedTest(name = "Cas #{index}: {0}")
    @MethodSource("invalidPartnerRequestProvider")
    @DisplayName("Devrait retourner une erreur 400 quand les données sont invalides")
    void shouldReturn400WhenDataIsInvalid(String testCase, PartnerRequest invalidRequest, String expectedErrorField) throws Exception {
        // When & Then
        mockMvc.perform(post("/api/partners")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Erreur de validation des données"))
                .andExpect(jsonPath("$.errors." + expectedErrorField).exists());
    }

    private static Stream<Arguments> invalidPartnerRequestProvider() {
        return Stream.of(
                Arguments.of(
                        "Alias manquant",
                        new PartnerRequest(null, "BANK", DirectionDTO.INBOUND, "Test App", ProcessedFlowTypeDTO.MESSAGE, "Test Description"),
                        "alias"
                ),
                Arguments.of(
                        "Alias vide",
                        new PartnerRequest("", "BANK", DirectionDTO.INBOUND, "Test App", ProcessedFlowTypeDTO.MESSAGE, "Test Description"),
                        "alias"
                ),
                Arguments.of(
                        "Type manquant",
                        new PartnerRequest("Test Partner", null, DirectionDTO.INBOUND, "Test App", ProcessedFlowTypeDTO.MESSAGE, "Test Description"),
                        "type"
                ),
                Arguments.of(
                        "Direction manquante",
                        new PartnerRequest("Test Partner","BANK", null, "Test App", ProcessedFlowTypeDTO.MESSAGE, "Test Description"),
                        "direction"
                ),
                Arguments.of(
                        "Description manquante",
                        new PartnerRequest("Test Partner", "BANK", DirectionDTO.INBOUND, "Test App", ProcessedFlowTypeDTO.MESSAGE, null),
                        "description"
                ),
                Arguments.of(
                        "Description vide",
                        new PartnerRequest("Test Partner", "BANK", DirectionDTO.INBOUND, "Test App", ProcessedFlowTypeDTO.MESSAGE, ""),
                        "description"
                )
        );
    }

    private static PartnerRequest createValidPartnerRequest() {
        return new PartnerRequest(
                "Test Partner",
                "BANK",
                DirectionDTO.INBOUND,
                "Test App",
                ProcessedFlowTypeDTO.MESSAGE,
                "Test Description"
        );
    }

    @Test
    @DisplayName("Devrait valider correctement les valeurs d'énumération Direction")
    void shouldCorrectlyValidateDirectionEnumValues() throws Exception {
        // Given
        var partnerId = UUID.randomUUID();

        var partnerDomain = new Partner(
                partnerId,
                "Test Partner",
                "BANK",
                Partner.Direction.INBOUND,
                "Test App",
                Partner.ProcessedFlowType.MESSAGE,
                "Test Description"
        );

        var partnerResponse = new PartnerResponse(
                partnerId,
                "Test Partner",
                "BANK",
                "INBOUND",
                "Test Description",
                "Test App",
                "MESSAGE"
        );

        when(partnerDtoMapper.toDomain(any(PartnerRequest.class))).thenReturn(partnerDomain);
        when(partnerUseCase.createPartner(any(Partner.class))).thenReturn(partnerDomain);
        when(partnerDtoMapper.toResponseDto(any(Partner.class))).thenReturn(partnerResponse);

        // Test avec INBOUND
        var inboundRequest = new PartnerRequest(
                "Test Partner",
                "BANK",
                DirectionDTO.INBOUND,
                "Test App",
                ProcessedFlowTypeDTO.MESSAGE,
                "Test Description"
        );

        mockMvc.perform(post("/api/partners")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inboundRequest)))
                .andExpect(status().isCreated());

        // Test avec OUTBOUND
        var outboundRequest = new PartnerRequest(
                "Test Partner",
                "BANK",
                DirectionDTO.OUTBOUND,
                "Test App",
                ProcessedFlowTypeDTO.MESSAGE,
                "Test Description"
        );

        mockMvc.perform(post("/api/partners")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(outboundRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Devrait valider correctement les valeurs d'énumération ProcessedFlowType")
    void shouldCorrectlyValidateProcessedFlowTypeEnumValues() throws Exception {
        // Given
        var partnerId = UUID.randomUUID();

        var partnerDomain = new Partner(
                partnerId,
                "Test Partner",
                "BANK",
                Partner.Direction.INBOUND,
                "Test App",
                Partner.ProcessedFlowType.MESSAGE,
                "Test Description"
        );

        var partnerResponse = new PartnerResponse(
                partnerId,
                "Test Partner",
                "BANK",
                "INBOUND",
                "Test Description",
                "Test App",
                "MESSAGE"
        );

        when(partnerDtoMapper.toDomain(any(PartnerRequest.class))).thenReturn(partnerDomain);
        when(partnerUseCase.createPartner(any(Partner.class))).thenReturn(partnerDomain);
        when(partnerDtoMapper.toResponseDto(any(Partner.class))).thenReturn(partnerResponse);

        // Test avec MESSAGE
        var messageRequest = new PartnerRequest(
                "Test Partner",
                "BANK",
                DirectionDTO.INBOUND,
                "Test App",
                ProcessedFlowTypeDTO.MESSAGE,
                "Test Description"
        );

        mockMvc.perform(post("/api/partners")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(messageRequest)))
                .andExpect(status().isCreated());

        // Test avec ALERTING
        var alertingRequest = new PartnerRequest(
                "Test Partner",
                "BANK",
                DirectionDTO.INBOUND,
                "Test App",
                ProcessedFlowTypeDTO.ALERTING,
                "Test Description"
        );

        mockMvc.perform(post("/api/partners")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(alertingRequest)))
                .andExpect(status().isCreated());

        // Test avec NOTIFICATION
        var notificationRequest = new PartnerRequest(
                "Test Partner",
                "BANK",
                DirectionDTO.INBOUND,
                "Test App",
                ProcessedFlowTypeDTO.NOTIFICATION,
                "Test Description"
        );

        mockMvc.perform(post("/api/partners")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(notificationRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Devrait valider correctement les valeurs d'énumération PartnerType")
    void shouldCorrectlyValidatePartnerTypeEnumValues() throws Exception {
        // Given
        var partnerId = UUID.randomUUID();

        var partnerDomain = new Partner(
                partnerId,
                "Test Partner",
                "BANK",
                Partner.Direction.INBOUND,
                "Test App",
                Partner.ProcessedFlowType.MESSAGE,
                "Test Description"
        );

        var partnerResponse = new PartnerResponse(
                partnerId,
                "Test Partner",
                "BANK",
                "INBOUND",
                "Test Description",
                "Test App",
                "MESSAGE"
        );

        when(partnerDtoMapper.toDomain(any(PartnerRequest.class))).thenReturn(partnerDomain);
        when(partnerUseCase.createPartner(any(Partner.class))).thenReturn(partnerDomain);
        when(partnerDtoMapper.toResponseDto(any(Partner.class))).thenReturn(partnerResponse);

        // Test avec BANK
        var bankRequest = new PartnerRequest(
                "Test Partner",
                "BANK",
                DirectionDTO.INBOUND,
                "Test App",
                ProcessedFlowTypeDTO.MESSAGE,
                "Test Description"
        );

        mockMvc.perform(post("/api/partners")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bankRequest)))
                .andExpect(status().isCreated());

        // Test avec MERCHANT
        var merchantRequest = new PartnerRequest(
                "Test Partner",
                "MERCHANT",
                DirectionDTO.INBOUND,
                "Test App",
                ProcessedFlowTypeDTO.MESSAGE,
                "Test Description"
        );

        mockMvc.perform(post("/api/partners")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(merchantRequest)))
                .andExpect(status().isCreated());

        // Test avec PAYMENT_PROCESSOR
        var paymentProcessorRequest = new PartnerRequest(
                "Test Partner",
                "PAYMENT_PROCESSOR",
                DirectionDTO.INBOUND,
                "Test App",
                ProcessedFlowTypeDTO.MESSAGE,
                "Test Description"
        );

        mockMvc.perform(post("/api/partners")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentProcessorRequest)))
                .andExpect(status().isCreated());

        // Test avec CARD_ISSUER
        var cardIssuerRequest = new PartnerRequest(
                "Test Partner",
                "CARD_ISSUER",
                DirectionDTO.INBOUND,
                "Test App",
                ProcessedFlowTypeDTO.MESSAGE,
                "Test Description"
        );

    }
}

