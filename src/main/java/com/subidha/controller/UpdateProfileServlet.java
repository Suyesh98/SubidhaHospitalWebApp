package com.subidha.controller;

import com.subidha.service.PatientService;
import com.subidha.service.UserService;
import com.subidha.model.User;
import com.subidha.model.Patient;
import com.subidha.util.ValidationUtil;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class UpdateProfileServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private UserService userService;
    private PatientService patientService;

    @Override
    public void init() throws ServletException {
        userService = new UserService();
        patientService = new PatientService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User loggedInUserFromSession = (session != null) ? (User) session.getAttribute("user") : null; 

        if (loggedInUserFromSession == null) {
            response.sendRedirect(request.getContextPath() + "/login?message=Please+login+to+access+this+page.");
            return;
        }

     
        request.setAttribute("loggedInUser", loggedInUserFromSession);


        if ("patient".equalsIgnoreCase(loggedInUserFromSession.getRole())) {
            Patient patientDetails = patientService.getPatientByUserId(loggedInUserFromSession.getUserId());
            if (patientDetails != null) {
                request.setAttribute("patientDetails", patientDetails);
                System.out.println("Patient DOB in doGet: " + patientDetails.getDateOfBirth());
            } else {
                System.err.println("Warning: User " + loggedInUserFromSession.getUsername() + " (ID: " + loggedInUserFromSession.getUserId() + ") with role 'patient' has no corresponding patient record.");
                request.setAttribute("patientDetails", new Patient()); 
            }
        }
        
        String successMessage = request.getParameter("successMessage");
        String errorMessageFromRedirect = request.getParameter("errorMessage");
        
        if (successMessage != null) {
            request.setAttribute("successMessage", successMessage);
        }
        if (errorMessageFromRedirect != null) {
            request.setAttribute("errorMessage", errorMessageFromRedirect);
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/pages/updateProfile.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login?message=Please+login+to+update+profile");
            return;
        }

        User userFromSession = (User) session.getAttribute("user");

        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String phoneNumber = request.getParameter("phoneNumber");

        String dobStr = request.getParameter("dateOfBirth");
        String address = request.getParameter("address");

        List<String> errors = new ArrayList<>();
        String forwardPage = "/WEB-INF/pages/updateProfile.jsp";

        if (ValidationUtil.isNullOrEmpty(firstName)) errors.add("First Name is required.");
        if (ValidationUtil.isNullOrEmpty(lastName)) errors.add("Last Name is required.");
        if (ValidationUtil.isNullOrEmpty(phoneNumber)) errors.add("Phone Number is required.");
        else if (!ValidationUtil.isValidPhoneNumber(phoneNumber)) errors.add("Enter a valid phone number.");

        Date newDobSql = null;
        if ("patient".equalsIgnoreCase(userFromSession.getRole())) {
            if (dobStr != null && !dobStr.isEmpty()) {
                try {
                    newDobSql = Date.valueOf(dobStr);
                    if (newDobSql.after(new java.util.Date())) {
                        errors.add("Date of Birth cannot be in the future.");
                    }
                } catch (IllegalArgumentException e) {
                    errors.add("Invalid Date of Birth format. Use YYYY-MM-DD.");
                }
            }
        }

        if (!errors.isEmpty()) {
            request.setAttribute("errorMessage", String.join(" ", errors));
            
            User submittedUserData = new User();
            submittedUserData.setUserId(userFromSession.getUserId()); 
            submittedUserData.setUsername(userFromSession.getUsername());
            submittedUserData.setRole(userFromSession.getRole());
            submittedUserData.setFirstName(firstName);
            submittedUserData.setLastName(lastName);
            submittedUserData.setPhoneNumber(phoneNumber);
            submittedUserData.setGender(userFromSession.getGender());
            submittedUserData.setDepartmentName(userFromSession.getDepartmentName()); 
            request.setAttribute("submittedUser", submittedUserData); 

            if ("patient".equalsIgnoreCase(userFromSession.getRole())) {
                 Patient currentPatientDetails = patientService.getPatientByUserId(userFromSession.getUserId());
                 request.setAttribute("patientDetails", currentPatientDetails); 
                 if (dobStr != null) request.setAttribute("submittedDob", dobStr);
                 if (address != null) request.setAttribute("submittedAddress", address);
            }
            
            request.setAttribute("loggedInUser", userFromSession);
            request.getRequestDispatcher(forwardPage).forward(request, response);
            return;
        }

        boolean userDetailsUpdated = false;
        boolean patientDetailsUpdatedOverall = true;

        
        User userToUpdate = new User();
        userToUpdate.setUserId(userFromSession.getUserId());
        userToUpdate.setUsername(userFromSession.getUsername()); 
        userToUpdate.setRole(userFromSession.getRole());       
        userToUpdate.setGender(userFromSession.getGender());  
        userToUpdate.setDepartmentName(userFromSession.getDepartmentName()); 
        userToUpdate.setPasswordHash(userFromSession.getPasswordHash()); 

        userToUpdate.setFirstName(firstName);
        userToUpdate.setLastName(lastName);
        userToUpdate.setPhoneNumber(phoneNumber);


        try {
          
            userDetailsUpdated = userService.updateUser(userToUpdate, null, null); 

            if (userDetailsUpdated) {
             
                userFromSession.setFirstName(userToUpdate.getFirstName());
                userFromSession.setLastName(userToUpdate.getLastName());
                userFromSession.setPhoneNumber(userToUpdate.getPhoneNumber());
                session.setAttribute("user", userFromSession);


                if ("patient".equalsIgnoreCase(userFromSession.getRole())) {
                    Patient currentPatient = patientService.getPatientByUserId(userFromSession.getUserId());
                    if (currentPatient != null) {
                        boolean patientSpecificFieldsUpdated = patientService.updatePatientDetails(currentPatient.getPatientId(), newDobSql, address);
                        if (!patientSpecificFieldsUpdated) {
                         
                            errors.add("Failed to update address or date of birth specific details. Ensure values are correct or contact support if the problem persists.");
                            patientDetailsUpdatedOverall = false;
                        }
                    } else {
                        errors.add("Patient record not found for update. Please contact support.");
                        patientDetailsUpdatedOverall = false;
                        System.err.println("CRITICAL ERROR: User " + userFromSession.getUsername() + " (ID: " + userFromSession.getUserId() + ") has role 'patient' but NO corresponding patient record during update.");
                    }
                }
            } else {
                errors.add("Profile update failed for general user details. The information was not saved.");
            }

            if (errors.isEmpty() && userDetailsUpdated && patientDetailsUpdatedOverall) {
                String successMsg = "Profile updated successfully!";
                response.sendRedirect(request.getContextPath() + "/updateProfile?successMessage=" + java.net.URLEncoder.encode(successMsg, "UTF-8"));
            } else {
                if (!userDetailsUpdated && errors.isEmpty()) { 
                    errors.add("Failed to update user details. No specific error message from service, but the changes were not saved.");
                }
                request.setAttribute("errorMessage", String.join(" ", errors));
                
                User submittedUserDataOnError = new User();
                submittedUserDataOnError.setUserId(userFromSession.getUserId());
                submittedUserDataOnError.setUsername(userFromSession.getUsername());
                submittedUserDataOnError.setRole(userFromSession.getRole());
                submittedUserDataOnError.setFirstName(firstName); 
                submittedUserDataOnError.setLastName(lastName);
                submittedUserDataOnError.setPhoneNumber(phoneNumber);
                submittedUserDataOnError.setGender(userFromSession.getGender());
                submittedUserDataOnError.setDepartmentName(userFromSession.getDepartmentName());
                request.setAttribute("submittedUser", submittedUserDataOnError);

                if ("patient".equalsIgnoreCase(userFromSession.getRole())) {
                    Patient currentPatientDetails = patientService.getPatientByUserId(userFromSession.getUserId());
                    request.setAttribute("patientDetails", currentPatientDetails); 
                     if (dobStr != null) request.setAttribute("submittedDob", dobStr); 
                     if (address != null) request.setAttribute("submittedAddress", address); 
                }
             
                request.setAttribute("loggedInUser", userFromSession);
                request.getRequestDispatcher(forwardPage).forward(request, response);
            }

        } catch (Exception e) {
            System.err.println("Update Profile Error: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMessage", "An unexpected error occurred during profile update: " + e.getMessage());
            
            User submittedUserDataOnException = new User();
            submittedUserDataOnException.setUserId(userFromSession.getUserId());
            submittedUserDataOnException.setUsername(userFromSession.getUsername());
            submittedUserDataOnException.setRole(userFromSession.getRole());
            submittedUserDataOnException.setFirstName(firstName);
            submittedUserDataOnException.setLastName(lastName);
            submittedUserDataOnException.setPhoneNumber(phoneNumber);
            submittedUserDataOnException.setGender(userFromSession.getGender());
            submittedUserDataOnException.setDepartmentName(userFromSession.getDepartmentName());
            request.setAttribute("submittedUser", submittedUserDataOnException);

            if ("patient".equalsIgnoreCase(userFromSession.getRole())) {
                 Patient currentPatientDetails = patientService.getPatientByUserId(userFromSession.getUserId());
                 request.setAttribute("patientDetails", currentPatientDetails);
                 if (dobStr != null) request.setAttribute("submittedDob", dobStr);
                 if (address != null) request.setAttribute("submittedAddress", address);
            }
        
            request.setAttribute("loggedInUser", userFromSession);
            request.getRequestDispatcher(forwardPage).forward(request, response);
        }
    }
}