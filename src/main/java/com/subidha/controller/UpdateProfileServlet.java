package com.subidha.controller;
import java.io.IOException; 

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.subidha.service.UserService;
import com.subidha.model.User;
import com.subidha.util.ValidationUtil;

public class UpdateProfileServlet extends HttpServlet {

   
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            // User is not logged in, redirect to login page
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute("user");

        // Get the updated information from the form
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String phoneNumber = request.getParameter("phoneNumber");
        String department = request.getParameter("department");
        
        // forward path
        String forwardPage = "/WEB-INF/pages/updateProfile.jsp";
        
        
        // Validate the input 
        
        if(ValidationUtil.isNullOrEmpty(firstName) || ValidationUtil.isNullOrEmpty(lastName) || 
        		ValidationUtil.isNullOrEmpty(phoneNumber) || ValidationUtil.isNullOrEmpty(department)) {
        	
        	request.setAttribute("user", user); 
            request.setAttribute("errorMessage", "All fields are required.");
            request.getRequestDispatcher(forwardPage).forward(request, response);
             return;
        }
        if(!ValidationUtil.isValidPhoneNumber(phoneNumber)) {
       	 request.setAttribute("errorMessage", "Enter a valid phone number.");
            request.getRequestDispatcher(forwardPage).forward(request, response);
            return; 
       }

        // Update the user object with the new information
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhoneNumber(phoneNumber);
        user.setDepartment(department);

        // Update the user information in the database
        UserService userDao = new UserService();
        boolean isUpdated = userDao.updateUser(user);

        if (isUpdated) {
            // Update the user object in the session 
            session.setAttribute("user", user);

            // Redirect back to the homepage with a success message
            response.sendRedirect(request.getContextPath() + "/updateProfile?successMessage=Profile+updated+successfully");
        } else {
            // Update failed
            request.setAttribute("errorMessage", "Profile update failed. Please try again.");
            request.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(request, response);
        }
    }
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    // forward the request to the login JSP page to display the form
	    RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/pages/updateProfile.jsp");
	    dispatcher.forward(request, response);
	}
	
}
