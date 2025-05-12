<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Services - Subidha Polyclinic</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/services.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>

    <jsp:include page="/WEB-INF/pages/navigation.jsp">
        <jsp:param name="activePage" value="services" />
    </jsp:include>

    <main>
        <section class="hero-section">
            <div class="hero-container">
                <div class="hero-content">
                    <h1>Our Services</h1>
                    <p class="tagline">Explore the comprehensive healthcare services offered at Subidha Polyclinic.</p>
                </div>
                <div class="hero-image-placeholder"></div>
            </div>
        </section>

        <section class="services-section">
            <div class="services-container container">
                <span class="section-tagline">OUR SERVICES</span>
                <h2 class="section-title">Comprehensive Care Tailored For You</h2>
                <p class="section-subtitle">Subidha Polyclinic provides top-notch care across various health specialties to ensure your well-being.</p>
                <div class="service-cards-grid">
                    <div class="service-card">
                        <div class="service-icon-wrapper">
                            <i class="fas fa-stethoscope service-icon"></i>
                        </div>
                        <h3>General Consultation</h3>
                        <p>Comprehensive health assessments and personalized advice.</p>
                    </div>
                    <div class="service-card">
                        <div class="service-icon-wrapper">
                            <i class="fas fa-heartbeat service-icon"></i>
                        </div>
                        <h3>Cardiology</h3>
                        <p>Expert cardiac care and diagnostic services.</p>
                    </div>
                    <div class="service-card">
                        <div class="service-icon-wrapper">
                            <i class="fas fa-hospital-alt service-icon"></i>
                        </div>
                        <h3>Orthopedics</h3>
                        <p>Advanced treatments for bone and joint health.</p>
                    </div>
                    <div class="service-card">
                        <div class="service-icon-wrapper">
                            <i class="fas fa-female service-icon"></i>
                        </div>
                        <h3>Gynecology</h3>
                        <p>Specialized care for women's health needs.</p>
                    </div>
                    <div class="service-card">
                        <div class="service-icon-wrapper">
                            <i class="fas fa-lungs service-icon"></i>
                        </div>
                        <h3>Pulmonology</h3>
                        <p>Comprehensive respiratory and lung care.</p>
                    </div>
                    <div class="service-card">
                        <div class="service-icon-wrapper">
                            <i class="fas fa-brain service-icon"></i>
                        </div>
                        <h3>Neurology</h3>
                        <p>Advanced neurological diagnosis and treatment.</p>
                    </div>
                    <div class="service-card">
                        <div class="service-icon-wrapper">
                            <i class="fas fa-microscope service-icon"></i>
                        </div>
                        <h3>Laboratory Services</h3>
                        <p>Accurate and timely lab testing for informed decisions.</p>
                    </div>
                    <div class="service-card">
                        <div class="service-icon-wrapper">
                            <i class="fas fa-x-ray service-icon"></i>
                        </div>
                        <h3>Imaging & Radiology</h3>
                        <p>Advanced diagnostic imaging for accurate assessments.</p>
                    </div>
                </div>
            </div>
        </section>
    </main>

    <footer class="footer">
        <div class="footer-container">
            <p>© <%=new java.util.GregorianCalendar().get(java.util.Calendar.YEAR)%> Subidha Polyclinic. All rights reserved.</p>
        </div>
    </footer>

</body>
</html>