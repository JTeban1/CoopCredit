package com.riwi.coopcredit.application.dto.response;

import java.util.Set;

public record AuthResponse(
    String token,
    String type,
    String username,
    String email,
    Set<String> roles,
    Long affiliateId
) {
    public AuthResponse(String token, String username, String email, 
                        Set<String> roles, Long affiliateId) {
        this(token, "Bearer", username, email, roles, affiliateId);
    }
}
