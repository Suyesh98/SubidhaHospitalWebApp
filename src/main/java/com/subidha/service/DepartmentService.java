package com.subidha.service;

import com.subidha.config.DBConfig;
import com.subidha.model.Department;
import com.subidha.util.ValidationUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentService {

    public List<Department> getAllDepartments() {
        List<Department> departments = new ArrayList<>();
        String sql = "SELECT department_id, department_name, created_at, updated_at FROM departments ORDER BY department_name";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                departments.add(mapResultSetToDepartment(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching departments: " + e.getMessage());
            e.printStackTrace();
        }
        return departments;
    }

    public Department getDepartmentById(int id) {
         String sql = "SELECT department_id, department_name, created_at, updated_at FROM departments WHERE department_id = ?";
         try (Connection conn = DBConfig.getConnection();
              PreparedStatement ps = conn.prepareStatement(sql)) {
             ps.setInt(1, id);
             try (ResultSet rs = ps.executeQuery()) {
                 if (rs.next()) {
                     return mapResultSetToDepartment(rs);
                 }
             }
         } catch (SQLException e) {
             System.err.println("Error fetching department by ID " + id + ": " + e.getMessage());
             e.printStackTrace();
         }
         return null; 
     }

    public boolean addDepartment(String name) throws SQLException {
        if (ValidationUtil.isNullOrEmpty(name)) {
            return false; 
        }
        String sql = "INSERT INTO departments (department_name) VALUES (?)";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name.trim());
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error adding department '" + name + "': " + e.getMessage());
             e.printStackTrace();
             throw e; 
        }
    }

    public boolean updateDepartment(int id, String name) throws SQLException {
        if (ValidationUtil.isNullOrEmpty(name)) {
             return false; 
         }
        String sql = "UPDATE departments SET department_name = ?, updated_at = CURRENT_TIMESTAMP WHERE department_id = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name.trim());
            ps.setInt(2, id);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating department ID " + id + ": " + e.getMessage());
             e.printStackTrace();
             throw e; 
        }
    }

    public boolean isDepartmentInUse(int id) {
        
        String sql = "SELECT COUNT(*) FROM doctors WHERE department_id = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking if department " + id + " is in use: " + e.getMessage());
             e.printStackTrace();
           
        }
        return false; 
    }

    public boolean deleteDepartment(int id) throws SQLException {
        if (isDepartmentInUse(id)) {
            System.err.println("Attempt to delete department ID " + id + " which is in use.");
            return false; 
        }

        String sql = "DELETE FROM departments WHERE department_id = ?";
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting department ID " + id + ": " + e.getMessage());
             e.printStackTrace();
             throw e; 
        }
    }

    public int getTotalDepartmentCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM departments";
        try (Connection conn = DBConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        }
    }
   
    private Department mapResultSetToDepartment(ResultSet rs) throws SQLException {
        Department dept = new Department();
        dept.setDepartmentId(rs.getInt("department_id"));
        dept.setDepartmentName(rs.getString("department_name"));
        dept.setCreatedAt(rs.getTimestamp("created_at"));
        dept.setUpdatedAt(rs.getTimestamp("updated_at"));
        return dept;
    }
}