<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.subidha.model.User" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Update Profile - Subidha Polyclinic</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/updateProfile.css">
    <%-- fonts and icons --%>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>

    <jsp:include page="/WEB-INF/pages/navigation.jsp">
        <jsp:param name="activePage" value="profile" />
    </jsp:include>

    <%-- Main Content  --%>
    <main class="update-profile-main">
        <div class="update-profile-container">
            <div class="profile-card">
                <h1 class="card-title">Update Your Profile</h1>
                <p class="card-subtitle">
                    Keep your information up-to-date,
                    <c:out value="${loggedInUser.firstName}" />.
                </p>

                <%-- Display Success Message (from redirect parameter) --%>
                <c:if test="${not empty param.successMessage}">
                    <p class="message success-message">
                        <i class="fas fa-check-circle"></i> ${param.successMessage}
                    </p>
                </c:if>

                <%-- Display Error Message (from request attribute) --%>
                <c:if test="${not empty errorMessage}">
                    <p class="message error-message">
                        <i class="fas fa-exclamation-circle"></i> ${errorMessage}
                    </p>
                </c:if>

                <form action="${pageContext.request.contextPath}/updateProfile" method="post" class="profile-form">
                    <div class="input-group">
                        <label for="firstName">First Name</label>
                        <input type="text" id="firstName" name="firstName"
                               value="<c:out value="${not empty submittedUser ? submittedUser.firstName : loggedInUser.firstName}"/>" required>
                    </div>

                    <div class="input-group">
                        <label for="lastName">Last Name</label>
                        <input type="text" id="lastName" name="lastName"
                               value="<c:out value="${not empty submittedUser ? submittedUser.lastName : loggedInUser.lastName}"/>" required>
                    </div>

                    <div class="input-group">
                        <label for="phoneNumber">Phone Number</label>
                        <input type="tel" id="phoneNumber" name="phoneNumber"
                               value="<c:out value="${not empty submittedUser ? submittedUser.phoneNumber : loggedInUser.phoneNumber}"/>"
                               placeholder="e.g., 98********" required>
                    </div>

                    <%-- Patient Specific Fields --%>
                       <c:if test="${loggedInUser.role == 'patient'}">
                        <div class="input-group">
                            <label for="dateOfBirth">Date of Birth</label>
                            
                            <input type="date" id="dateOfBirth" name="dateOfBirth"
                                   value="<fmt:formatDate value='${patientDetails.dateOfBirth}' pattern='yyyy-MM-dd' />"
                                   max="<%= new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()) %>" 
                                   style="width: 100%; padding: 12px 15px; border: 1px solid var(--border-color); border-radius: 6px; background-color: var(--white-color); color: #495057; font-size: 0.95rem; font-family: inherit; transition: border-color 0.2s ease, box-shadow 0.2s ease; line-height: 1.5;"
                                   >
                        </div>

                        <div class="input-group full-width">
                            <label for="address">Address</label>
                            <textarea id="address" name="address" rows="3" placeholder="Enter your full address"
                                        style="resize: none; width: 100%; padding: 12px 15px; border: 1px solid var(--border-color); border-radius: 6px; background-color: var(--white-color); color: #495057; font-size: 0.95rem; font-family: inherit; transition: border-color 0.2s ease, box-shadow 0.2s ease; line-height: 1.5; min-height: 80px;"><c:out value="${not empty submittedAddress ? submittedAddress : patientDetails.address}"/></textarea>
                        </div>
                    </c:if>
                    <%-- End Patient Specific Fields --%>

                    <button type="submit" class="update-button">Save Changes</button>
                </form>

                <div class="form-links">
                    <c:choose>
                        <c:when test="${loggedInUser.role == 'admin'}">
                            <a href="${pageContext.request.contextPath}/admin" class="back-link">
                                <i class="fas fa-arrow-left"></i> Back to Dashboard
                            </a>
                        </c:when>
                        <c:when test="${loggedInUser.role == 'doctor'}">
                            <a href="${pageContext.request.contextPath}/doctor" class="back-link">
                                <i class="fas fa-arrow-left"></i> Back to Dashboard
                            </a>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/home" class="back-link">
                                <i class="fas fa-arrow-left"></i> Back to Home
                            </a>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </main>

    <footer class="footer">
        <div class="footer-container">
            <p>
                © <%= new java.util.GregorianCalendar().get(java.util.Calendar.YEAR) %> Subidha Polyclinic. All rights reserved.
            </p>
        </div>
    </footer>

</body>
</html>