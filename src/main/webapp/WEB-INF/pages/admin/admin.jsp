<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.subidha.model.User" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<%
   
    User loggedInUser = (User) session.getAttribute("user");

   
    pageContext.setAttribute("adminUser", loggedInUser);
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - Subidha Polyclinic</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css"> 
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    
    
      <style>
    .card-stats {
        margin-bottom: 20px;
        font-size: 0.9rem;
        color: var(--medium-text);
    }
    .card-stats span {
        margin-right: 15px;
    }
    .card-stats strong {
        color: var(--dark-text);
        font-weight: 600;
    }
  </style>
</head>
</head>
<body>

   <jsp:include page="/WEB-INF/pages/navigation.jsp">
    <jsp:param name="activePage" value="adminDashboard"/>
</jsp:include>

    <%-- Main Admin Content  --%>
    <main class="admin-main">
        <div class="admin-container">
            <h1 class="dashboard-title">Admin Dashboard</h1>
            <p class="welcome-message">Welcome back, Admin <c:out value="${adminUser.firstName}"/>!</p>

            
            <div class="dashboard-grid">

                <%-- User Management --%>
                <div class="dashboard-card user-management-card" id="users">
                    <div class="card-icon"><i class="fas fa-users-cog"></i></div>
                    <h2 class="card-title">User Management</h2>
                    <p class="card-description">View, add, or manage user accounts and roles.</p>
                     <div class="card-stats">
                         <span>Total: <strong><c:out value="${totalUsers}"/></strong></span>
                         <span>Doctors: <strong><c:out value="${totalDoctors}"/></strong></span>
                         <span>Patients: <strong><c:out value="${totalPatients}"/></strong></span>
                     </div>
                    <div class="card-actions">
                        
                        <a href="${pageContext.request.contextPath}/userManagement" class="action-link">Manage Users</a>
                    </div>
                </div>

                <%-- Visti management  --%>
                 
                <div class="dashboard-card appointment-card">
                     <div class="card-icon"><i class="fas fa-calendar-check"></i></div>
                    <h2 class="card-title">Appointments</h2>
                    <p class="card-description">Overview of scheduled appointments.</p>
                     <div class="card-stats">
                         <span>Total: <strong><c:out value="${totalVisits}"/></strong></span>
                         <span>Pending: <strong><c:out value="${pendingVisits}"/></strong></span>
                     </div>
                    <div class="card-actions">
                        <a href="${pageContext.request.contextPath}/visitManagement" class="action-link">Manage Appointments</a>
                    </div>
                </div>

                <%--  Department Management --%>
                 <div class="dashboard-card settings-card" id="settings">
                    <div class="card-icon"><i class="fas fa-cogs"></i></div>
                    <h2 class="card-title">Department Management</h2>
                    <p class="card-description">Manage the departments.</p>
                     <div class="card-stats">
                         <span>Total: <strong><c:out value="${totalDepartments}"/></strong></span>
                     </div>
                    <div class="card-actions">
                          <a href="${pageContext.request.contextPath}/departmentManagement" class="action-link">Manage Departments</a>
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