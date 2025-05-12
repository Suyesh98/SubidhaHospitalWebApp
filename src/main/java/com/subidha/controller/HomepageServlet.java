package com.subidha.controller;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


import com.subidha.model.User;

public class HomepageServlet extends HttpServlet {

    
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);  

        if (session != null && session.getAttribute("user") != null) {
         
            User user = (User) session.getAttribute("user");
            request.setAttribute("user", user);  
            
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/pages/home.jsp");
            dispatcher.forward(request, response);
        } else {
         
            response.sendRedirect(request.getContextPath() + "/login");
        }
    }
	
	// Handle POST requests the same way as GET
	 @Override
	    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	        doGet(req, resp);
	    }
}