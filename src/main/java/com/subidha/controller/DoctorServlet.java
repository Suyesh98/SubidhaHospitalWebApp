package com.subidha.controller;

import com.subidha.model.Doctor;
import com.subidha.model.User;

import com.subidha.service.DoctorService;
import com.subidha.service.VisitService; 

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

public class DoctorServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private DoctorService doctorService;
    private VisitService visitService; 

    @Override
    public void init() throws ServletException {
        doctorService = new DoctorService();
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

        if (!"doctor".equalsIgnoreCase(loggedInUser.getRole())) {
            request.setAttribute("errorMessage", "You are not authorized to access this page.");
            request.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(request, response);
            return;
        }

        Doctor doctorProfile = doctorService.getDoctorDetailsByUserId(loggedInUser.getUserId());
        if (doctorProfile == null) {
            request.setAttribute("errorMessage", "Doctor-specific profile information not found. Please contact administrator.");
            request.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(request, response);
            return;
        }

        request.setAttribute("loggedInDoctorUser", loggedInUser);
        request.setAttribute("doctorProfile", doctorProfile);

        try {
       
            request.setAttribute("totalVisitsForDoctor", visitService.getTotalVisitCountForDoctor(doctorProfile.getDoctorId()));
            request.setAttribute("pendingVisitsForDoctor", visitService.getPendingVisitCountForDoctor(doctorProfile.getDoctorId()));
            request.setAttribute("confirmedVisitsForDoctor", visitService.getConfirmedVisitCountForDoctor(doctorProfile.getDoctorId()));
            request.setAttribute("completedVisitsForDoctor", visitService.getCompletedVisitCountForDoctor(doctorProfile.getDoctorId()));
            request.setAttribute("cancelledVisitsForDoctor", visitService.getCancelledVisitCountForDoctor(doctorProfile.getDoctorId()));

        } catch (SQLException e) {
            System.err.println("DoctorServlet SQL Error (loading dashboard stats): " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMessage", "A database error occurred while loading dashboard statistics.");
           
             request.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(request, response);
            return;
        }

        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/pages/doctor/doctor.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       
        HttpSession session = request.getSession(false);
        User loggedInUser = (session != null) ? (User) session.getAttribute("user") : null;

        if (loggedInUser == null || !"doctor".equalsIgnoreCase(loggedInUser.getRole())) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "You are not authorized for this action.");
            return;
        }
       
        response.sendRedirect(request.getContextPath() + "/doctor");
    }
}