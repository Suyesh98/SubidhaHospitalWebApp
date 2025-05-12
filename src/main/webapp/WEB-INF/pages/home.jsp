<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>



<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Home - Subidha Polyclinic</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/home.css">

<%-- fonts and icons --%>
<link
	href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap"
	rel="stylesheet">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>

	<jsp:include page="/WEB-INF/pages/navigation.jsp">
		<jsp:param name="activePage" value="home" />
	</jsp:include>

	<main>

		<%--  Hero Section  --%>
		<section class="hero-section">
			<div class="hero-container">
				<div class="hero-content">
					<img src="${pageContext.request.contextPath}/images/logo.png"
						alt="Subidha Polyclinic Logo" class="hero-logo">
					<h1>Subidha Polyclinic</h1>
					<p class="tagline">Where people's health comes first</p>
					 <a class="cta-button" href="${pageContext.request.contextPath}/booking">Book a
                        Consultation</a>
				</div>
				<div class="hero-image-placeholder"></div>
			</div>
		</section>



		<%-- our services --%>
		<section id="services" class="services-section">
			<div class="services-container container">
				<span class="section-tagline">OUR SERVICES</span>
				<h2 class="section-title">Comprehensive Care Tailored For You</h2>
				<p class="section-subtitle">Subidha Polyclinic provides
					top-notch care across various health specialties to ensure your
					well-being.</p>
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


		<%-- meet our doctors section --%>
		<section class="doctors-section" id="about">
			<div class="doctors-container">
				<h2>Meet our Doctors</h2>
				<div class="doctor-cards">
					<%-- Doctor Card 1 --%>
					<div class="doctor-card">
						<img
							src="${pageContext.request.contextPath}/images/Ritika Image.jpg"
							alt="Dr. Ritika Neupane" class="doctor-image">
						<h3>Dr. Ritika Neupane</h3>
						<p class="specialty">GYNECOLOGIST</p>
						<p class="description">With over a decade of experience, Dr.
							Ritika is the resident expert on general gynecology and women's
							health.</p>
					</div>

					<%-- Doctor Card 2 --%>
					<div class="doctor-card">
						<img
							src="${pageContext.request.contextPath}/images/Suyesh Image.jpg"
							alt="Dr. Suyesh Neupane" class="doctor-image">
						<h3>Dr. Suyesh Neupane</h3>
						<p class="specialty">CARDIOLOGIST</p>
						<p class="description">As the senior doctor in Subidha
							Hospital, Dr. Suyesh specialises in cardiothoracic surgery and
							cardiac care.</p>
					</div>

					<%-- Doctor Card 3 --%>
					<div class="doctor-card">
						<img
							src="${pageContext.request.contextPath}/images/Snehal Image.jpg"
							alt="Dr. Snehal Subedi" class="doctor-image">
						<h3>Dr. Snehal Subedi</h3>
						<p class="specialty">ORTHOPEDIC SPECIALIST</p>
						<p class="description">Dr. Snehal has over 15 years of
							experience as an orthopedic specialist.</p>
					</div>

				</div>
			</div>
		</section>


		<%-- Contact Section --%>
		<section id="contact" class="contact-section">
			<div class="contact-overlay"></div>
			<div class="contact-container container">
				<div class="contact-content-left">
					<div class="contact-logo-text">
						<img src="${pageContext.request.contextPath}/images/logo.png"
							alt="Subidha Polyclinic Logo" class="contact-logo">
						<h2 class="contact-title">Subidha Polyclinic</h2>
					</div>
					<div class="contact-tag-button">
					
					<p class="contact-tagline">Connect with us for exceptional
						healthcare.</p>
					<a class="cta-button contact-cta" href="${pageContext.request.contextPath}/booking">Book
                        a consultation</a>
						
						</div>
				</div>
				<div class="contact-content-right">
					<h3 class="contact-subtitle">GET IN TOUCH</h3>
					<div class="contact-info-item">
						<i class="fas fa-map-marker-alt contact-icon"></i>
						<div>
							<h4>Mailing Address</h4>
							<p>Belbari, Morang</p>
						</div>
					</div>
					<div class="contact-info-item">
						<i class="fas fa-envelope contact-icon"></i>
						<div>
							<h4>Email Address</h4>
							<p>subidhapolyclinic@gmail.com</p>
						</div>
					</div>
					<div class="contact-info-item">
						<i class="fas fa-phone-alt contact-icon"></i>
						<div>
							<h4>Phone Number</h4>
							<p>9807090836</p>
						</div>
					</div>
				</div>
			</div>
		</section>



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