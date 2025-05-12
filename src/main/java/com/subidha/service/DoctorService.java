package com.subidha.service;

import com.subidha.config.DBConfig;
import com.subidha.model.Doctor;
import java.util.List;

import java.sql.*;
import java.util.ArrayList;

public class DoctorService {

	public boolean addDoctorRecord(int userId, Integer departmentId, Connection conn) throws SQLException {
		String sql = "INSERT INTO doctors (user_id, department_id) VALUES (?, ?)";

		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, userId);
			if (departmentId != null && departmentId > 0) {
				ps.setInt(2, departmentId);
			} else {
				ps.setNull(2, Types.INTEGER);
			}
			int rowsAffected = ps.executeUpdate();
			return rowsAffected > 0;
		}

	}

	
	public boolean updateDoctorDepartment(int userId, Integer departmentId, Connection conn) throws SQLException {
		String sql = "UPDATE doctors SET department_id = ?, updated_at = CURRENT_TIMESTAMP WHERE user_id = ?";

		boolean exists = checkDoctorExists(userId, conn);

		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			if (departmentId != null && departmentId > 0) {
				ps.setInt(1, departmentId);
			} else {
				ps.setNull(1, Types.INTEGER); 
			}
			ps.setInt(2, userId);

			if (exists) {

				 ps.executeUpdate();
				return true;
			} else {

				System.err.println("Warning: Attempted to update department for user ID " + userId
						+ " who does not have a doctor record.");
				return false;
			}
		}

	}

	public boolean deleteDoctorRecordByUserId(int userId, Connection conn) throws SQLException {

		String sql = "DELETE FROM doctors WHERE user_id = ?";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, userId);
			ps.executeUpdate();
			return true;
		}

	}

	public Integer getDoctorDepartmentId(int userId) {
		String sql = "SELECT department_id FROM doctors WHERE user_id = ?";
		try (Connection conn = DBConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, userId);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					int deptId = rs.getInt("department_id");
					return rs.wasNull() ? null : deptId;
				}
			}
		} catch (SQLException e) {
			System.err.println("Error getting department ID for doctor user ID " + userId + ": " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	private boolean checkDoctorExists(int userId, Connection conn) throws SQLException {
		String sql = "SELECT 1 FROM doctors WHERE user_id = ?";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, userId);
			try (ResultSet rs = ps.executeQuery()) {
				return rs.next();
			}
		}
	}

	void deleteDoctorVisits(int userId, Connection conn) throws SQLException {

		Doctor doc = getDoctorDetailsByUserId(userId);
		if (doc != null) {
			String sql = "DELETE FROM visits WHERE doctor_id = ?";
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setInt(1, doc.getDoctorId());
				ps.executeUpdate();
				System.out.println("Deleted visits for doctor_id: " + doc.getDoctorId());
			}
		}
	}

	public Doctor getDoctorDetailsByUserId(int userId) {
		 String sql = "SELECT d.doctor_id, d.user_id, d.department_id, d.created_at, d.updated_at, " +
                 "u.first_name, u.last_name, dep.department_name " +
                 "FROM doctors d " +
                 "JOIN users u ON d.user_id = u.user_id " +
                 "LEFT JOIN departments dep ON d.department_id = dep.department_id " +
                 "WHERE d.user_id = ?";
    try (Connection conn = DBConfig.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, userId);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                Doctor doctor = new Doctor();
                doctor.setDoctorId(rs.getInt("doctor_id"));
                doctor.setUserId(rs.getInt("user_id")); 

               
                doctor.setFirstName(rs.getString("first_name"));
                doctor.setLastName(rs.getString("last_name"));

                
                int deptId = rs.getInt("department_id");
                doctor.setDepartmentId(rs.wasNull() ? null : deptId);
                doctor.setDepartmentName(rs.getString("department_name")); 

                doctor.setCreatedAt(rs.getTimestamp("created_at"));
                doctor.setUpdatedAt(rs.getTimestamp("updated_at"));
					return doctor;
				}
			}
		} catch (SQLException e) {
			System.err.println("Error fetching doctor details by user ID " + userId + ": " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	 public List<Doctor> getAllDoctorsWithDetails() {
	        List<Doctor> doctors = new ArrayList<>();
	        String sql = "SELECT d.doctor_id, d.user_id, d.department_id, " +
	                     "u.first_name, u.last_name, " +
	                     "dep.department_name " +
	                     "FROM doctors d " +
	                     "JOIN users u ON d.user_id = u.user_id " +
	                     "LEFT JOIN departments dep ON d.department_id = dep.department_id " +
	                     "ORDER BY u.last_name, u.first_name";

	        try (Connection conn = DBConfig.getConnection();
	             PreparedStatement ps = conn.prepareStatement(sql);
	             ResultSet rs = ps.executeQuery()) {

	            while (rs.next()) {
	                Doctor doctor = new Doctor();
	                doctor.setDoctorId(rs.getInt("doctor_id"));
	                doctor.setUserId(rs.getInt("user_id"));
	                int deptId = rs.getInt("department_id");
	                doctor.setDepartmentId(rs.wasNull() ? null : deptId);

	                
	                doctor.setFirstName(rs.getString("first_name"));
	                doctor.setLastName(rs.getString("last_name"));
	                doctor.setDepartmentName(rs.getString("department_name")); 

	                doctors.add(doctor);
	            }
	        } catch (SQLException e) {
	            System.err.println("Error fetching doctors with details: " + e.getMessage());
	            e.printStackTrace();
	        }
	        return doctors;
	    }
	}
	
