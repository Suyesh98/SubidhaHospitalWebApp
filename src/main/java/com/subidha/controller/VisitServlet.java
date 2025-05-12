package com.subidha.controller;

import com.subidha.model.Doctor;
import com.subidha.model.User;
import com.subidha.model.Visit;
import com.subidha.service.DoctorService;
import com.subidha.service.VisitService;
import com.subidha.util.ValidationUtil;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import java.util.List;
import java.util.ArrayList;

public class VisitServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private VisitService visitService;
    private DoctorService doctorService;

    @Override
    public void init() throws ServletException {
        visitService = new VisitService();
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

        if (!"doctor".equalsIgnoreCase(loggedInUser.getRole())) {
            request.setAttribute("errorMessage", "You are not authorized to access this page.");
            request.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(request, response);
            return;
        }

        Doctor doctorProfile = doctorService.getDoctorDetailsByUserId(loggedInUser.getUserId());
        if (doctorProfile == null) {
            request.setAttribute("errorMessage", "Doctor profile not found. Cannot fetch visits.");
            request.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(request, response);
            return;
        }
        request.setAttribute("loggedInDoctorUser", loggedInUser);
        request.setAttribute("doctorProfile", doctorProfile);

        String action = request.getParameter("action");
        List<Visit> visitList = new ArrayList<>();

        try {
          
            if (action == null || action.isEmpty() || "listDoctorVisits".equals(action)) {
                String searchTerm = request.getParameter("searchTerm");
                String statusFilter = request.getParameter("statusFilter");
                if (statusFilter == null || statusFilter.isEmpty()) {
                    statusFilter = "all";
                }

                visitList = visitService.getVisitsByDoctorId(doctorProfile.getDoctorId(), searchTerm, statusFilter);
                request.setAttribute("visitList", visitList);
                request.setAttribute("searchTerm", searchTerm);
                request.setAttribute("statusFilter", statusFilter);

                RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/pages/doctor/doctor_visits.jsp");
                dispatcher.forward(request, response);
            } else {
                request.setAttribute("errorMessage", "Invalid action specified for visits.");
                request.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(request, response);
            }
        } catch (Exception e) { 
            System.err.println("Error in VisitServlet doGet (Doctor View): " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred while loading your visit data.");
          
            request.setAttribute("visitList", visitList);
            request.setAttribute("loggedInDoctorUser", loggedInUser);
            request.setAttribute("doctorProfile", doctorProfile);
          
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/pages/doctor/doctor_visits.jsp"); 
            dispatcher.forward(request, response);
        }
    }

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User loggedInUser = (session != null) ? (User) session.getAttribute("user") : null;

        if (loggedInUser == null || !"doctor".equalsIgnoreCase(loggedInUser.getRole())) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Login required or not authorized.");
            return;
        }

        Doctor doctorProfile = doctorService.getDoctorDetailsByUserId(loggedInUser.getUserId());
        if (doctorProfile == null) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Doctor profile not found.");
            return;
        }

        String action = request.getParameter("action");
        String successMessage = null;
        String errorMessage = null;
        String redirectToListUrl = request.getContextPath() + "/doctorVisits?action=listDoctorVisits";
        StringBuilder redirectParams = new StringBuilder();

        String currentSearchTerm = request.getParameter("currentSearchTerm");
        String currentStatusFilter = request.getParameter("currentStatusFilter");

        if (currentSearchTerm != null && !currentSearchTerm.isEmpty()) {
            redirectParams.append("&searchTerm=").append(java.net.URLEncoder.encode(currentSearchTerm, "UTF-8"));
        }
        if (currentStatusFilter != null && !currentStatusFilter.isEmpty() && !"all".equalsIgnoreCase(currentStatusFilter)) {
            redirectParams.append("&statusFilter=").append(java.net.URLEncoder.encode(currentStatusFilter, "UTF-8"));
        }

        try {
            if ("updateVisitStatusByDoctor".equals(action)) {
                
                handleUpdateStatus(request, doctorProfile.getDoctorId()); 
                successMessage = "Visit status updated successfully.";

            } else if ("deleteVisitByDoctor".equals(action)) {
                handleDeleteVisit(request, doctorProfile.getDoctorId());
                successMessage = "Visit deleted successfully.";
            } else {
                errorMessage = "Invalid POST action specified for visits: " + action;
            }
        } catch (java.sql.SQLException e) { 
            errorMessage = "A database error occurred: " + e.getMessage();
            System.err.println("VisitServlet POST Error (Database): " + e.getMessage());
            e.printStackTrace();
        } catch (IllegalArgumentException | SecurityException e) {
            errorMessage = e.getMessage();
            System.err.println("VisitServlet POST Error (Validation/Security): " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) { 
            errorMessage = "An unexpected error occurred: " + e.getMessage();
            System.err.println("VisitServlet POST Error (General): " + e.getMessage());
            e.printStackTrace();
        }

        if (successMessage != null) {
            redirectParams.append("&successMessage=").append(java.net.URLEncoder.encode(successMessage, "UTF-8"));
        }
        if (errorMessage != null) {
            redirectParams.append("&errorMessage=").append(java.net.URLEncoder.encode(errorMessage, "UTF-8"));
        }
        
        String finalRedirectUrl = redirectToListUrl;
        if (redirectParams.length() > 0) {
            if (finalRedirectUrl.contains("?")) {
                 finalRedirectUrl += redirectParams.toString();
            } else {
                finalRedirectUrl += "?" + redirectParams.substring(1);
            }
        }
        response.sendRedirect(finalRedirectUrl);
    }

    
    private void handleUpdateStatus(HttpServletRequest request, int currentDoctorId) throws java.sql.SQLException, IllegalArgumentException, SecurityException {
        int visitId = Integer.parseInt(request.getParameter("visitId"));
        String newStatus = request.getParameter("status");

        if (ValidationUtil.isNullOrEmpty(newStatus) ||
            !List.of("Pending", "Confirmed", "Cancelled", "Completed").contains(newStatus)) {
            throw new IllegalArgumentException("Invalid status value: " + newStatus);
        }

      

        boolean updated = visitService.updateVisitStatus(visitId, newStatus); 
        if (!updated) {
            
            throw new RuntimeException("Failed to update visit status for ID " + visitId + ". It might not exist or status unchanged.");
        }
    }

    private void handleDeleteVisit(HttpServletRequest request, int currentDoctorId) throws java.sql.SQLException, SecurityException {
        int visitId = Integer.parseInt(request.getParameter("visitId"));
       
        boolean deleted = visitService.deleteVisit(visitId); 
        if (!deleted) {
            throw new RuntimeException("Failed to delete visit ID " + visitId + ". It might not exist.");
        }
    }
}