package com.riwi.coopcredit.infrastructure.adapter.in.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.riwi.coopcredit.application.dto.request.CreditApplicationRequest;
import com.riwi.coopcredit.application.dto.response.CreditApplicationResponse;
import com.riwi.coopcredit.application.mapper.CreditApplicationMapper;
import com.riwi.coopcredit.domain.model.CreditApplication;
import com.riwi.coopcredit.domain.port.in.EvaluateCreditApplicationUseCase;
import com.riwi.coopcredit.domain.port.in.RegisterCreditApplicationUseCase;
import com.riwi.coopcredit.domain.port.out.CreditApplicationRepositoryPort;
import com.riwi.coopcredit.infrastructure.security.JwtTokenProvider;
import com.riwi.coopcredit.infrastructure.security.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CreditApplicationController.class, 
    excludeAutoConfiguration = UserDetailsServiceAutoConfiguration.class)
class CreditApplicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RegisterCreditApplicationUseCase registerCreditApplicationUseCase;

    @MockBean
    private EvaluateCreditApplicationUseCase evaluateCreditApplicationUseCase;

    @MockBean
    private CreditApplicationRepositoryPort creditApplicationRepository;

    @MockBean
    private CreditApplicationMapper creditApplicationMapper;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    private CreditApplication testApplication;
    private CreditApplicationResponse testResponse;

    @BeforeEach
    void setUp() {
        testApplication = new CreditApplication();
        testApplication.setId(1L);
        testApplication.setAffiliateId(1L);
        testApplication.setRequestedAmount(new BigDecimal("5000000"));
        testApplication.setTermMonths(12);
        testApplication.setPurpose("Home renovation");
        testApplication.setStatus(CreditApplication.ApplicationStatus.PENDING);
        testApplication.setApplicationDate(LocalDateTime.now());

        testResponse = new CreditApplicationResponse(
            1L,
            1L,
            new BigDecimal("5000000"),
            12,
            "Home renovation",
            "PENDING",
            LocalDateTime.now(),
            new BigDecimal("416666.67"),
            null
        );
    }

    @Test
    @DisplayName("Should create credit application successfully")
    @WithMockUser(roles = "AFILIADO")
    void shouldCreateCreditApplication() throws Exception {
        CreditApplicationRequest request = new CreditApplicationRequest(
            1L,
            new BigDecimal("5000000"),
            12,
            "Home renovation"
        );

        when(creditApplicationMapper.toDomain(any())).thenReturn(testApplication);
        when(registerCreditApplicationUseCase.register(any())).thenReturn(testApplication);
        when(creditApplicationMapper.toResponse(any())).thenReturn(testResponse);

        mockMvc.perform(post("/api/applications")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    @DisplayName("Should get credit application by ID")
    @WithMockUser(roles = "AFILIADO")
    void shouldGetCreditApplicationById() throws Exception {
        when(creditApplicationRepository.findById(1L)).thenReturn(Optional.of(testApplication));
        when(creditApplicationMapper.toResponse(any())).thenReturn(testResponse);

        mockMvc.perform(get("/api/applications/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.affiliateId").value(1));
    }

    @Test
    @DisplayName("Should get applications by affiliate ID")
    @WithMockUser(roles = "ANALISTA")
    void shouldGetApplicationsByAffiliateId() throws Exception {
        when(creditApplicationRepository.findByAffiliateId(1L))
            .thenReturn(List.of(testApplication));
        when(creditApplicationMapper.toResponse(any())).thenReturn(testResponse);

        mockMvc.perform(get("/api/applications/affiliate/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    @DisplayName("Should evaluate credit application")
    @WithMockUser(roles = "ANALISTA")
    void shouldEvaluateCreditApplication() throws Exception {
        testApplication.setStatus(CreditApplication.ApplicationStatus.APPROVED);
        CreditApplicationResponse approvedResponse = new CreditApplicationResponse(
            1L, 1L, new BigDecimal("5000000"), 12, "Home renovation",
            "APPROVED", LocalDateTime.now(), new BigDecimal("416666.67"), null
        );

        when(evaluateCreditApplicationUseCase.evaluate(1L)).thenReturn(testApplication);
        when(creditApplicationMapper.toResponse(any())).thenReturn(approvedResponse);

        mockMvc.perform(post("/api/applications/1/evaluate")
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    @DisplayName("Should return 400 for invalid request")
    @WithMockUser(roles = "AFILIADO")
    void shouldReturn400ForInvalidRequest() throws Exception {
        CreditApplicationRequest invalidRequest = new CreditApplicationRequest(
            null,
            null,
            null,
            null
        );

        mockMvc.perform(post("/api/applications")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 403 for unauthorized access")
    void shouldReturn403ForUnauthorizedAccess() throws Exception {
        mockMvc.perform(get("/api/applications/1"))
            .andExpect(status().isUnauthorized());
    }
}
