<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="com.subidha.model.User" %>

<%
    User loggedInUser = (User) session.getAttribute("user");
    pageContext.setAttribute("loggedInUser", loggedInUser);

    String activePageParam = request.getParameter("activePage");
   
    String activePageAttr = (String) request.getAttribute("activePage");

    String activePage = "";
    if (activePageAttr != null && !activePageAttr.isEmpty()) {
        activePage = activePageAttr;
    } else if (activePageParam != null && !activePageParam.isEmpty()) {
        activePage = activePageParam;
    }
    pageContext.setAttribute("activePage", activePage);
%>

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/navigation.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">

<header class="navbar">
    <div class="navbar-container">
        <a href="${pageContext.request.contextPath}/home" class="navbar-logo-link" title="Subidha Polyclinic Home">
            <img src="${pageContext.request.contextPath}/images/logo.png" alt="Subidha Polyclinic Logo" class="navbar-logo">
        </a>

        <input type="checkbox" id="menu-toggle" class="menu-toggle-checkbox">
        <label for="menu-toggle" class="mobile-menu-toggle" aria-label="Toggle menu">
            <i class="fas fa-bars menu-icon-open"></i>
            <i class="fas fa-times menu-icon-close"></i>
        </label>

        <nav class="navbar-nav">
            <ul>
              

                <%-- General links for logged-in users, often related to homepage sections --%>
                <c:if test="${not empty loggedInUser}">
                   

                    <%-- Role-specific navigation links --%>
                    <c:choose>
                        <c:when test="${loggedInUser.role == 'admin'}">
                            <li><a href="${pageContext.request.contextPath}/admin" class="${activePage == 'adminDashboard' ? 'active' : ''}">Dashboard</a></li>
                            <li><a href="${pageContext.request.contextPath}/userManagement" class="${activePage == 'userManagement' ? 'active' : ''}">User Mgt.</a></li>
                            <li><a href="${pageContext.request.contextPath}/visitManagement" class="${activePage == 'visitManagement' ? 'active' : ''}">Visit Mgt.</a></li>
                            <li><a href="${pageContext.request.contextPath}/departmentManagement" class="${activePage == 'departmentManagement' ? 'active' : ''}">Dept. Mgt.</a></li>
                        </c:when>
                        <c:when test="${loggedInUser.role == 'doctor'}">
                            <li><a href="${pageContext.request.contextPath}/doctor" class="${activePage == 'doctorDashboard' ? 'active' : ''}">Dashboard</a></li>
                            <li><a href="${pageContext.request.contextPath}/doctorVisits" class="${activePage == 'doctorVisits' ? 'active' : ''}">My Visits</a></li>
                           
                        </c:when>
                        <c:when test="${loggedInUser.role == 'patient'}">
                            <%-- Patient specific links --%>
                              <li><a href="${pageContext.request.contextPath}/home" class="${activePage == 'home' ? 'active' : ''}">Home</a></li>
                             <li><a href="${pageContext.request.contextPath}/services" class="${activePage == 'services' ? 'active' : ''}">Services</a></li>
                    <li><a href="${pageContext.request.contextPath}/about_us" class="${activePage == 'about_us' ? 'active' : ''}">About Us</a></li>
                    <li><a href="${pageContext.request.contextPath}/contact_us" class="${activePage == 'contact_us' ? 'active' : ''}">Contact Us</a></li>
                            <li><a href="${pageContext.request.contextPath}/booking" class="${activePage == 'booking' ? 'active' : ''}">Book Visit</a></li>
                          
                          
                        </c:when>
                    </c:choose>
                </c:if>

             
            </ul>
        </nav>

        <div class="navbar-actions">
            <c:choose>
                <c:when test="${not empty loggedInUser}">
                    <a href="${pageContext.request.contextPath}/updateProfile" class="navbar-profile-link ${activePage == 'profile' ? 'active' : ''}" title="Update Your Profile">
                        <i class="fas fa-user-circle"></i>
                        <span><c:out value="${loggedInUser.firstName != null ? loggedInUser.firstName : loggedInUser.username}"/></span>
                    </a>
                    <form action="${pageContext.request.contextPath}/logout" method="post" style="display: inline;">
                        <button type="submit" class="logout-button" title="Log Out">
                            <i class="fas fa-sign-out-alt logout-icon"></i>
                            <span class="logout-text">Logout</span>
                        </button>
                    </form>
                </c:when>
                <c:otherwise>
                     <a href="${pageContext.request.contextPath}/login" class="button button-secondary">Login</a>
                     <a href="${pageContext.request.contextPath}/register" class="button button-primary">Register</a>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</header>