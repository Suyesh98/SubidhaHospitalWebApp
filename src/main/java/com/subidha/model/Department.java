package com.subidha.model;

import java.sql.Timestamp;

public class Department {
    private int departmentId;
    private String departmentName;
    private Timestamp createdAt; 
    private Timestamp updatedAt; 

    // Constructors
    public Department() {}

    public Department(int departmentId, String departmentName, Timestamp createdAt, Timestamp updatedAt) {
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

     public Department(String departmentName) {
        this.departmentName = departmentName;
    }


    // Getters and Setters
    public int getDepartmentId() { return departmentId; }
    public void setDepartmentId(int departmentId) { this.departmentId = departmentId; }
    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return "Department{" +
               "departmentId=" + departmentId +
               ", departmentName='" + departmentName + '\'' +
               ", createdAt=" + createdAt +
               ", updatedAt=" + updatedAt +
               '}';
    }
}