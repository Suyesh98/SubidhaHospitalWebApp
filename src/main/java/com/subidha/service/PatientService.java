package com.subidha.service;

import com.subidha.config.DBConfig;
import com.subidha.model.Patient;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PatientService {

  
    public boolean addPatientRecord(int userId, Connection conn) throws SQLException {
        String sql = "INSERT INTO patients (user_id) VALUES (?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Patient record created for user_id: " + userId);
                return true;
            } else {
                System.err.println("Failed to create patient record for user_id: " + userId);
                return false; 
            }
        } catch (SQLException e) {
             System.err.println("Error creating patient record for user_id " + userId + ": " + e.getMessage());
             throw e; 
        }
    }

    public Patient getPatientByUserId(int userId) {
        String sql = "SELECT patient_id, user_id, date_of_birth, address, created_at, updated_at " +
                     "FROM patients WHERE user_id = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPatient(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching patient by user ID " + userId + ": " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public boolean updatePatientDetails(int patientId, Date dateOfBirth, String address) {
        if (patientId <= 0) {
             System.err.println("Update patient details failed: Invalid patient ID.");
            return false;
        }
        
         if (dateOfBirth == null && (address == null || address.trim().isEmpty())) {
             System.out.println("No details provided to update for patient ID: " + patientId);
             return true; 
         }

        StringBuilder sqlBuilder = new StringBuilder("UPDATE patients SET updated_at = CURRENT_TIMESTAMP");
        List<Object> params = new ArrayList<>();

        if (dateOfBirth != null) {
            sqlBuilder.append(", date_of_birth = ?");
            params.add(dateOfBirth);
        }
        if (address != null && !address.trim().isEmpty()) {
            sqlBuilder.append(", address = ?");
            params.add(address.trim());
        }

        sqlBuilder.append(" WHERE patient_id = ?");
        params.add(patientId);

        
        if (params.size() > 1) { 
            try (Connection conn = DBConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(sqlBuilder.toString())) {

                for (int i = 0; i < params.size(); i++) {
                     
                     if (params.get(i) instanceof java.sql.Date) {
                         ps.setDate(i + 1, (java.sql.Date) params.get(i));
                     } else {
                          ps.setObject(i + 1, params.get(i));
                     }
                }
                 System.out.println("Executing patient update: " + ps); 
                int rowsAffected = ps.executeUpdate();
                 System.out.println("Rows affected by patient update: " + rowsAffected); 

                if (rowsAffected > 0) {
                    System.out.println("Patient details updated successfully for patient_id: " + patientId);
                    return true;
                } else {
                    
                    System.err.println("Failed to update patient details: Patient record not found or no changes made for ID " + patientId);
                    return false;
                }
            } catch (SQLException e) {
                System.err.println("Error updating patient details for ID " + patientId + ": " + e.getMessage());
                e.printStackTrace();
                return false;
            }
        } else {
             
             System.out.println("Skipping patient update as no new details were provided.");
             return true;
        }
    }

    
    private Date toSqlDate(LocalDate localDate) {
       return (localDate == null) ? null : Date.valueOf(localDate);
    }

     
     private Patient mapResultSetToPatient(ResultSet rs) throws SQLException {
         Patient patient = new Patient();
         patient.setPatientId(rs.getInt("patient_id"));
         patient.setUserId(rs.getInt("user_id"));
         
         patient.setDateOfBirth(rs.getDate("date_of_birth"));
         patient.setAddress(rs.getString("address"));
         patient.setCreatedAt(rs.getTimestamp("created_at"));
         patient.setUpdatedAt(rs.getTimestamp("updated_at"));
         return patient;
     }
    
     public Integer getPatientIdByUserId(int userId) {
         Patient patient = getPatientByUserId(userId);
         return (patient != null) ? patient.getPatientId() : null;
     }
}