<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.subidha.model.User, com.subidha.model.Visit, java.util.List" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%

    List<Visit> visitList = (List<Visit>) request.getAttribute("visitList");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Visit Management - Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/visitManagement.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>

    <jsp:include page="/WEB-INF/pages/navigation.jsp">
        <jsp:param name="activePage" value="visitManagement" />
    </jsp:include>

    <main>
        <div class="container">
            <div class="management-header">
                <h1 class="management-title">Visit Management</h1>
                <a href="${pageContext.request.contextPath}/admin" class="btn btn-info"><i class="fas fa-tachometer-alt"></i> Dashboard</a>
            </div>

            <c:if test="${not empty param.successMessage}">
                <p class="message success-message"><i class="fas fa-check-circle"></i> ${param.successMessage}</p>
            </c:if>
            <c:if test="${not empty param.errorMessage}">
                <p class="message error-message"><i class="fas fa-exclamation-circle"></i> ${param.errorMessage}</p>
            </c:if>

            <section class="table-section">
               
                        <form action="${pageContext.request.contextPath}/visitManagement" method="get" class="table-controls">
                         <div>
                            <label for="searchInput">Search</label>
                            <input type="text" id="searchInput" name="searchTerm" placeholder="Search by doctor, department, reason..." value="${not empty searchTerm ? searchTerm : ''}">
                        </div>
                         <div>
                            <label for="statusFilter">Filter by Status</label>
                            <select id="statusFilter" name="statusFilter">
                                <option value="all" ${statusFilter == 'all' ? 'selected' : ''}>All Statuses</option>
                                <option value="Pending" ${param.statusFilter == 'Pending' ? 'selected' : ''}>Pending</option>
                                <option value="Confirmed" ${param.statusFilter == 'Confirmed' ? 'selected' : ''}>Confirmed</option>
                                <option value="Cancelled" ${param.statusFilter == 'Cancelled' ? 'selected' : ''}>Cancelled</option>
                                <option value="Completed" ${param.statusFilter == 'Completed' ? 'selected' : ''}>Completed</option>
                            </select>
                        </div>
                         <button type="submit" class="filter-button">
                            <i class="fas fa-filter"></i> Filter / Search
                        </button>
                    </form>
                 

                <h2>Existing Visits</h2>
                <c:choose>
                    <c:when test="${not empty visitList}">
                        <table class="management-table">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Patient Name</th>
                                    <th>Doctor Name</th>
                                    <th>Department</th>
                                    <th>Visit Date</th>
                                    <th>Visit Time</th>
                                    <th>Reason</th>
                                    <th>Status</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="visit" items="${visitList}">
                                    <tr>
                                        <td data-label="ID">${visit.visitId}</td>
                                        <td data-label="Patient Name"><c:out value="${visit.patientName}"/></td>
                                        <td data-label="Doctor Name"><c:out value="${visit.doctorName}"/></td>
                                        <td data-label="Department"><c:out value="${visit.departmentName}"/></td>
                                        <td data-label="Visit Date"><fmt:formatDate value="${visit.visitDate}" pattern="yyyy-MM-dd"/></td>
                                        <td data-label="Visit Time"><fmt:formatDate value="${visit.visitTime}" pattern="HH:mm"/></td>
                                        <td data-label="Reason"><c:out value="${visit.reason}"/></td>
                                        <td data-label="Status">
                                            <form action="visitManagement" method="POST" style="display: inline;">
                                                 <input type="hidden" name="action" value="updateStatus">
                                                 <input type="hidden" name="visitId" value="${visit.visitId}">
                                                 <select name="status" onchange="this.form.submit()">
                                                     <option value="Pending" ${visit.status == 'Pending' ? 'selected' : ''}>Pending</option>
                                                     <option value="Confirmed" ${visit.status == 'Confirmed' ? 'selected' : ''}>Confirmed</option>
                                                     <option value="Cancelled" ${visit.status == 'Cancelled' ? 'selected' : ''}>Cancelled</option>
                                                     <option value="Completed" ${visit.status == 'Completed' ? 'selected' : ''}>Completed</option>
                                                 </select>
                                             </form>
                                        </td>
                                        <td data-label="Actions" class="actions-cell">
                                            <form action="visitManagement" method="POST" style="display: inline;" onsubmit="return confirm('Are you sure you want to delete this visit record?');">
                                                <input type="hidden" name="action" value="delete">
                                                <input type="hidden" name="visitId" value="${visit.visitId}">
                                                <button type="submit" class="btn btn-danger btn-sm"><i class="fas fa-trash"></i> Delete</button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </c:when>
                    <c:otherwise>
                        <p class="message info-message"><i class="fas fa-info-circle"></i> No visits found.</p>
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