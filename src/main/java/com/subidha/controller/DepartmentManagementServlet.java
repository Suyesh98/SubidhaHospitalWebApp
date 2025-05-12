package com.subidha.controller;

import com.subidha.model.Department;
import com.subidha.model.User;
import com.subidha.service.DepartmentService;
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

public class DepartmentManagementServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private DepartmentService departmentService;

    @Override
    public void init() throws ServletException {
        departmentService = new DepartmentService();
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
        String departmentIdParam = request.getParameter("departmentId");

        try {
           
            if ("edit".equals(action) && departmentIdParam != null) {
                int deptId = Integer.parseInt(departmentIdParam);
                Department deptToEdit = departmentService.getDepartmentById(deptId);
                if (deptToEdit != null) {
                    request.setAttribute("deptToEdit", deptToEdit);
                } else {
                    request.setAttribute("errorMessage", "Department with ID " + deptId + " not found.");
                }
            }

           
            List<Department> departmentList = departmentService.getAllDepartments();
            request.setAttribute("departmentList", departmentList);

        } catch (NumberFormatException e) {
             System.err.println("Invalid Department ID format: " + departmentIdParam);
             request.setAttribute("errorMessage", "Invalid Department ID specified.");
        } catch (Exception e) {
             System.err.println("Error in DepartmentManagementServlet doGet: " + e.getMessage());
             e.printStackTrace();
             request.setAttribute("errorMessage", "An error occurred while loading department data.");
        }

        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/pages/admin/departmentManagement.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        
        if (session == null || session.getAttribute("user") == null || !"admin".equals(((User) session.getAttribute("user")).getRole())) {
            response.sendRedirect(request.getContextPath() + "/login?message=Access+Denied");
            return;
        }

        String action = request.getParameter("action");
        String redirectUrl = request.getContextPath() + "/departmentManagement"; 
        String successMessage = null;
        String errorMessage = null;

         try {
             switch (action != null ? action : "") {
                 case "add":
                     handleAddDepartment(request);
                     successMessage = "Department added successfully.";
                     break;
                 case "update":
                     handleUpdateDepartment(request);
                     successMessage = "Department updated successfully.";
                     break;
                 case "delete":
                     
                     handleDeleteDepartment(request);
                     successMessage = (String) request.getAttribute("successMessage"); 
                     errorMessage = (String) request.getAttribute("errorMessage");     
                     break;
                 default:
                     errorMessage = "Invalid action specified: " + action;
                     break;
             }
             
        }  catch (SQLException e) {
             System.err.println("Department Management POST Error (Database): " + e.getMessage());
             e.printStackTrace();
            
             if (e.getMessage().toLowerCase().contains("foreign key constraint fails")) {
                errorMessage = "Database error: Cannot perform action due to related records.";
             } else {
                 errorMessage = "A database error occurred: " + e.getMessage();
             }
        } catch (Exception e) {
             System.err.println("Department Management POST Error (General): " + e.getMessage());
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

    private void handleAddDepartment(HttpServletRequest request) throws SQLException {
        String deptName = request.getParameter("departmentName");
        if (ValidationUtil.isNullOrEmpty(deptName)) {
            throw new IllegalArgumentException("Department name cannot be empty.");
        }
        

        if (!departmentService.addDepartment(deptName.trim())) {
           
            throw new RuntimeException("Failed to add department (unexpected). Check server logs.");
        }
    }

    private void handleUpdateDepartment(HttpServletRequest request) throws SQLException {
        int deptId = Integer.parseInt(request.getParameter("departmentId")); 
        String deptName = request.getParameter("departmentName");
        if (ValidationUtil.isNullOrEmpty(deptName)) {
            throw new IllegalArgumentException("Department name cannot be empty.");
        }
      

        if (!departmentService.updateDepartment(deptId, deptName.trim())) {
             
             throw new RuntimeException("Failed to update department ID " + deptId + ". It might not exist.");
        }
    }

    private void handleDeleteDepartment(HttpServletRequest request) throws SQLException {
        int deptId = Integer.parseInt(request.getParameter("departmentId")); 

        if (departmentService.isDepartmentInUse(deptId)) {
              request.setAttribute("errorMessage", "Cannot delete department. It is currently assigned to one or more doctors.");
        } else {
             boolean deleted = departmentService.deleteDepartment(deptId); 
             if (deleted) {
                 request.setAttribute("successMessage", "Department deleted successfully.");
             } else {
                 
                 request.setAttribute("errorMessage", "Failed to delete department. It might have been deleted already or is still in use.");
             }
        }
    }
}