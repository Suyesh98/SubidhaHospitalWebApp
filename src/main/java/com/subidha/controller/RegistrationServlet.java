
package com.subidha.controller;

import com.subidha.service.UserService;  
import com.subidha.model.User;
import com.subidha.util.ValidationUtil;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class RegistrationServlet extends HttpServlet {
	
    
	private static final long serialVersionUID = 1L;
	
	 @Override
	    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	        // Forward to the registration page to display the form
	        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/pages/register.jsp");
	        dispatcher.forward(request, response);
	    }
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String username = request.getParameter("username");
        String visitDateStr = request.getParameter("visitDate");
        String gender = request.getParameter("gender");
        String phoneNumber = request.getParameter("phoneNumber");
        String department = request.getParameter("department");
        String password = request.getParameter("password");
        
        
        String forwardPage = "/WEB-INF/pages/register.jsp";

        // Validate the input data
       
        if(ValidationUtil.isNullOrEmpty(firstName) || ValidationUtil.isNullOrEmpty(lastName) || ValidationUtil.isNullOrEmpty(username) ||
        		ValidationUtil.isNullOrEmpty(password)|| ValidationUtil.isNullOrEmpty(visitDateStr) || ValidationUtil.isNullOrEmpty(gender) || 
        		ValidationUtil.isNullOrEmpty(phoneNumber) || ValidationUtil.isNullOrEmpty(department)) {
        	 request.setAttribute("errorMessage", "All fields are required.");
             request.getRequestDispatcher(forwardPage).forward(request, response);
             return;
        }
        if (!ValidationUtil.isValidPassword(password)) {
            request.setAttribute("errorMessage", "Password must be at least 8 characters long.");
            request.getRequestDispatcher(forwardPage).forward(request, response);
            return; 
        }
        if(!ValidationUtil.isValidPhoneNumber(phoneNumber)) {
        	 request.setAttribute("errorMessage", "Enter a vlaid phone number.");
             request.getRequestDispatcher(forwardPage).forward(request, response);
             return; 
        }

        Date visitDate = null;
        if (visitDateStr != null && !visitDateStr.isEmpty()) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
                visitDate = dateFormat.parse(visitDateStr);
            } catch (ParseException e) {
                request.setAttribute("errorMessage", "Invalid date format. Please use yyyy-MM-dd.");
                request.getRequestDispatcher(forwardPage).forward(request, response);
                return;
            }
        }

        // Create a User object
        User user = new User(firstName, lastName, username, visitDate, gender, phoneNumber, department, password);

        // Register the user using the UserDao
        UserService userDao = new UserService();
        boolean isRegistered = userDao.registerUser(user);

        
        	if (isRegistered) {
        	    response.sendRedirect(request.getContextPath() + "/login?"
        	    		+ "registrationSuccess=Registration+successful.+Proceed+to+login.");
        	}

         else {
            // Registration failed, display an error message
            request.setAttribute("errorMessage", "Registration failed. Username might already exist.");
            request.getRequestDispatcher("registration.jsp").forward(request, response);
        }
    }
}