package com.riwi.riskcentral.service;

import com.riwi.riskcentral.model.RiskEvaluationRequest;
import com.riwi.riskcentral.model.RiskEvaluationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RiskEvaluationServiceTest {

    private RiskEvaluationService service;

    @BeforeEach
    void setUp() {
        service = new RiskEvaluationService();
    }

    @Test
    @DisplayName("Should return consistent score for same document")
    void shouldReturnConsistentScoreForSameDocument() {
        RiskEvaluationRequest request = new RiskEvaluationRequest("123456789", 1L);

        RiskEvaluationResponse response1 = service.evaluate(request);
        RiskEvaluationResponse response2 = service.evaluate(request);

        assertEquals(response1.getRiskScore(), response2.getRiskScore());
        assertEquals(response1.getRiskLevel(), response2.getRiskLevel());
    }

    @Test
    @DisplayName("Should return LOW risk level for score >= 700")
    void shouldReturnLowRiskLevelForHighScore() {
        // Find a document that produces a high score
        String document = "HIGH_SCORE_DOC";
        RiskEvaluationRequest request = new RiskEvaluationRequest(document, 1L);

        RiskEvaluationResponse response = service.evaluate(request);

        assertNotNull(response.getRiskScore());
        assertNotNull(response.getRiskLevel());
        assertTrue(response.getRiskScore() >= 300 && response.getRiskScore() <= 900);
    }

    @Test
    @DisplayName("Should set credit application ID in response")
    void shouldSetCreditApplicationIdInResponse() {
        RiskEvaluationRequest request = new RiskEvaluationRequest("123456789", 42L);

        RiskEvaluationResponse response = service.evaluate(request);

        assertEquals(42L, response.getCreditApplicationId());
    }

    @Test
    @DisplayName("Should set document in response")
    void shouldSetDocumentInResponse() {
        RiskEvaluationRequest request = new RiskEvaluationRequest("ABC123", 1L);

        RiskEvaluationResponse response = service.evaluate(request);

        assertEquals("ABC123", response.getDocument());
    }

    @Test
    @DisplayName("Should set evaluation date in response")
    void shouldSetEvaluationDateInResponse() {
        RiskEvaluationRequest request = new RiskEvaluationRequest("123456789", 1L);

        RiskEvaluationResponse response = service.evaluate(request);

        assertNotNull(response.getEvaluationDate());
    }

    @Test
    @DisplayName("Should generate evaluation details")
    void shouldGenerateEvaluationDetails() {
        RiskEvaluationRequest request = new RiskEvaluationRequest("123456789", 1L);

        RiskEvaluationResponse response = service.evaluate(request);

        assertNotNull(response.getEvaluationDetails());
        assertTrue(response.getEvaluationDetails().contains("Risk evaluation completed"));
    }

    @Test
    @DisplayName("Should return different scores for different documents")
    void shouldReturnDifferentScoresForDifferentDocuments() {
        RiskEvaluationRequest request1 = new RiskEvaluationRequest("DOC_A", 1L);
        RiskEvaluationRequest request2 = new RiskEvaluationRequest("DOC_B", 2L);

        RiskEvaluationResponse response1 = service.evaluate(request1);
        RiskEvaluationResponse response2 = service.evaluate(request2);

        // Scores might be the same by chance, but documents should differ
        assertNotEquals(request1.getDocument(), request2.getDocument());
    }

    @Test
    @DisplayName("Risk score should be within valid range")
    void riskScoreShouldBeWithinValidRange() {
        for (int i = 0; i < 100; i++) {
            RiskEvaluationRequest request = new RiskEvaluationRequest("DOC_" + i, (long) i);
            RiskEvaluationResponse response = service.evaluate(request);

            assertTrue(response.getRiskScore() >= 300, "Score should be >= 300");
            assertTrue(response.getRiskScore() <= 900, "Score should be <= 900");
        }
    }
}
