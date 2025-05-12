<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.subidha.model.User, com.subidha.model.Doctor, java.util.List" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>



<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
   
    <title>Doctor Dashboard - Dr. <c:out value="${doctorProfile.fullName}"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/doctor.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>

    <jsp:include page="/WEB-INF/pages/navigation.jsp">
        <jsp:param name="activePage" value="doctorDashboard" />
    </jsp:include>

    <main>
        <div class="container">

            <c:if test="${not empty param.successMessage}">
                <p class="message success-message"><i class="fas fa-check-circle"></i> <c:out value="${param.successMessage}"/></p>
            </c:if>
            <c:if test="${not empty param.errorMessage}">
                <p class="message error-message"><i class="fas fa-exclamation-circle"></i> <c:out value="${param.errorMessage}"/></p>
            </c:if>

            <div class="dashboard-header">
              
                <h1>Welcome, Dr. <c:out value="${doctorProfile.firstName}"/> <c:out value="${doctorProfile.lastName}"/></h1>
                <p>This is your personal dashboard where you can manage your profile and patient visits.</p>
            </div>

            <section class="dashboard-cards">
                <div class="card profile-card">
                    <h2><i class="fas fa-user-md"></i> Your Profile</h2>
                 
                    <p><strong>Name:</strong> <c:out value="${doctorProfile.fullName}"/></p>
                 
                    <p><strong>Username:</strong> <c:out value="${loggedInDoctorUser.username}"/></p>
                    <p><strong>Phone:</strong> <c:out value="${not empty loggedInDoctorUser.phoneNumber ? loggedInDoctorUser.phoneNumber : 'N/A'}"/></p>
                 
                    <p><strong>Department:</strong> <c:out value="${not empty doctorProfile.departmentName ? doctorProfile.departmentName : 'Not Assigned'}"/></p>
                    <a href="${pageContext.request.contextPath}/updateProfile" class="btn btn-primary"><i class="fas fa-edit"></i> Edit Profile</a>
                </div>

                <div class="card visit-management-card">
                    <h2><i class="fas fa-calendar-check"></i> Manage Your Visits</h2>
                    <p>View upcoming appointments, update visit statuses, and manage your schedule efficiently.</p>
                    
                    <a href="${pageContext.request.contextPath}/doctorVisits" class="btn btn-primary"><i class="fas fa-arrow-right"></i> Go to My Visits</a>
                </div>
            </section>

            <section class="dashboard-stats">
                <h2>Your Visit Statistics</h2>
                <div class="stats-grid">
                    <div class="stat-item"><h3><c:out value="${totalVisitsForDoctor}"/></h3><p>Total Assigned</p></div>
                    <div class="stat-item"><h3><c:out value="${pendingVisitsForDoctor}"/></h3><p>Pending</p></div>
                    <div class="stat-item"><h3><c:out value="${confirmedVisitsForDoctor}"/></h3><p>Confirmed</p></div>
                    <div class="stat-item"><h3><c:out value="${completedVisitsForDoctor}"/></h3><p>Completed</p></div>
                    <div class="stat-item"><h3><c:out value="${cancelledVisitsForDoctor}"/></h3><p>Cancelled</p></div>
                </div>
            </section>

        </div> 
    </main>

    <footer class="footer">
        <div class="footer-container">
            <p>© <%=new java.util.GregorianCalendar().get(java.util.Calendar.YEAR)%> Subidha Polyclinic. All rights reserved.</p>
        </div>
    </footer>

</body>
</html>