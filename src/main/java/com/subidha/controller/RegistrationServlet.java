package com.subidha.controller;

import com.subidha.service.UserService;
import com.subidha.model.User;
import com.subidha.util.ValidationUtil; 

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class RegistrationServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = new UserService(); 
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
     
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/pages/register.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String username = request.getParameter("username"); 
        String gender = request.getParameter("gender");
        String phoneNumber = request.getParameter("phoneNumber");
        String password = request.getParameter("password");

        String forwardPage = "/WEB-INF/pages/register.jsp";

     
        try {
            if (ValidationUtil.isNullOrEmpty(firstName) || ValidationUtil.isNullOrEmpty(lastName) ||
                ValidationUtil.isNullOrEmpty(username) || ValidationUtil.isNullOrEmpty(password) ||
                ValidationUtil.isNullOrEmpty(gender) || ValidationUtil.isNullOrEmpty(phoneNumber)) {
                throw new IllegalArgumentException("All fields are required.");
            }
          
            if (!ValidationUtil.isValidPassword(password)) { 
                throw new IllegalArgumentException("Password must be at least 6 characters long."); 
            }
            if (!ValidationUtil.isValidPhoneNumber(phoneNumber)) {
                throw new IllegalArgumentException("Enter a valid phone number.");
            }
            if (userService.isUsernameTaken(username)) {
                 throw new IllegalArgumentException("Registration failed. Username might already exist.");
            }

        
            User user = new User(); 
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setUsername(username);
            user.setGender(gender);
            user.setPhoneNumber(phoneNumber != null ? phoneNumber.trim() : null);
            user.setPassword(password); 
            user.setRole("patient");    

            
            System.out.println("Trying to register user: " + username + " with role: " + user.getRole());
          
            boolean isRegistered = userService.registerUser(user, null);

            if (isRegistered) {
                
                response.sendRedirect(request.getContextPath() + "/login?registrationSuccess=Registration+successful.+Proceed+to+login.");
            } else {
               
                 throw new RuntimeException("Registration failed for an unknown reason. Please try again.");
            }

        } catch (IllegalArgumentException e) {
            
            request.setAttribute("errorMessage", e.getMessage());
           
            request.getRequestDispatcher(forwardPage).forward(request, response);
        } catch (Exception e) {
             
             System.err.println("Registration Error: " + e.getMessage());
             e.printStackTrace();
             request.setAttribute("errorMessage", "An unexpected error occurred during registration. Please try again later.");
             request.getRequestDispatcher(forwardPage).forward(request, response);
        }
    }
}