<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.subidha.model.User"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
// Check if user is logged in
User user = (User) session.getAttribute("user");
if (user == null) {
	// If not logged in, redirect to login page
	response.sendRedirect("/login?message=Please login to update your profile.");
	return;
}

pageContext.setAttribute("loggedInUser", user);
%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Update Profile - Subidha Polyclinic</title>

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/updateProfile.css">

<%-- fonts and icons --%>
<link
	href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap"
	rel="stylesheet">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
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
					<c:out value="${loggedInUser.firstName}" />
					.
				</p>

				<%-- Display Success Message --%>
				<c:if test="${not empty param.successMessage}">
					<p class="message success-message">
						<i class="fas fa-check-circle"></i> ${param.successMessage}
					</p>
				</c:if>

				<%-- Display Error Message --%>
				<c:if test="${not empty errorMessage}">
					<p class="message error-message">
						<i class="fas fa-exclamation-circle"></i> ${errorMessage}
					</p>
				</c:if>

				<form action="updateProfile" method="post" class="profile-form">
					<div class="input-group">
						<label for="firstName">First Name</label> <input type="text"
							id="firstName" name="firstName"
							value="<c:out value="${loggedInUser.firstName}"/>" required>
					</div>

					<div class="input-group">
						<label for="lastName">Last Name</label> <input type="text"
							id="lastName" name="lastName"
							value="<c:out value="${loggedInUser.lastName}"/>" required>
					</div>

					<div class="input-group">
						<label for="phoneNumber">Phone Number</label> <input type="tel"
							id="phoneNumber" name="phoneNumber"
							value="<c:out value="${loggedInUser.phoneNumber}"/>"
							placeholder="e.g., +1234567890">
					</div>

					<div class="input-group">
						<label for="department">Department</label> <input type="text"
							id="department" name="department"
							value="<c:out value="${loggedInUser.department}"/>"
							placeholder="e.g., Cardiology, Orthopedics">
					</div>



					<button type="submit" class="update-button">Save Changes</button>
				</form>

				<div class="form-links">
					<c:choose>
						<c:when test="${loggedInUser.role == 'admin'}">
							<a href="${pageContext.request.contextPath}/admin"
								class="back-link"> <i class="fas fa-arrow-left"></i> Back to
								Dashboard
							</a>
						</c:when>
						<c:otherwise>
							<a href="${pageContext.request.contextPath}/home"
								class="back-link"> <i class="fas fa-arrow-left"></i> Back to
								Home
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
				©
				<%=new java.util.GregorianCalendar().get(java.util.Calendar.YEAR)%>
				Subidha Polyclinic. All rights reserved.
			</p>
		</div>
	</footer>

</body>
</html>