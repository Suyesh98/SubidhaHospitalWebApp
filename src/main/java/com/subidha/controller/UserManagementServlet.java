package com.subidha.controller;

import com.subidha.model.Department;
import com.subidha.model.User;
import com.subidha.service.DepartmentService;
import com.subidha.service.DoctorService; 
import com.subidha.service.UserService;
import com.subidha.util.ValidationUtil; 

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

public class UserManagementServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserService userService;
    private DepartmentService departmentService;
    private DoctorService doctorService; 

    @Override
    public void init() throws ServletException {
        userService = new UserService();
        departmentService = new DepartmentService();
        doctorService = new DoctorService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
       
      
        User loggedInUser = (session != null) ? (User) session.getAttribute("user") : null;

        if (loggedInUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        if(!"admin".equalsIgnoreCase(loggedInUser.getRole())) {
        	request.setAttribute("errorMessage", "Page access only to admin.");
        	
        	 RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/pages/error.jsp");
             dispatcher.forward(request, response);
            return;
        }

        String action = request.getParameter("action");
        String roleFilter = request.getParameter("role"); 
        String searchTerm = request.getParameter("search"); 
        String userIdParam = request.getParameter("userId"); 

        try {
         
            if ("edit".equals(action) && userIdParam != null) {
                int editUserId = Integer.parseInt(userIdParam);
                User userToEdit = userService.getUserById(editUserId);
                if (userToEdit != null) {
                    request.setAttribute("userToEdit", userToEdit);
                   
                    if ("doctor".equalsIgnoreCase(userToEdit.getRole())) {
                         Integer currentDeptId = doctorService.getDoctorDepartmentId(editUserId);
                         if (currentDeptId != null) {
                             request.setAttribute("doctorDepartmentId", currentDeptId);
                         }
                    }
                } else {
                    request.setAttribute("errorMessage", "User with ID " + editUserId + " not found.");
                }
            }

          
            List<User> userList = userService.getAllUsers(roleFilter, searchTerm);
            List<Department> departmentList = departmentService.getAllDepartments(); 

            request.setAttribute("userList", userList);
            request.setAttribute("departmentList", departmentList);
            request.setAttribute("currentRoleFilter", roleFilter != null ? roleFilter : "all");
            request.setAttribute("currentSearchQuery", searchTerm != null ? searchTerm : "");

        } catch (NumberFormatException e) {
             System.err.println("Invalid User ID format: " + userIdParam);
             request.setAttribute("errorMessage", "Invalid user ID specified.");
        } catch (Exception e) {
             System.err.println("Error in UserManagementServlet doGet: " + e.getMessage());
             e.printStackTrace();
             request.setAttribute("errorMessage", "An error occurred while loading user data.");
        }

       
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/pages/admin/userManagement.jsp");
        dispatcher.forward(request, response);
    }

     @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         HttpSession session = request.getSession(false);
       
       
         User loggedInUser = (session != null) ? (User) session.getAttribute("user") : null;

         if (loggedInUser == null) {
             response.sendRedirect(request.getContextPath() + "/login");
             return;
         }
         if(!"admin".equalsIgnoreCase(loggedInUser.getRole())) {
         	request.setAttribute("errorMessage", "Page only access to admin.");
         	
         	 RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/pages/error.jsp");
              dispatcher.forward(request, response);
             return;
         }

         String action = request.getParameter("action");
         String redirectUrl = request.getContextPath() + "/userManagement"; 
         String successMessage = null;
         String errorMessage = null;

         try {
             switch (action != null ? action : "") { 
                 case "addDoctor":
                     handleAddDoctor(request);
                     successMessage = "Doctor added successfully.";
                     break;
                 case "updateUser":
                     handleUpdateUser(request);
                     successMessage = "User updated successfully.";
                     break;
                 case "deleteUser":
                     handleDeleteUser(request);
                     successMessage = "User deleted successfully.";
                     break;
                 default:
                     errorMessage = "Invalid action specified: " + action;
                     break;
             }
         
         } catch (Exception e) {
              System.err.println("User Management POST Error (General): " + e.getMessage());
              e.printStackTrace();
              errorMessage = "An unexpected error occurred: " + e.getMessage(); 
         }

         
         StringBuilder redirectParams = new StringBuilder();
         if (successMessage != null) {
            redirectParams.append("?successMessage=").append(java.net.URLEncoder.encode(successMessage, "UTF-8"));
         } else if (errorMessage != null) {
             redirectParams.append("?errorMessage=").append(java.net.URLEncoder.encode(errorMessage, "UTF-8"));
         }

        

         response.sendRedirect(redirectUrl + redirectParams.toString());
    }

   

    private void handleAddDoctor(HttpServletRequest request) {
         String firstName = request.getParameter("firstName");
         String lastName = request.getParameter("lastName");
         String username = request.getParameter("username"); 
         String password = request.getParameter("password");
         String gender = request.getParameter("gender");
         String phoneNumber = request.getParameter("phoneNumber");
         String departmentIdStr = request.getParameter("departmentId");

      
         if (ValidationUtil.isNullOrEmpty(firstName) || ValidationUtil.isNullOrEmpty(lastName) ||
             ValidationUtil.isNullOrEmpty(username) || ValidationUtil.isNullOrEmpty(password) ||
             ValidationUtil.isNullOrEmpty(gender) || ValidationUtil.isNullOrEmpty(phoneNumber) ||
             ValidationUtil.isNullOrEmpty(departmentIdStr)) { 
             throw new IllegalArgumentException("All fields (including Department) are required for adding a doctor.");
         }
        
          if (!ValidationUtil.isValidPassword(password)) {
               throw new IllegalArgumentException("Password does not meet minimum requirements.");
          }
          if (!ValidationUtil.isValidPhoneNumber(phoneNumber)) {
              throw new IllegalArgumentException("Invalid phone number format.");
          }
         if (userService.isUsernameTaken(username)) {
              throw new IllegalArgumentException("Username '" + username + "' is already taken.");
         }

         Integer departmentId = null;
         try {
             departmentId = Integer.parseInt(departmentIdStr.trim());
             if (departmentId <= 0) throw new NumberFormatException(); 
         } catch (NumberFormatException e) {
             throw new IllegalArgumentException("Invalid Department ID selected.");
         }

        
         User newUser = new User();
         newUser.setFirstName(firstName);
         newUser.setLastName(lastName);
         newUser.setUsername(username);
         newUser.setPassword(password); 
         newUser.setGender(gender);
         newUser.setPhoneNumber(phoneNumber);
         newUser.setRole("doctor"); 

        
         boolean success = userService.registerUser(newUser, departmentId);
         if (!success) {
             throw new RuntimeException("Failed to add doctor. Check server logs for details.");
         }
    }

     private void handleUpdateUser(HttpServletRequest request) {
         int userId = Integer.parseInt(request.getParameter("userId")); 
         String firstName = request.getParameter("firstName");
         String lastName = request.getParameter("lastName");
        
         String gender = request.getParameter("gender");
         String phoneNumber = request.getParameter("phoneNumber");
         String newPassword = request.getParameter("password"); 
         String departmentIdStr = request.getParameter("departmentId"); 

        
         if (ValidationUtil.isNullOrEmpty(firstName) || ValidationUtil.isNullOrEmpty(lastName) ||
              ValidationUtil.isNullOrEmpty(gender) || ValidationUtil.isNullOrEmpty(phoneNumber)) {
             throw new IllegalArgumentException("First name, last name, gender, and phone number are required.");
         }
         if (!ValidationUtil.isValidPhoneNumber(phoneNumber)) {
             throw new IllegalArgumentException("Invalid phone number format.");
         }
        
         if (!ValidationUtil.isNullOrEmpty(newPassword) && !ValidationUtil.isValidPassword(newPassword)) {
             throw new IllegalArgumentException("New password does not meet minimum requirements.");
         }

        
         User userToUpdate = userService.getUserById(userId); 
         if (userToUpdate == null) {
             throw new IllegalArgumentException("User with ID " + userId + " not found for update.");
         }
         userToUpdate.setFirstName(firstName);
         userToUpdate.setLastName(lastName);
         userToUpdate.setGender(gender);
         userToUpdate.setPhoneNumber(phoneNumber);
        

         
         Integer departmentId = null;
         if ("doctor".equalsIgnoreCase(userToUpdate.getRole())) {
             if (ValidationUtil.isNullOrEmpty(departmentIdStr)) {
                  throw new IllegalArgumentException("Department is required for doctors.");
             }
             try {
                 departmentId = Integer.parseInt(departmentIdStr.trim());
                 if (departmentId <= 0) throw new NumberFormatException();
             } catch (NumberFormatException e) {
                 throw new IllegalArgumentException("Invalid Department ID selected for doctor.");
             }
         }

         
         boolean success = userService.updateUser(userToUpdate, departmentId, newPassword);
         if (!success) {
              throw new RuntimeException("Failed to update user. Check server logs.");
         }
    }

     private void handleDeleteUser(HttpServletRequest request) {
         int userId = Integer.parseInt(request.getParameter("userId"));

          
         HttpSession session = request.getSession(false);
         User loggedInUser = (User) session.getAttribute("user");
         if (loggedInUser != null && loggedInUser.getUserId() == userId) {
             throw new IllegalArgumentException("You cannot delete your own account.");
         }

         boolean success = userService.deleteUser(userId);
         if (!success) {
             throw new RuntimeException("Failed to delete user. The user might have related records or may not exist.");
         }
    }
}