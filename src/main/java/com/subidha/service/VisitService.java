package com.subidha.service;

import com.subidha.config.DBConfig;
import com.subidha.model.Visit;
import com.subidha.util.ValidationUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VisitService {

    public boolean bookVisit(Visit visit) {
        if (visit == null || visit.getPatientId() <= 0 || visit.getDoctorId() <= 0 ||
            visit.getVisitDate() == null || visit.getVisitTime() == null) {
            System.err.println("Booking failed: Missing required visit data.");
            return false;
        }

        String sql = "INSERT INTO visits (patient_id, doctor_id, visit_date, visit_time, reason, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, visit.getPatientId());
            ps.setInt(2, visit.getDoctorId());
            ps.setDate(3, visit.getVisitDate());
            ps.setTime(4, visit.getVisitTime());
            ps.setString(5, ValidationUtil.isNullOrEmpty(visit.getReason()) ? null : visit.getReason().trim());
            ps.setString(6, "Pending"); 

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Visit booked successfully for patient_id: " + visit.getPatientId());
                return true;
            } else {
                 System.err.println("Failed to insert visit record for patient_id: " + visit.getPatientId());
                 return false;
            }

        } catch (SQLException e) {
            System.err.println("Error booking visit for patient " + visit.getPatientId() + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List<Visit> getVisitsByPatientId(int patientId, String searchTerm, String statusFilter) {
        List<Visit> visits = new ArrayList<>();
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT v.visit_id, v.patient_id, v.doctor_id, v.visit_date, v.visit_time, ")
                .append("v.reason, v.status, v.created_at, v.updated_at, ")
                .append("u.first_name, u.last_name, dep.department_name ")
                .append("FROM visits v ")
                .append("JOIN doctors d ON v.doctor_id = d.doctor_id ")
                .append("JOIN users u ON d.user_id = u.user_id ")
                .append("LEFT JOIN departments dep ON d.department_id = dep.department_id ")
                .append("WHERE v.patient_id = ? "); 

        params.add(patientId);
        
        if (!ValidationUtil.isNullOrEmpty(statusFilter) && !"all".equalsIgnoreCase(statusFilter)) {
            sql.append("AND v.status = ? ");
            params.add(statusFilter);
        }

        
        if (!ValidationUtil.isNullOrEmpty(searchTerm)) {
            sql.append("AND (LOWER(u.first_name) LIKE LOWER(?) OR ");
            sql.append("LOWER(u.last_name) LIKE LOWER(?) OR ");
            sql.append("LOWER(CONCAT(u.first_name, ' ', u.last_name)) LIKE LOWER(?) OR ");
            sql.append("LOWER(dep.department_name) LIKE LOWER(?) OR ");
            sql.append("LOWER(v.reason) LIKE LOWER(?)) ");
            String likeTerm = "%" + searchTerm.trim() + "%";
            params.add(likeTerm);
            params.add(likeTerm);
            params.add(likeTerm);
            params.add(likeTerm);
            params.add(likeTerm);
        }

        
        sql.append("ORDER BY v.visit_date DESC, v.visit_time DESC");

    

        try (Connection conn = DBConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql.toString())) {

               for (int i = 0; i < params.size(); i++) {
                   ps.setObject(i + 1, params.get(i));
               }
               try (ResultSet rs = ps.executeQuery()) {
                   while (rs.next()) {
                        try { 
                            visits.add(mapResultSetToVisitWithDetails(rs));
                        } catch (SQLException mapEx) {
                            System.err.println("Error mapping row for visit_id (approx): " + rs.getInt("visit_id") + " - " + mapEx.getMessage());
                          
                        }
                   }
               }
           } catch (SQLException e) {
               System.err.println("Error fetching visits for patient ID " + patientId + ": " + e.getMessage());
               e.printStackTrace(); 
           }
           System.out.println("Finished fetching visits. Count: " + visits.size()); 
           return visits;
       }

    public List<Visit> getAllVisits(String searchTerm, String statusFilter) {
        List<Visit> visits = new ArrayList<>();
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT v.visit_id, v.patient_id, v.doctor_id, v.visit_date, v.visit_time, ")
            .append("v.reason, v.status, v.created_at, v.updated_at, ")
            
            .append("u_doc.first_name AS doctor_first_name, u_doc.last_name AS doctor_last_name, ")
            .append("dep.department_name, ")
            
            .append("u_pat.first_name AS patient_first_name, u_pat.last_name AS patient_last_name ")
            .append("FROM visits v ")
            .append("JOIN doctors d ON v.doctor_id = d.doctor_id ")
            .append("JOIN users u_doc ON d.user_id = u_doc.user_id ") 
            .append("LEFT JOIN departments dep ON d.department_id = dep.department_id ")
            .append("JOIN patients p ON v.patient_id = p.patient_id ") 
            .append("JOIN users u_pat ON p.user_id = u_pat.user_id "); 

        
        boolean whereClauseAdded = false;

        
        if (!ValidationUtil.isNullOrEmpty(statusFilter) && !"all".equalsIgnoreCase(statusFilter)) {
            sql.append("WHERE v.status = ? "); 
            params.add(statusFilter);
            whereClauseAdded = true;
        }

        
        if (!ValidationUtil.isNullOrEmpty(searchTerm)) {
            if (!whereClauseAdded) {
                sql.append("WHERE "); 
            } else {
                sql.append("AND "); 
            }
            sql.append("(LOWER(u_doc.first_name) LIKE LOWER(?) OR "); 
            sql.append("LOWER(u_doc.last_name) LIKE LOWER(?) OR ");    
            sql.append("LOWER(CONCAT(u_doc.first_name, ' ', u_doc.last_name)) LIKE LOWER(?) OR "); 
            sql.append("LOWER(u_pat.first_name) LIKE LOWER(?) OR "); 
            sql.append("LOWER(u_pat.last_name) LIKE LOWER(?) OR ");    
            sql.append("LOWER(CONCAT(u_pat.first_name, ' ', u_pat.last_name)) LIKE LOWER(?) OR "); 
            sql.append("LOWER(dep.department_name) LIKE LOWER(?) OR "); 
            sql.append("LOWER(v.reason) LIKE LOWER(?)) ");             

            String likeTerm = "%" + searchTerm.trim() + "%";
            
            params.add(likeTerm); 
            params.add(likeTerm); 
            params.add(likeTerm); 
            params.add(likeTerm); 
            params.add(likeTerm); 
            params.add(likeTerm); 
            params.add(likeTerm); 
            params.add(likeTerm); 
        }

        
        sql.append("ORDER BY v.visit_date DESC, v.visit_time DESC");

        System.out.println("Executing SQL (getAllVisits): " + sql.toString());
        System.out.println("Parameters: " + params);

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                     try {
                        visits.add(mapResultSetToVisitWithAdminDetails(rs)); 
                     } catch (SQLException mapEx) {
                         System.err.println("Error mapping row in getAllVisits for visit_id (approx): " + rs.getInt("visit_id") + " - " + mapEx.getMessage());
                     }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all visits: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("Finished fetching all visits. Count: " + visits.size());
        return visits;
    }
    
    
    public List<Visit> getVisitsByDoctorId(int doctorId, String searchTerm, String statusFilter) {
        List<Visit> visits = new ArrayList<>();
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT v.visit_id, v.patient_id, v.doctor_id, v.visit_date, v.visit_time, ")
                .append("v.reason, v.status, v.created_at, v.updated_at, ")
                
                .append("p_user.first_name AS patient_first_name, p_user.last_name AS patient_last_name ")
                
                .append("FROM visits v ")
                .append("JOIN patients pat ON v.patient_id = pat.patient_id ")
                .append("JOIN users p_user ON pat.user_id = p_user.user_id ")
                .append("WHERE v.doctor_id = ? ");

        params.add(doctorId);

        if (!ValidationUtil.isNullOrEmpty(statusFilter) && !"all".equalsIgnoreCase(statusFilter)) {
            sql.append("AND v.status = ? ");
            params.add(statusFilter);
        }

        if (!ValidationUtil.isNullOrEmpty(searchTerm)) {
            sql.append("AND (LOWER(p_user.first_name) LIKE LOWER(?) OR ");
            sql.append("LOWER(p_user.last_name) LIKE LOWER(?) OR ");
            sql.append("LOWER(CONCAT(p_user.first_name, ' ', p_user.last_name)) LIKE LOWER(?) OR ");
            sql.append("LOWER(v.reason) LIKE LOWER(?)) "); 
            String likeTerm = "%" + searchTerm.trim().toLowerCase() + "%";
            params.add(likeTerm);
            params.add(likeTerm);
            params.add(likeTerm);
            params.add(likeTerm);
        }

        sql.append("ORDER BY v.visit_date DESC, v.visit_time DESC");

        System.out.println("Executing SQL (getVisitsByDoctorId): " + sql.toString());
        System.out.println("Parameters: " + params);

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    try {
                        visits.add(mapResultSetToVisitForDoctorView(rs));
                    } catch (SQLException mapEx) {
                        
                        int visitIdOnError = -1;
                        try {
                           if(!(rs.isBeforeFirst() || rs.isAfterLast())) visitIdOnError = rs.getInt("visit_id");
                        } catch (SQLException fetchIdEx) {  }
                        System.err.println("Error mapping row in getVisitsByDoctorId for visit_id (approx): " + visitIdOnError + " - " + mapEx.getMessage());
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching visits for doctor ID " + doctorId + ": " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("Finished fetching visits for doctor " + doctorId + ". Count: " + visits.size());
        return visits;
    }

    private Visit mapResultSetToVisitWithDetails(ResultSet rs) throws SQLException {
        Visit visit = new Visit();
        visit.setVisitId(rs.getInt("visit_id"));
        visit.setPatientId(rs.getInt("patient_id"));
        visit.setDoctorId(rs.getInt("doctor_id"));
        visit.setVisitDate(rs.getDate("visit_date"));
        visit.setVisitTime(rs.getTime("visit_time"));
        visit.setReason(rs.getString("reason"));
        visit.setStatus(rs.getString("status"));
        visit.setCreatedAt(rs.getTimestamp("created_at"));
        visit.setUpdatedAt(rs.getTimestamp("updated_at"));

        
        visit.setDoctorName(rs.getString("first_name") + " " + rs.getString("last_name"));
        visit.setDepartmentName(rs.getString("department_name")); 

        return visit;
    }
    
    
    private Visit mapResultSetToVisitWithAdminDetails(ResultSet rs) throws SQLException {
        Visit visit = new Visit();
        visit.setVisitId(rs.getInt("visit_id"));
        visit.setPatientId(rs.getInt("patient_id"));
        visit.setDoctorId(rs.getInt("doctor_id"));
        visit.setVisitDate(rs.getDate("visit_date"));
        visit.setVisitTime(rs.getTime("visit_time"));
        visit.setReason(rs.getString("reason"));
        visit.setStatus(rs.getString("status"));
        visit.setCreatedAt(rs.getTimestamp("created_at"));
        visit.setUpdatedAt(rs.getTimestamp("updated_at"));

        
        visit.setDoctorName(rs.getString("doctor_first_name") + " " + rs.getString("doctor_last_name"));
        visit.setDepartmentName(rs.getString("department_name"));
        visit.setPatientName(rs.getString("patient_first_name") + " " + rs.getString("patient_last_name")); 

        return visit;
    }
    
    
    
    private Visit mapResultSetToVisitForDoctorView(ResultSet rs) throws SQLException {
        Visit visit = new Visit();
        visit.setVisitId(rs.getInt("visit_id"));
        visit.setPatientId(rs.getInt("patient_id"));
        visit.setDoctorId(rs.getInt("doctor_id")); 
        visit.setVisitDate(rs.getDate("visit_date"));
        visit.setVisitTime(rs.getTime("visit_time"));
        visit.setReason(rs.getString("reason"));
        visit.setStatus(rs.getString("status"));
        visit.setCreatedAt(rs.getTimestamp("created_at"));
        visit.setUpdatedAt(rs.getTimestamp("updated_at"));

        visit.setPatientName(rs.getString("patient_first_name") + " " + rs.getString("patient_last_name"));
        
        
        return visit;
    }
    
    
    
    public boolean updateVisitStatus(int visitId, String newStatus) throws SQLException {
        String sql = "UPDATE visits SET status = ? WHERE visit_id = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setInt(2, visitId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        }
    }


    public boolean deleteVisit(int visitId) throws SQLException {
        String sql = "DELETE FROM visits WHERE visit_id = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, visitId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        }
    }
    
    public int getTotalVisitCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM visits";
        try (Connection conn = DBConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        }
    }

    public int getPendingVisitCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM visits WHERE status = 'Pending'";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        }
    }
    
    public int getConfirmedVisitCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM visits WHERE status = 'Confirmed'";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        }
    }
    
    public int getCompletedVisitCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM visits WHERE status = 'Completed'";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        }
    }
    
    public int getCancelledVisitCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM visits WHERE status = 'Cancelled'";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        }
    }
    
    
    public int getTotalVisitCountForDoctor(int doctorId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM visits WHERE doctor_id = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, doctorId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    public int getPendingVisitCountForDoctor(int doctorId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM visits WHERE doctor_id = ? AND status = 'Pending'";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, doctorId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    public int getConfirmedVisitCountForDoctor(int doctorId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM visits WHERE doctor_id = ? AND status = 'Confirmed'";
         try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, doctorId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }
    
    public int getCompletedVisitCountForDoctor(int doctorId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM visits WHERE doctor_id = ? AND status = 'Completed'";
         try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, doctorId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }
    
    public int getCancelledVisitCountForDoctor(int doctorId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM visits WHERE doctor_id = ? AND status = 'Cancelled'";
         try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, doctorId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }
    
    
}