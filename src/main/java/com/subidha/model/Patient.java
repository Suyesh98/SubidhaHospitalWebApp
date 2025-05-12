package com.subidha.model;

import java.sql.Date;
import java.sql.Timestamp;

public class Patient {
    private int patientId;
    private int userId;
    private Date dateOfBirth; 
    private String address;   
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Constructors
    public Patient() {}

    // Getters and Setters
    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Patient [patientId=" + patientId + ", userId=" + userId + ", dateOfBirth=" + dateOfBirth
                + ", address=" + address + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
    }
}