package com.subidha.service;

import com.subidha.config.DBConfig;

import com.subidha.model.User;
import com.subidha.util.PasswordUtil;
import com.subidha.util.ValidationUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserService {

    private DoctorService doctorService;
    private PatientService patientService; 

    public UserService() {
        this.doctorService = new DoctorService();
        this.patientService = new PatientService();
    }


    public boolean registerUser(User user, Integer departmentId) {
        Connection connection = null;
        PreparedStatement psUser = null;
        ResultSet generatedKeys = null;
        boolean success = false;


        if (user == null || ValidationUtil.isNullOrEmpty(user.getUsername()) ||
            ValidationUtil.isNullOrEmpty(user.getPassword()) ||
            ValidationUtil.isNullOrEmpty(user.getFirstName()) ||
            ValidationUtil.isNullOrEmpty(user.getLastName()) ||
            ValidationUtil.isNullOrEmpty(user.getGender()) ||
            ValidationUtil.isNullOrEmpty(user.getPhoneNumber()) ||
            ValidationUtil.isNullOrEmpty(user.getRole())) {
            System.err.println("Registration failed: Missing required user data.");
            return false;
        }

        if (isUsernameTaken(user.getUsername())) {
            System.err.println("Registration failed: Username '" + user.getUsername() + "' is already taken.");

            return false;
        }

        String userSql = "INSERT INTO users (first_name, last_name, username, gender, role, phone_number, password_hash) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            connection = DBConfig.getConnection();
            connection.setAutoCommit(false);


            psUser = connection.prepareStatement(userSql, Statement.RETURN_GENERATED_KEYS);
            psUser.setString(1, user.getFirstName());
            psUser.setString(2, user.getLastName());
            psUser.setString(3, user.getUsername());
            psUser.setString(4, user.getGender());
            psUser.setString(5, user.getRole());
            psUser.setString(6, user.getPhoneNumber());

            String salt = PasswordUtil.generateSalt();
            String hashedPassword = PasswordUtil.hashPassword(user.getPassword(), salt);
            psUser.setString(7, hashedPassword);

            int userRowsInserted = psUser.executeUpdate();

            if (userRowsInserted > 0) {
                generatedKeys = psUser.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int newUserId = generatedKeys.getInt(1);
                    user.setUserId(newUserId);


                    if ("doctor".equalsIgnoreCase(user.getRole())) {
                        if (!doctorService.addDoctorRecord(newUserId, departmentId, connection)) {
                            throw new SQLException("Failed to insert doctor record, rolling back.");
                        }
                        System.out.println("Doctor record added for user ID: " + newUserId);
                    }
                    else if ("patient".equalsIgnoreCase(user.getRole())) {
                       
                        if (!patientService.addPatientRecord(newUserId, connection)) {
                            throw new SQLException("Failed to insert patient record, rolling back.");
                        }
                        System.out.println("Doctor record added for user ID: " + newUserId);
                    }



                    connection.commit();
                    success = true;
                    System.out.println("User registered successfully (ID: " + newUserId + ", Role: " + user.getRole() + ")");

                } else {
                    throw new SQLException("Failed to retrieve generated user ID, rolling back.");
                }
            } else {
                throw new SQLException("Failed to insert user record, rolling back.");
            }

        } catch (SQLException e) {
            System.err.println("Error during user registration transaction: " + e.getMessage());
            e.printStackTrace();
            if (connection != null) {
                try {
                    System.err.println("Rolling back transaction.");
                    connection.rollback();
                } catch (SQLException ex) {
                    System.err.println("Error rolling back transaction: " + ex.getMessage());
                }
            }
            success = false;
        } finally {

             try { if (generatedKeys != null) generatedKeys.close(); } catch (SQLException e) {  }
             try { if (psUser != null) psUser.close(); } catch (SQLException e) {  }
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    DBConfig.closeConnection(connection);
                } catch (SQLException e) {
                    System.err.println("Error finalizing connection: " + e.getMessage());
                }
            }
        }
        return success;
    }



     public User loginUser(String username, String password) {
         Connection connection = null;
         PreparedStatement preparedStatement = null;
         ResultSet resultSet = null;

         try {
             connection = DBConfig.getConnection();

             String sql = "SELECT user_id, first_name, last_name, username, gender, phone_number, role, password_hash FROM users WHERE username = ?";
             preparedStatement = connection.prepareStatement(sql);
             preparedStatement.setString(1, username);
             resultSet = preparedStatement.executeQuery();

             if (resultSet.next()) {
                 String storedHashedPassword = resultSet.getString("password_hash");

                 boolean passwordMatched = PasswordUtil.verifyPassword(password, storedHashedPassword);

                 if (passwordMatched) {
                     User user = new User();
                     user.setUserId(resultSet.getInt("user_id"));
                     user.setFirstName(resultSet.getString("first_name"));
                     user.setLastName(resultSet.getString("last_name"));
                     user.setUsername(resultSet.getString("username"));
                     user.setGender(resultSet.getString("gender"));
                     user.setPhoneNumber(resultSet.getString("phone_number"));
                     user.setRole(resultSet.getString("role"));

                     System.out.println("Login successful for user: " + username + ", Role: " + user.getRole());
                     return user;
                 } else {
                     System.out.println("Password mismatch for user: " + username);
                     return null;
                 }
             } else {
                 System.out.println("User not found: " + username);
                 return null;
             }
         } catch (SQLException e) {
             System.err.println("Error during login for user " + username + ": " + e.getMessage());
             e.printStackTrace();
             return null;
         } finally {

             try { if (resultSet != null) resultSet.close(); } catch (SQLException e) {  }
             try { if (preparedStatement != null) preparedStatement.close(); } catch (SQLException e) {  }
             DBConfig.closeConnection(connection);
         }
     }


    public boolean updateUser(User user, Integer departmentId, String newPassword) {
        Connection connection = null;
        PreparedStatement psUser = null;
        boolean success = false;


         if (user == null || user.getUserId() <= 0) {
             System.err.println("Update failed: Invalid user data or ID.");
             return false;
         }


        StringBuilder sqlBuilder = new StringBuilder("UPDATE users SET first_name = ?, last_name = ?, phone_number = ?, gender = ?, updated_at = CURRENT_TIMESTAMP");
        List<Object> params = new ArrayList<>();
        params.add(user.getFirstName());
        params.add(user.getLastName());
        params.add(user.getPhoneNumber());
        params.add(user.getGender());


        String newHashedPassword = null;
        if (!ValidationUtil.isNullOrEmpty(newPassword)) {
             if (!ValidationUtil.isValidPassword(newPassword)) {
                  System.err.println("Update failed: New password does not meet criteria.");
                  return false;
             }
            String salt = PasswordUtil.generateSalt();
            newHashedPassword = PasswordUtil.hashPassword(newPassword, salt);
            sqlBuilder.append(", password_hash = ?");
            params.add(newHashedPassword);
        }

        sqlBuilder.append(" WHERE user_id = ?");
        params.add(user.getUserId());


        try {
            connection = DBConfig.getConnection();
            connection.setAutoCommit(false);


            psUser = connection.prepareStatement(sqlBuilder.toString());
            for (int i = 0; i < params.size(); i++) {
                psUser.setObject(i + 1, params.get(i));
            }

            int rowsUpdated = psUser.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("User record updated for ID: " + user.getUserId());

                 User existingUser = getUserById(user.getUserId());
                 if (existingUser != null && "doctor".equalsIgnoreCase(existingUser.getRole())) {
                     if (!doctorService.updateDoctorDepartment(user.getUserId(), departmentId, connection)) {

                          System.err.println("Warning: Failed to update department for doctor user ID " + user.getUserId() + ". User info still updated.");

                     } else {
                          System.out.println("Doctor department updated for user ID: " + user.getUserId());
                     }
                 }


                connection.commit();
                success = true;
            } else {
                 System.err.println("User record not found or no changes made for ID: " + user.getUserId());
                 connection.rollback();
            }

        } catch (SQLException e) {
            System.err.println("Error updating user ID " + user.getUserId() + ": " + e.getMessage());
            e.printStackTrace();
            if (connection != null) {
                try { connection.rollback(); } catch (SQLException ex) {  }
            }
            success = false;
        } finally {
            try { if (psUser != null) psUser.close(); } catch (SQLException e) {  }
            if (connection != null) {
                try { connection.setAutoCommit(true); } catch (SQLException e) {  }
                DBConfig.closeConnection(connection);
            }
        }
        return success;
    }



    public List<User> getAllUsers(String roleFilter, String searchTerm) {
        List<User> users = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
            "SELECT u.user_id, u.first_name, u.last_name, u.username, u.gender, u.phone_number, u.role, u.created_at, u.updated_at, " +
            "d.department_id, dep.department_name " +
            "FROM users u " +
            "LEFT JOIN doctors d ON u.user_id = d.user_id " +
            "LEFT JOIN departments dep ON d.department_id = dep.department_id " +
            "WHERE 1=1 ");

        List<Object> params = new ArrayList<>();


        if (roleFilter != null && !roleFilter.isEmpty() && !"all".equalsIgnoreCase(roleFilter)) {
            sql.append(" AND u.role = ?");
            params.add(roleFilter);
        }


        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            sql.append(" AND (u.first_name LIKE ? OR u.last_name LIKE ? OR u.username LIKE ?)");
            String likeTerm = "%" + searchTerm.trim() + "%";
            params.add(likeTerm);
            params.add(likeTerm);
            params.add(likeTerm);
        }

        sql.append(" ORDER BY u.role, u.last_name, u.first_name");

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {


            for(int i = 0; i < params.size(); i++) {
               ps.setObject(i + 1, params.get(i));
            }
            System.out.println("Executing SQL: " + ps);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    User user = mapResultSetToUser(rs);

                    if ("doctor".equalsIgnoreCase(user.getRole())) {
                         user.setDepartmentName(rs.getString("department_name"));
                    }
                    users.add(user);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching users: " + e.getMessage());
            e.printStackTrace();
        }
        return users;
    }

    public User getUserById(int userId) {

         String sql = "SELECT u.user_id, u.first_name, u.last_name, u.username, u.gender, u.phone_number, u.role, u.created_at, u.updated_at, " +
                      "d.department_id, dep.department_name " +
                      "FROM users u " +
                      "LEFT JOIN doctors d ON u.user_id = d.user_id " +
                      "LEFT JOIN departments dep ON d.department_id = dep.department_id " +
                      "WHERE u.user_id = ?";

        try (Connection conn = DBConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                     User user = mapResultSetToUser(rs);
                      if ("doctor".equalsIgnoreCase(user.getRole())) {
                          user.setDepartmentName(rs.getString("department_name"));
                      }
                     return user;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching user by ID " + userId + ": " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

     public boolean deleteUser(int userId) {
         Connection connection = null;
         boolean success = false;
         User userToDelete = getUserById(userId);

         if (userToDelete == null) {
            System.err.println("User not found for deletion: ID " + userId);
            return false;
         }

         String deleteUserSql = "DELETE FROM users WHERE user_id = ?";
         String deletePatientSql = "DELETE FROM patients WHERE user_id = ?";

         try {
             connection = DBConfig.getConnection();
             connection.setAutoCommit(false);


             if ("doctor".equalsIgnoreCase(userToDelete.getRole())) {

                  doctorService.deleteDoctorVisits(userId, connection);
                 if (!doctorService.deleteDoctorRecordByUserId(userId, connection)) {
                    throw new SQLException("Failed to delete doctor record for user ID: " + userId);
                 }
                 System.out.println("Deleted doctor record for user ID: " + userId);
             } else if ("patient".equalsIgnoreCase(userToDelete.getRole())) {
            	 try (PreparedStatement psPatient = connection.prepareStatement(deletePatientSql)) {
                     psPatient.setInt(1, userId);
                     int patientRowsAffected = psPatient.executeUpdate();
                     if (patientRowsAffected > 0) {
                         connection.commit();
                         success = true;
                     System.out.println("Deleted patient record attempt for user ID: " + userId + " (Rows: " + patientRowsAffected + ")");
                 } else {
                	 throw new SQLException("Patient record not found or deletion failed during final step.");
                 }

             }
             }


             try (PreparedStatement psUser = connection.prepareStatement(deleteUserSql)) {
                 psUser.setInt(1, userId);
                 int rowsAffected = psUser.executeUpdate();
                 if (rowsAffected > 0) {
                     connection.commit();
                     success = true;
                     System.out.println("User deleted successfully: ID " + userId);
                 } else {
                     throw new SQLException("User record not found or deletion failed during final step.");
                 }
             }

         } catch (SQLException e) {
             System.err.println("Error deleting user (ID: " + userId + "): " + e.getMessage());
             e.printStackTrace();
             if (connection != null) {
                 try { connection.rollback(); } catch (SQLException ex) {  }
             }
             success = false;
         } finally {
             if (connection != null) {
                 try { connection.setAutoCommit(true); } catch (SQLException ex) {  }
                 DBConfig.closeConnection(connection);
             }
         }
         return success;
     }


    public boolean isUsernameTaken(String username) {
         String sql = "SELECT 1 FROM users WHERE username = ?";
         try (Connection conn = DBConfig.getConnection();
              PreparedStatement ps = conn.prepareStatement(sql)) {
             ps.setString(1, username);
             try (ResultSet rs = ps.executeQuery()) {
                 return rs.next();
             }
         } catch (SQLException e) {
             System.err.println("Error checking username uniqueness for '" + username + "': " + e.getMessage());
             e.printStackTrace();

             return true;
         }
     }
    
    public int getTotalUserCount() throws SQLException {
    	 String sql = "SELECT COUNT(*) FROM users";
         try (Connection conn = DBConfig.getConnection();
              Statement stmt = conn.createStatement();
              ResultSet rs = stmt.executeQuery(sql)) {
             if (rs.next()) {
                 return rs.getInt(1);
             }
             return 0;
         }
    }
    
    public int getTotalDoctorCount() throws SQLException{
    	String sql = "SELECT COUNT(*) FROM users WHERE role=?";
    	try(Connection conn = DBConfig.getConnection();
    			PreparedStatement ps = conn.prepareStatement(sql)){
    			ps.setString(1, "doctor");
    			 try (ResultSet rs = ps.executeQuery()) {
    		            if (rs.next()) {
    		                return rs.getInt(1);
    		            }
    		            return 0;
    		        }
    	}
    }
    
    public int getTotalPatientCount() throws SQLException{
    	String sql = "SELECT COUNT(*) FROM users WHERE role=?";
    	try(Connection conn = DBConfig.getConnection();
    			PreparedStatement ps = conn.prepareStatement(sql)){
    			ps.setString(1, "patient");
    			 try (ResultSet rs = ps.executeQuery()) {
    		            if (rs.next()) {
    		                return rs.getInt(1);
    		            }
    		            return 0;
    		        }
    	}
    }


    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setUsername(rs.getString("username"));
        user.setGender(rs.getString("gender"));
        user.setPhoneNumber(rs.getString("phone_number"));
        user.setRole(rs.getString("role"));
        user.setCreatedAt(rs.getTimestamp("created_at"));
        user.setUpdatedAt(rs.getTimestamp("updated_at"));


        return user;
    }
}