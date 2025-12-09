package com.riwi.coopcredit.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.riwi.coopcredit.application.dto.request.CreditApplicationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class CreditApplicationIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
        .withDatabaseName("testdb")
        .withUsername("test")
        .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.flyway.enabled", () -> "true");
        registry.add("risk-central.url", () -> "http://localhost:8081");
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // Flyway migrations will create the test data
    }

    @Test
    @DisplayName("Should create credit application with valid data")
    @WithMockUser(username = "testuser", roles = "AFILIADO")
    void shouldCreateCreditApplicationWithValidData() throws Exception {
        CreditApplicationRequest request = new CreditApplicationRequest(
            1L,
            new BigDecimal("5000000"),
            12,
            "Home renovation"
        );

        mockMvc.perform(post("/api/applications")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.status").value("PENDING"))
            .andExpect(jsonPath("$.affiliateId").value(1))
            .andExpect(jsonPath("$.requestedAmount").value(5000000))
            .andExpect(jsonPath("$.termMonths").value(12));
    }

    @Test
    @DisplayName("Should get affiliate by ID")
    @WithMockUser(username = "testuser", roles = "ANALISTA")
    void shouldGetAffiliateById() throws Exception {
        mockMvc.perform(get("/api/affiliates/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.document").value("1234567890"))
            .andExpect(jsonPath("$.firstName").value("Juan"));
    }

    @Test
    @DisplayName("Should return 404 for non-existent affiliate")
    @WithMockUser(username = "testuser", roles = "ANALISTA")
    void shouldReturn404ForNonExistentAffiliate() throws Exception {
        mockMvc.perform(get("/api/affiliates/999"))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should reject credit application with amount exceeding maximum")
    @WithMockUser(username = "testuser", roles = "AFILIADO")
    void shouldRejectCreditApplicationWithExcessiveAmount() throws Exception {
        CreditApplicationRequest request = new CreditApplicationRequest(
            1L,
            new BigDecimal("20000000"),
            12,
            "Excessive request"
        );

        mockMvc.perform(post("/api/applications")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Health endpoint should be accessible without authentication")
    void healthEndpointShouldBeAccessible() throws Exception {
        mockMvc.perform(get("/actuator/health"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("UP"));
    }

    @Test
    @DisplayName("Should require authentication for protected endpoints")
    void shouldRequireAuthenticationForProtectedEndpoints() throws Exception {
        mockMvc.perform(get("/api/affiliates/1"))
            .andExpect(status().isForbidden());
    }
}
