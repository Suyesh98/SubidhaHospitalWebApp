
package com.subidha.service;

import java.sql.Connection;  
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.subidha.config.DBConfig;
import com.subidha.model.User;
import com.subidha.util.PasswordUtil;


public class UserService{
	// Method to register a new user
    public boolean registerUser(User user) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = DBConfig.getConnection();
            String sql = "INSERT INTO users (first_name, last_name, username, visit_date, gender, phone_number, department, password) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getUsername());
            if (user.getVisitDate() != null) {
                preparedStatement.setDate(4, new java.sql.Date(user.getVisitDate().getTime()));
            } else {
                preparedStatement.setDate(4, null); 
            }
            preparedStatement.setString(5, user.getGender());
            preparedStatement.setString(6, user.getPhoneNumber());
            preparedStatement.setString(7, user.getDepartment());

            // Hash the password before storing it
            String salt = PasswordUtil.generateSalt();
            String hashedPassword = PasswordUtil.hashPassword(user.getPassword(), salt);

            preparedStatement.setString(8, hashedPassword);

            int rowsInserted = preparedStatement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace(); 
            return false;
        } finally {
            DBConfig.closeConnection(connection);
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing PreparedStatement: " + e.getMessage());
            }
        }
    }
    
 // Method to login user
    public User loginUser(String username, String password) {
     Connection connection = null;
     PreparedStatement preparedStatement = null;
     ResultSet resultSet = null;

     try {
         connection = DBConfig.getConnection();
         String sql = "SELECT id, first_name, last_name, username, visit_date, gender, phone_number, department, password, role FROM users WHERE username = ?";
         preparedStatement = connection.prepareStatement(sql);
         preparedStatement.setString(1, username);
         resultSet = preparedStatement.executeQuery();

         if (resultSet.next()) {
        	 System.out.println("user found....");
             String storedHashedPassword = resultSet.getString("password");
             // verify password
             boolean passwordMatched = PasswordUtil.verifyPassword(password, storedHashedPassword);
             
             // check if password matched with stored password
             if (passwordMatched) {
                 // Passwords match, create and return the user object
                 User user = new User();
                 user.setId(resultSet.getInt("id"));
                 user.setFirstName(resultSet.getString("first_name"));
                 user.setLastName(resultSet.getString("last_name"));
                 user.setUsername(resultSet.getString("username"));
                 Date visitDate = resultSet.getDate("visit_date");
                 user.setVisitDate(visitDate);

                 user.setGender(resultSet.getString("gender"));
                 user.setPhoneNumber(resultSet.getString("phone_number"));
                 user.setDepartment(resultSet.getString("department"));
                 user.setRole(resultSet.getString("role"));
                 System.out.println("password matched...");
                 return user;
             } else {
            	 System.out.println("pass not matched....");
                 // Passwords do not match
                 return null;
             }
         } else {
        	 System.out.println("user not found....");
             // User not found
             return null;
         }
     } catch (SQLException e) {
         e.printStackTrace(); 
         return null;
     } finally {
         DBConfig.closeConnection(connection);
         try {
             if (preparedStatement != null) {
                 preparedStatement.close();
             }
             if (resultSet != null) {
                 resultSet.close();
             }
         } catch (SQLException e) {
             System.err.println("Error closing resources: " + e.getMessage());
         }
     }
 }
    
    public boolean updateUser(User user) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
        	
            System.out.println("trying to update...");
            connection = DBConfig.getConnection();
            System.out.println("got connecction ...");
            String sql = "UPDATE users SET first_name = ?, last_name = ?, phone_number = ?, department = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getPhoneNumber());
            preparedStatement.setString(4, user.getDepartment());
            preparedStatement.setInt(5, user.getId()); // user's ID to identify the record to update
            	
            System.out.println("executing query...");
            int rowsUpdated = preparedStatement.executeUpdate();
            System.out.println("query execcuted...");
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace(); 
            return false;
        } finally {
            DBConfig.closeConnection(connection);
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing PreparedStatement: " + e.getMessage());
            }
        }
    }
    
}