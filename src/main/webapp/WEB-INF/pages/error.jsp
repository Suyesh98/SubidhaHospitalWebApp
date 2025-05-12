<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isErrorPage="true" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
 
    Object errorMessageObj = request.getAttribute("errorMessage");
    String errorMessage = (errorMessageObj != null) ? errorMessageObj.toString() : null;

  
    if (errorMessage == null || errorMessage.trim().isEmpty()) {
        if (exception != null) {
           
             errorMessage = "An unexpected application issue occurred.";
           
        } else {
             errorMessage = "Page you are trying to access does not exist.";
        }
    }
    pageContext.setAttribute("displayErrorMessage", errorMessage); // Set for JSTL access

  
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error - Subidha Polyclinic</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/error.css"> 
   
   <%-- fonts and icons --%>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>

    <jsp:include page="/WEB-INF/pages/navigation.jsp">
    
     <jsp:param name="activePage" value="error"/>
</jsp:include>

    <%-- Main Error Content  --%>
    <main class="error-main">
        <div class="error-container">
            <div class="error-card">
                <div class="error-icon">
                    <i class="fas fa-exclamation-triangle"></i> 
                </div>
                <h1 class="error-title">Oops! Something Went Wrong</h1>
                <p class="error-text">
                    <c:out value="${displayErrorMessage}" /> 
                </p>
                <a href="${pageContext.request.contextPath}/home" class="back-link"><i class="fas fa-home"></i> Go Back to Homepage</a>
            </div>
        </div>
    </main>

    <%-- Footer  --%>
    <footer class="footer">
        <div class="footer-container">
            <p>© <%= new java.util.GregorianCalendar().get(java.util.Calendar.YEAR) %> Subidha Polyclinic. All rights reserved.</p>
        </div>
    </footer>

</body>
</html>