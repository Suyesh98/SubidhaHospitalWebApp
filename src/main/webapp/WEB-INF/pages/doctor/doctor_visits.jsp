<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.subidha.model.User, com.subidha.model.Doctor, com.subidha.model.Visit, java.util.List" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>



<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Visits - Dr. <c:out value="${doctorProfile.fullName}"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/doctor_visits.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>

    <jsp:include page="/WEB-INF/pages/navigation.jsp">
        <jsp:param name="activePage" value="doctorVisits" />
    </jsp:include>

    <main>
        <div class="container">
            <div class="management-header">
                <h1 class="management-title">My Scheduled Visits</h1>
                <a href="${pageContext.request.contextPath}/doctor" class="btn btn-info"><i class="fas fa-tachometer-alt"></i> Dashboard</a>
            </div>

            <c:if test="${not empty param.successMessage}">
                <p class="message success-message"><i class="fas fa-check-circle"></i> <c:out value="${param.successMessage}"/></p>
            </c:if>
            <c:if test="${not empty param.errorMessage}">
                <p class="message error-message"><i class="fas fa-exclamation-circle"></i> <c:out value="${param.errorMessage}"/></p>
            </c:if>

            <section class="table-section">
                <form action="${pageContext.request.contextPath}/doctorVisits" method="get" class="table-controls">
                    <input type="hidden" name="action" value="listDoctorVisits">
                    <div>
                        <label for="searchInput">Search by Patient or Reason</label>
                        <input type="text" id="searchInput" name="searchTerm" value="<c:out value="${searchTerm}"/>" placeholder="E.g., John Doe, Checkup...">
                    </div>
                    <div>
                        <label for="statusFilter">Filter by Status</label>
                        <select id="statusFilter" name="statusFilter">
                            <option value="all" ${statusFilter == 'all' or empty statusFilter ? 'selected' : ''}>All Statuses</option>
                            <option value="Pending" ${statusFilter == 'Pending' ? 'selected' : ''}>Pending</option>
                            <option value="Confirmed" ${statusFilter == 'Confirmed' ? 'selected' : ''}>Confirmed</option>
                            <option value="Completed" ${statusFilter == 'Completed' ? 'selected' : ''}>Completed</option>
                            <option value="Cancelled" ${statusFilter == 'Cancelled' ? 'selected' : ''}>Cancelled</option>
                        </select>
                    </div>
                    <button type="submit" class="filter-button">
                        <i class="fas fa-filter"></i> Filter / Search
                    </button>
                </form>

                <h2>Your Visits</h2>
                <c:choose>
                    <c:when test="${not empty visitList}">
                        <table class="management-table">
                            <thead>
                                <tr>
                                    <th>Visit ID</th>
                                    <th>Patient Name</th>
                                    <th>Visit Date</th>
                                    <th>Visit Time</th>
                                    <th>Reason</th>
                                    <th>Current Status</th>
                                    <th>Update Status</th>
                                    <th>Action</th>
                                 
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="visit" items="${visitList}">
                                    <tr>
                                        <td data-label="Visit ID">${visit.visitId}</td>
                                        <td data-label="Patient Name"><c:out value="${visit.patientName}"/></td>
                                        <td data-label="Visit Date"><fmt:formatDate value="${visit.visitDate}" pattern="yyyy-MM-dd"/></td>
                                        <td data-label="Visit Time"><fmt:formatDate value="${visit.visitTime}" pattern="HH:mm"/></td>
                                        <td data-label="Reason"><c:out value="${fn:escapeXml(visit.reason)}"/></td>
                                        <td data-label="Current Status">
                                            <span class="status status-${fn:toLowerCase(visit.status)}"><c:out value="${visit.status}"/></span>
                                        </td>
                                        <td data-label="Update Status">
                                            <form action="${pageContext.request.contextPath}/doctorVisits" method="POST" class="update-status-form">
                                                <input type="hidden" name="action" value="updateVisitStatusByDoctor">
                                                <input type="hidden" name="visitId" value="${visit.visitId}">
                                             
                                                <input type="hidden" name="currentSearchTerm" value="<c:out value="${searchTerm}"/>">
                                                <input type="hidden" name="currentStatusFilter" value="<c:out value="${statusFilter}"/>">

                                                <select name="status" onchange="this.form.submit()" class="form-control-sm">
                                                    <option value="Pending" ${visit.status == 'Pending' ? 'selected' : ''}>Pending</option>
                                                    <option value="Confirmed" ${visit.status == 'Confirmed' ? 'selected' : ''}>Confirmed</option>
                                                    <option value="Completed" ${visit.status == 'Completed' ? 'selected' : ''}>Completed</option>
                                                    <option value="Cancelled" ${visit.status == 'Cancelled' ? 'selected' : ''}>Cancelled</option>
                                                </select>
                                            </form>
                                        </td>
                                     
                                        
                                        <td data-label="Actions" class="actions-cell">
                                            <form action="${pageContext.request.contextPath}/doctorVisits" method="POST" style="display: inline;" onsubmit="return confirm('Are you sure you want to delete this visit record?');">
                                                <input type="hidden" name="action" value="deleteVisitByDoctor">
                                                <input type="hidden" name="visitId" value="${visit.visitId}">
                                                <input type="hidden" name="currentSearchTerm" value="<c:out value="${searchTerm}"/>">
                                                <input type="hidden" name="currentStatusFilter" value="<c:out value="${statusFilter}"/>">
                                                <button type="submit" class="btn btn-danger btn-sm"><i class="fas fa-trash"></i></button>
                                            </form>
                                        </td>
                                        
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </c:when>
                    <c:otherwise>
                        <p class="message info-message"><i class="fas fa-info-circle"></i> No visits found matching your criteria.</p>
                    </c:otherwise>
                </c:choose>
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