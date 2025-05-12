package com.subidha.controller;

import java.io.IOException;
import java.sql.SQLException;

import com.subidha.model.User;
import com.subidha.service.DepartmentService;
import com.subidha.service.DoctorService;
import com.subidha.service.UserService;
import com.subidha.service.VisitService;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class AdminServlet extends HttpServlet {

	   private VisitService visitService; 
	   private UserService userService;
	   private DepartmentService departmentService;
	   
	private static final long serialVersionUID = 1L;
	
	
	@Override
    public void init() throws ServletException {
        userService = new UserService();
        visitService = new VisitService();
        departmentService = new DepartmentService();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);  
        
        User loggedInUser = (session != null) ? (User) session.getAttribute("user") : null;

        if (loggedInUser == null) {
        	 response.sendRedirect(request.getContextPath()+"/login?message=Please login to access this page.");
            return;
        }
        if(!"admin".equalsIgnoreCase(loggedInUser.getRole())) {
        	request.setAttribute("errorMessage", "Page access only to admin.");
        	
        	 RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/pages/error.jsp");
             dispatcher.forward(request, response);
            return;
        }
        
        try {
            request.setAttribute("totalUsers", userService.getTotalUserCount());
            request.setAttribute("totalDoctors", userService.getTotalDoctorCount());
            request.setAttribute("totalPatients", userService.getTotalPatientCount());
            request.setAttribute("totalVisits", visitService.getTotalVisitCount() );
            request.setAttribute("pendingVisits", visitService.getPendingVisitCount());
            request.setAttribute("confirmedVisits", visitService.getConfirmedVisitCount());
            request.setAttribute("completedVisits", visitService.getCompletedVisitCount());
            request.setAttribute("cancelledVisits", visitService.getCancelledVisitCount());
            request.setAttribute("totalDepartments", departmentService.getTotalDepartmentCount());

        }
        catch (SQLException e) {
            System.err.println("AdminServlet SQL Error (loading dashboard stats): " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMessage", "A database error occurred while loading dashboard statistics.");
           
             request.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(request, response);
            return;
        }
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/pages/admin/admin.jsp");
        dispatcher.forward(request, response);
    }
	 
	
	 @Override
	    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	        doGet(req, resp);
	    }
}
