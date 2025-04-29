package com.subidha.controller;

import java.io.IOException;

import com.subidha.model.User;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class AdminServlet extends HttpServlet {

    
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);  

        if (session != null && session.getAttribute("user") != null) {
            // User is logged in
            User user = (User) session.getAttribute("user");
            request.setAttribute("user", user);  // Pass the user object to the JSP
            
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/pages/admin/admin.jsp");
            dispatcher.forward(request, response);
        } else {
            // User is not logged in, redirect to login page
            response.sendRedirect(request.getContextPath() + "/login");
        }
    }
	 
	// Handle POST requests the same way as GET
	 @Override
	    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	        doGet(req, resp);
	    }
}
