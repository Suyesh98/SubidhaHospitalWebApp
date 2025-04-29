
package com.subidha.controller;

import com.subidha.service.UserService; 
import com.subidha.model.User;
import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class LoginServlet extends HttpServlet {

   
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { 
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Validate input
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            request.setAttribute("errorMessage", "Username and password are required.");
            request.getRequestDispatcher("/WEB-INF/pages/login.jsp").forward(request, response);
            return;
        }

        UserService userDao = new UserService();
        User user = userDao.loginUser(username, password);
        System.out.println("user: " + user);
        System.out.println("checking if user is null.....");
        if (user != null) {
            // Login successful
            HttpSession session = request.getSession();
            System.out.println("role of user: " + user.getRole());
            session.setAttribute("user", user);  // Store the user object in the session
            if(user.getRole().equalsIgnoreCase("admin")) {
            	 // Redirect to the adminpage
                response.sendRedirect(request.getContextPath() + "/admin"); 
                return;
            }else {
            	 // Redirect to the homepage
                response.sendRedirect(request.getContextPath() + "/home"); 
                return;
            }

           
        } else {
        	 
            // Login failed
            request.setAttribute("errorMessage", "Invalid username or password.");
            request.getRequestDispatcher("/WEB-INF/pages/login.jsp").forward(request, response);
        }
    }
		
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    // forward the request to the login JSP page to display the form
	    RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/pages/login.jsp");
	    dispatcher.forward(request, response);
	}

}