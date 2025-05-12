package com.subidha.controller;

import com.subidha.model.Visit;
import com.subidha.model.User;
import com.subidha.service.VisitService;
import com.subidha.util.ValidationUtil;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class VisitManagementServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private VisitService visitService;

    @Override
    public void init() throws ServletException {
        visitService = new VisitService();
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
        String searchTerm = request.getParameter("searchTerm");
        String statusFilter = request.getParameter("statusFilter");
        List<Visit> visitList = null;

        try {
           visitList = visitService.getAllVisits(searchTerm, statusFilter);
           request.setAttribute("visitList", visitList);

        } catch (Exception e) {
            System.err.println("Error in VisitManagementServlet doGet: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred while loading visit data.");
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/pages/admin/visitManagement.jsp");
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
        	request.setAttribute("errorMessage", "Page access only to admin.");
        	
        	 RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/pages/error.jsp");
             dispatcher.forward(request, response);
            return;
        }

        String action = request.getParameter("action");
        String redirectUrl = request.getContextPath() + "/visitManagement";
        String successMessage = null;
        String errorMessage = null;

        try {
            switch (action != null ? action : "") {
                case "updateStatus":
                    handleUpdateStatus(request);
                    successMessage = "Visit status updated successfully.";
                    break;
                case "delete":
                    handleDeleteVisit(request);
                    successMessage = "Visit deleted successfully.";
                    break;
                default:
                    errorMessage = "Invalid action specified: " + action;
                    break;
            }
        } catch (SQLException e) {
            System.err.println("Visit Management POST Error (Database): " + e.getMessage());
            e.printStackTrace();

             errorMessage = "A database error occurred: " + e.getMessage();
        }catch (Exception e) {
            System.err.println("Visit Management POST Error (General): " + e.getMessage());
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

    private void handleUpdateStatus(HttpServletRequest request) throws SQLException {
        int visitId = Integer.parseInt(request.getParameter("visitId"));
        String newStatus = request.getParameter("status");

        if (ValidationUtil.isNullOrEmpty(newStatus)) {
            throw new IllegalArgumentException("Status cannot be empty.");
        }

        
        if (!newStatus.equals("Pending") && !newStatus.equals("Confirmed") && !newStatus.equals("Cancelled") && !newStatus.equals("Completed")) {
            throw new IllegalArgumentException("Invalid status value: " + newStatus);
        }

        
        boolean updated = visitService.updateVisitStatus(visitId, newStatus);

        if (!updated) {
            throw new RuntimeException("Failed to update visit status for ID " + visitId + ". It might not exist.");
        }
    }

    private void handleDeleteVisit(HttpServletRequest request) throws SQLException {
        int visitId = Integer.parseInt(request.getParameter("visitId"));

    
        boolean deleted = visitService.deleteVisit(visitId);

        if (!deleted) {
            throw new RuntimeException("Failed to delete visit ID " + visitId + ". It might not exist.");
        }
    }
    
    
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      

        HttpSession session = request.getSession(false);
        User loggedInUser = (session != null) ? (User) session.getAttribute("user") : null;

        if (loggedInUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        if(!"admin".equalsIgnoreCase(loggedInUser.getRole())) {
        	request.setAttribute("errorMessage", "Page access only to admin..");
        	
        	 RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/pages/error.jsp");
             dispatcher.forward(request, response);
            return;
        }

        if (request.getServletPath().equals("/admin")) {
            try {
                int totalVisits = visitService.getTotalVisitCount();
                int pendingVisits = visitService.getPendingVisitCount();

                request.setAttribute("totalVisits", totalVisits);
                request.setAttribute("pendingVisits", pendingVisits);

            } catch (SQLException e) {
                System.err.println("Error fetching visit counts: " + e.getMessage());
                e.printStackTrace();
                request.setAttribute("errorMessage", "Failed to load visit statistics.");
            }
        }
        super.service(request, response);
    }
}
