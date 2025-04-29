
package com.subidha.controller;


 import jakarta.servlet.ServletException;
 import jakarta.servlet.http.HttpServlet;
 import jakarta.servlet.http.HttpServletRequest;
 import jakarta.servlet.http.HttpServletResponse;
 import jakarta.servlet.http.HttpSession;

import com.subidha.model.User;

import java.io.IOException;

public class LogoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false); // Get session without creating one

        if (session != null) {
        	User user = (User) session.getAttribute("user");
        	String username = (user != null) ? user.getUsername() : null;
            System.out.println("Logging out user: " + (username != null ? username : "Unknown"));
            session.invalidate(); // Invalidate the session
        } else {
             System.out.println("Logout request received, but no active session found.");
        }

        // Redirect to the login page with a logout message
        response.sendRedirect(request.getContextPath() + "/login?logout=success");
    }
    
    
 // Handle POST requests the same way as GET 
     @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp); 
    }
}