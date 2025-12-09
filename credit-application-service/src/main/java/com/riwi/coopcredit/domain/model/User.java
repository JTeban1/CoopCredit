package com.riwi.coopcredit.domain.model;

import java.util.HashSet;
import java.util.Set;

public class User {
    private Long id;
    private String username;
    private String password;
    private String email;
    private boolean enabled;
    private Set<Role> roles;
    private Long affiliateId;

    public enum Role {
        ROLE_AFILIADO,
        ROLE_ANALISTA,
        ROLE_ADMIN
    }

    public User() {
        this.roles = new HashSet<>();
        this.enabled = true;
    }

    public User(Long id, String username, String password, String email, 
                boolean enabled, Set<Role> roles, Long affiliateId) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.enabled = enabled;
        this.roles = roles != null ? roles : new HashSet<>();
        this.affiliateId = affiliateId;
    }

    public boolean hasRole(Role role) {
        return roles != null && roles.contains(role);
    }

    public boolean isAffiliate() {
        return hasRole(Role.ROLE_AFILIADO);
    }

    public boolean isAnalyst() {
        return hasRole(Role.ROLE_ANALISTA);
    }

    public boolean isAdmin() {
        return hasRole(Role.ROLE_ADMIN);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Long getAffiliateId() {
        return affiliateId;
    }

    public void setAffiliateId(Long affiliateId) {
        this.affiliateId = affiliateId;
    }
}
