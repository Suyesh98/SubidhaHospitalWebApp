package com.subidha.model;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

public class Visit {
    private int visitId;
    private int patientId;
    private int doctorId;
    private Date visitDate;
    private Time visitTime;
    private String reason;
    private String status; 
    private Timestamp createdAt;
    private Timestamp updatedAt;

  
    private String doctorName;
    private String departmentName;
    private String patientName;

    // Constructors
    public Visit() {}

    // Getters and Setters
    public int getVisitId() { return visitId; }
    public void setVisitId(int visitId) { this.visitId = visitId; }

    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }

    public int getDoctorId() { return doctorId; }
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }

    public Date getVisitDate() { return visitDate; }
    public void setVisitDate(Date visitDate) { this.visitDate = visitDate; }

    public Time getVisitTime() { return visitTime; }
    public void setVisitTime(Time visitTime) { this.visitTime = visitTime; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

   
    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }

    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    @Override
    public String toString() {
        return "Visit [visitId=" + visitId + ", patientId=" + patientId + ", doctorId=" + doctorId + ", visitDate="
                + visitDate + ", visitTime=" + visitTime + ", reason=" + reason + ", status=" + status + "]";
    }
}