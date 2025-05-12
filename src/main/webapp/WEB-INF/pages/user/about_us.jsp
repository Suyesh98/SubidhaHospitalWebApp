<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>About Us - Subidha Polyclinic</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/about_us.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>

    <jsp:include page="/WEB-INF/pages/navigation.jsp">
        <jsp:param name="activePage" value="about_us" />
    </jsp:include>

    <main>
        <section class="hero-section">
            <div class="hero-container">
                <div class="hero-content">
                    <h1>About Us</h1>
                    <p class="tagline">Learn more about Subidha Polyclinic and our commitment to your health.</p>
                </div>
                <div class="hero-image-placeholder"></div>
            </div>
        </section>

        <section class="about-polyclinic-section">
            <div class="about-polyclinic-container container">
                <span class="section-tagline">SUBIDHA POLYCLINIC</span>
                <h2 class="section-title">Committed to Your Well-being</h2>
                <p class="section-subtitle">Subidha Polyclinic is dedicated to providing comprehensive and compassionate healthcare services to the community.</p>

                <div class="about-polyclinic-content">
                    <p>Founded in 1998, Subidha Polyclinic has a long-standing tradition of delivering exceptional medical care. We are committed to providing personalized treatment plans and fostering a supportive environment for our patients. Our experienced team of doctors, nurses, and staff are passionate about helping you achieve optimal health.</p>
                    <p>We strive to stay at the forefront of medical advancements, offering the latest diagnostic and treatment options.  Our goal is to make healthcare accessible and affordable for everyone in the community.</p>
                </div>
            </div>
        </section>

        <section class="doctors-section">
            <div class="doctors-container container">
                <span class="section-tagline">OUR TEAM</span>
                <h2 class="section-title">Meet Our Doctors</h2>
                <div class="doctor-cards">
                    <div class="doctor-card">
                        <img src="${pageContext.request.contextPath}/images/Ritika Image.jpg" alt="Dr. Ritika Neupane" class="doctor-image">
                        <h3>Dr. Ritika Neupane</h3>
                        <p class="specialty">GYNECOLOGIST</p>
                        <p class="description">With over a decade of experience, Dr. Ritika is the resident expert on general gynecology and women's health.</p>
                    </div>

                    <div class="doctor-card">
                        <img src="${pageContext.request.contextPath}/images/Suyesh Image.jpg" alt="Dr. Suyesh Neupane" class="doctor-image">
                        <h3>Dr. Suyesh Neupane</h3>
                        <p class="specialty">CARDIOLOGIST</p>
                        <p class="description">As the senior doctor in Subidha Hospital, Dr. Suyesh specializes in cardiothoracic surgery and cardiac care.</p>
                    </div>

                    <div class="doctor-card">
                        <img src="${pageContext.request.contextPath}/images/Snehal Image.jpg" alt="Dr. Snehal Subedi" class="doctor-image">
                        <h3>Dr. Snehal Subedi</h3>
                        <p class="specialty">ORTHOPEDIC SPECIALIST</p>
                        <p class="description">Dr. Snehal has over 15 years of experience as an orthopedic specialist.</p>
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