package com.riwi.coopcredit.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Affiliate {
    private Long id;
    private String document;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private BigDecimal salary;
    private LocalDate affiliationDate;
    private boolean active;

    public Affiliate() {
    }

    public Affiliate(Long id, String document, String firstName, String lastName, 
                     String email, String phone, BigDecimal salary, 
                     LocalDate affiliationDate, boolean active) {
        this.id = id;
        this.document = document;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.salary = salary;
        this.affiliationDate = affiliationDate;
        this.active = active;
    }

    public boolean canApplyForCredit() {
        return active;
    }

    public BigDecimal getMaxCreditAmount() {
        return salary.multiply(BigDecimal.valueOf(5));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public LocalDate getAffiliationDate() {
        return affiliationDate;
    }

    public void setAffiliationDate(LocalDate affiliationDate) {
        this.affiliationDate = affiliationDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
