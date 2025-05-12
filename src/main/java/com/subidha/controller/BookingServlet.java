package com.subidha.controller;

import com.subidha.model.Doctor;
import com.subidha.model.Patient;
import com.subidha.model.User;
import com.subidha.model.Visit;
import com.subidha.service.DoctorService;
import com.subidha.service.PatientService;
import com.subidha.service.VisitService;
import com.subidha.util.ValidationUtil;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList; // Added for update logic
import java.util.List;

public class BookingServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private DoctorService doctorService;
    private PatientService patientService;
    private VisitService visitService;

    @Override
    public void init() throws ServletException {
        doctorService = new DoctorService();
        patientService = new PatientService();
        visitService = new VisitService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User loggedInUser = (session != null) ? (User) session.getAttribute("user") : null;

        if (loggedInUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        if(!"patient".equalsIgnoreCase(loggedInUser.getRole())) {
        	request.setAttribute("errorMessage", "Only patient can access booking page.");
        	
        	 RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/pages/error.jsp");
             dispatcher.forward(request, response);
            return;
        }
        
        
        // Fetch patient details using user ID
        Patient currentPatient = patientService.getPatientByUserId(loggedInUser.getUserId());
        
        
        if (currentPatient == null) {
             System.err.println("CRITICAL ERROR: Logged in user " + loggedInUser.getUsername() + " (ID: " + loggedInUser.getUserId() + ") has role 'patient' but NO corresponding record in 'patients' table.");
             request.setAttribute("message", "Patient profile issue. Contact support.");
             request.setAttribute("messageType", "error");
             RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/pages/home.jsp");
             dispatcher.forward(request, response);
             return;
        }
        
        
        String searchTerm = request.getParameter("searchTerm");
        String statusFilter = request.getParameter("statusFilter");
        
        String currentStatusFilter = (statusFilter == null) ? "all" : statusFilter;
        
      
        List<Doctor> doctors = doctorService.getAllDoctorsWithDetails();
        List<Visit> userVisits = visitService.getVisitsByPatientId(currentPatient.getPatientId(), searchTerm, statusFilter);

       
        request.setAttribute("user", loggedInUser);
        request.setAttribute("patient", currentPatient); 
        request.setAttribute("doctors", doctors);
        request.setAttribute("visits", userVisits);
        request.setAttribute("searchTerm", searchTerm);
        request.setAttribute("statusFilter", currentStatusFilter); 
        
      
        String successMessage = (String) session.getAttribute("successMessage");
        if (successMessage != null) {
             request.setAttribute("message", successMessage);
             request.setAttribute("messageType", "success");
             session.removeAttribute("successMessage");
        }
        String errorMessage = (String) session.getAttribute("errorMessage");
         if (errorMessage != null) {
             request.setAttribute("message", errorMessage);
             request.setAttribute("messageType", "error");
             session.removeAttribute("errorMessage");
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/pages/user/booking.jsp");
        dispatcher.forward(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        User loggedInUser = (session != null) ? (User) session.getAttribute("user") : null;

        if (loggedInUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        if(!"patient".equalsIgnoreCase(loggedInUser.getRole())) {
        	response.sendRedirect(request.getContextPath() + "/home");
            return;
        }

     
        Patient currentPatient = patientService.getPatientByUserId(loggedInUser.getUserId());
         if (currentPatient == null) {
             System.err.println("Booking POST failed: Patient record not found for user " + loggedInUser.getUsername());
             session.setAttribute("errorMessage", "Could not process booking: Patient record missing.");
             response.sendRedirect(request.getContextPath() + "/booking");
             return;
         }
         int patientId = currentPatient.getPatientId(); 

      
        String doctorIdStr = request.getParameter("doctorId");
        String dateStr = request.getParameter("preferredDate");
        String timeStr = request.getParameter("preferredTime");
        String reason = request.getParameter("reason");
        
        String dobStr = request.getParameter("dateOfBirth"); 
        String address = request.getParameter("address"); 

     
        List<String> errors = new ArrayList<>();
        if (ValidationUtil.isNullOrEmpty(doctorIdStr)) errors.add("Please select a doctor.");
        if (ValidationUtil.isNullOrEmpty(dateStr)) errors.add("Please select a preferred date.");
        if (ValidationUtil.isNullOrEmpty(timeStr)) errors.add("Please select a preferred time.");

       
         Date newDobSql = null;
         if (dobStr != null && !dobStr.isEmpty()) {
             try {
                 newDobSql = Date.valueOf(dobStr); 
                 
                 if (newDobSql.after(new java.util.Date())) {
                      errors.add("Date of Birth cannot be in the future.");
                 }
             } catch (IllegalArgumentException e) {
                 errors.add("Invalid Date of Birth format.");
             }
         }

      
          String newAddress = null;
         if (address != null && !address.trim().isEmpty()) {
             newAddress = address.trim();
            
         } else if (address != null && address.trim().isEmpty() && currentPatient.getAddress() == null) {
          
             errors.add("Please enter your address.");
         }


        int doctorId = 0;
        Date visitDate = null;
        Time visitTime = null;
        try {
            if (!ValidationUtil.isNullOrEmpty(doctorIdStr)) doctorId = Integer.parseInt(doctorIdStr);
            if (!ValidationUtil.isNullOrEmpty(dateStr)) visitDate = Date.valueOf(dateStr);
            if (!ValidationUtil.isNullOrEmpty(timeStr)) visitTime = Time.valueOf(timeStr + ":00");
        } catch (IllegalArgumentException e) {
             errors.add("Invalid date, time, or doctor selection format.");
        }

        if (!errors.isEmpty()) {
            session.setAttribute("errorMessage", String.join(" ", errors));
            response.sendRedirect(request.getContextPath() + "/booking");
            return;
        }


      
         boolean detailsUpdated = true;
     
         boolean needsUpdate = (newDobSql != null && (currentPatient.getDateOfBirth() == null || !currentPatient.getDateOfBirth().equals(newDobSql))) ||
                            (newAddress != null && (currentPatient.getAddress() == null || !currentPatient.getAddress().equals(newAddress)));

        if (needsUpdate) {
            System.out.println("DEBUG: Attempting to update patient details for patient ID: " + patientId);
           
             detailsUpdated = patientService.updatePatientDetails(patientId, newDobSql, newAddress);
        }

        if (!detailsUpdated) {
             System.err.println("Booking POST failed: Could not update patient details for patient ID: " + patientId);
             session.setAttribute("errorMessage", "Failed to update your profile details. Please try again.");
             response.sendRedirect(request.getContextPath() + "/booking");
             return;
        }
      


     
        Visit newVisit = new Visit();
        newVisit.setPatientId(patientId);
        newVisit.setDoctorId(doctorId);
        newVisit.setVisitDate(visitDate);
        newVisit.setVisitTime(visitTime);
        newVisit.setReason(reason);

        boolean booked = visitService.bookVisit(newVisit);

        if (booked) {
            session.setAttribute("successMessage", "Appointment requested successfully! Status is Pending." + (needsUpdate ? " Your profile was also updated." : ""));
        } else {
            session.setAttribute("errorMessage", "Failed to request appointment. Please try again later or contact support.");
        }

        response.sendRedirect(request.getContextPath() + "/booking");
    }
}