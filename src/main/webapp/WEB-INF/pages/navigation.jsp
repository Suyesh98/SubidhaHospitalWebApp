<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="com.subidha.model.User" %>

<%-- ========= Navigation Bar Component ========= --%>
<%-- This component displays the main site navigation. --%>
<%-- It requires the 'User' object in the session attribute named 'user'. --%>
<%-- It accepts an optional request parameter 'activePage' to highlight the current nav item. --%>
<%-- Possible values for activePage: 'home', 'services', 'about', 'contact', 'admin', 'profile', 'login', 'register' --%>

<%
    // Get the user from session, okay if it's null (logged out)
    User loggedInUser = (User) session.getAttribute("user");
    pageContext.setAttribute("loggedInUser", loggedInUser); // Make available to JSTL

    // Get the active page parameter (default to empty string if not provided)
    String activePage = request.getParameter("activePage");
    if (activePage == null) {
        activePage = ""; // Avoid null pointer later
    }
    pageContext.setAttribute("activePage", activePage); // Make available to JSTL
%>

<%-- Link to the common navigation stylesheet --%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/navigation.css">
<%-- Include Font Awesome if not already included globally --%>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">

<header class="navbar">
    <div class="navbar-container">
        <%-- Logo --%>
        <a href="${pageContext.request.contextPath}/home" class="navbar-logo-link" title="Subidha Polyclinic Home">
            <img src="${pageContext.request.contextPath}/images/logo.png" alt="Subidha Polyclinic Logo" class="navbar-logo">
            <%-- Optional: Add Brand Text if needed --%>
            <%-- <span class="navbar-brand-text">Subidha Polyclinic</span> --%>
        </a>

        <%-- Mobile Menu Toggle Checkbox (hidden) --%>
        <input type="checkbox" id="menu-toggle" class="menu-toggle-checkbox">
        <label for="menu-toggle" class="mobile-menu-toggle" aria-label="Toggle menu">
            <i class="fas fa-bars menu-icon-open"></i>
            <i class="fas fa-times menu-icon-close"></i>
        </label>

        <%-- Navigation Links --%>
        <nav class="navbar-nav">
            <ul>
                <li><a href="${pageContext.request.contextPath}/home" class="${activePage == 'home' ? 'active' : ''}">Home</a></li>
                <%-- Only show these section links if logged in and likely on the home page context --%>
                <c:if test="${not empty loggedInUser}">
                    <li><a href="${pageContext.request.contextPath}/home#services" class="${activePage == 'services' ? 'active' : ''}">Services</a></li>
                    <li><a href="${pageContext.request.contextPath}/home#about" class="${activePage == 'about' ? 'active' : ''}">About Us</a></li>
                    <li><a href="${pageContext.request.contextPath}/home#contact" class="${activePage == 'contact' ? 'active' : ''}">Contact Us</a></li>
                </c:if>

                <%-- Admin specific link --%>
                <c:if test="${not empty loggedInUser && loggedInUser.role == 'admin'}">
                    <li><a href="${pageContext.request.contextPath}/admin" class="${activePage == 'admin' ? 'active' : ''}">Admin Dashboard</a></li>
                </c:if>

                <%-- Links for Logged Out Users --%>
                <c:if test="${empty loggedInUser}">
                     <li><a href="${pageContext.request.contextPath}/login" class="${activePage == 'login' ? 'active' : ''}">Login</a></li>
                     <li><a href="${pageContext.request.contextPath}/register" class="${activePage == 'register' ? 'active' : ''}">Register</a></li>
                </c:if>
            </ul>
        </nav>

        <%-- User Actions (Login/Register or Profile/Logout) --%>
        <div class="navbar-actions">
            <c:choose>
                <c:when test="${not empty loggedInUser}">
                    <%-- Logged In User Actions --%>
                    <a href="${pageContext.request.contextPath}/updateProfile" class="navbar-profile-link ${activePage == 'profile' ? 'active' : ''}" title="Update Your Profile">
                        <i class="fas fa-user-circle"></i>
                        <span><c:out value="${loggedInUser.firstName != null ? loggedInUser.firstName : loggedInUser.username}"/></span> <%-- Show First Name if available, else Username --%>
                    </a>
                    <form action="logout" method="post" style="display: inline;">
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