<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.subidha.model.User" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<%
    //  Security Check
    User loggedInUser = (User) session.getAttribute("user");

    // Check if logged in
    if (loggedInUser == null) {
        response.sendRedirect("/login.jsp?message=Please login to access this page.");
        return;
    }

    // Check if the logged-in user has the 'admin' role 
    if (!"admin".equalsIgnoreCase(loggedInUser.getRole())) {
        // Not an admin, redirect to home or an error page
        response.sendRedirect("/home?message=Access Denied: You do not have permission.");
        
        return;
    }

    // User is an admin, proceed. Make user easily accessible via JSTL
    pageContext.setAttribute("adminUser", loggedInUser);
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - Subidha Polyclinic</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css"> <%-- Link to specific admin CSS --%>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>

   <jsp:include page="/WEB-INF/pages/navigation.jsp">
    <jsp:param name="activePage" value="admin"/>
</jsp:include>

    <%-- Main Admin Content  --%>
    <main class="admin-main">
        <div class="admin-container">
            <h1 class="dashboard-title">Admin Dashboard</h1>
            <p class="welcome-message">Welcome back, Admin <c:out value="${adminUser.firstName}"/>!</p>

            <%-- Dashboard Grid --%>
            <div class="dashboard-grid">

                <%-- User Management --%>
                <div class="dashboard-card user-management-card" id="users">
                    <div class="card-icon"><i class="fas fa-users-cog"></i></div>
                    <h2 class="card-title">User Management</h2>
                    <p class="card-description">View, add, or manage user accounts and roles.</p>
                    <div class="card-actions">
                        <a href="#" class="action-link">View All Users</a> 
                        <a href="#" class="action-link">Add New User</a>
                    </div>
                </div>

                <%-- Appointment Overview  --%>
                <div class="dashboard-card appointment-card">
                     <div class="card-icon"><i class="fas fa-calendar-check"></i></div>
                    <h2 class="card-title">Appointments</h2>
                    <p class="card-description">Overview of scheduled appointments.</p>
                     <div class="card-stats">
                         <span>Today: <strong>15</strong></span>
                         <span>Pending: <strong>8</strong></span>
                     </div>
                    <div class="card-actions">
                        <a href="#" class="action-link">View Calendar</a>
                    </div>
                </div>

                <%--  System Settings (Example) --%>
                 <div class="dashboard-card settings-card" id="settings">
                    <div class="card-icon"><i class="fas fa-cogs"></i></div>
                    <h2 class="card-title">System Settings</h2>
                    <p class="card-description">Configure application parameters.</p>
                    <div class="card-actions">
                         <a href="#" class="action-link">General Settings</a>
                    </div>
                </div>

                 <%--  Admin Info --%>
                 <div class="dashboard-card admin-info-card">
                    <div class="card-icon"><i class="fas fa-id-badge"></i></div>
                    <h2 class="card-title">Your Information</h2>
                    <ul class="info-list">
                        <li><strong>Username:</strong> <c:out value="${adminUser.username}"/></li>
                        <li><strong>Name:</strong> <c:out value="${adminUser.firstName} ${adminUser.lastName}"/></li>
                        <li><strong>Role:</strong> <span class="role-badge"><c:out value="${adminUser.role}"/></span></li>
                       
                    </ul>
                     <div class="card-actions">
                         <a href="${pageContext.request.contextPath}/updateProfile" class="action-link">Update Your Profile</a> 
                     </div>
                 </div>

            </div> 

        </div>
    </main>

   
    <footer class="footer">
        <div class="footer-container">
            <p>© <%= new java.util.GregorianCalendar().get(java.util.Calendar.YEAR) %> Subidha Polyclinic. All rights reserved. | Admin Area</p>
        </div>
    </footer>


</body>
</html>