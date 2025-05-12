<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Contact Us - Subidha Polyclinic</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/contact_us.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>

    <jsp:include page="/WEB-INF/pages/navigation.jsp">
        <jsp:param name="activePage" value="contact_us" />
    </jsp:include>

    <main class="contact-page-main">
        <section class="contact-us-section">
            <div class="contact-us-container">
                <div class="contact-image-area">
                    <img src="${pageContext.request.contextPath}/images/contact doctor bg.png" alt="Dental Patient Care">
                </div>
                <div class="contact-form-area">
                    <h2>Contact Us</h2>
                    <form action="#" method="post">
                        <div class="form-group">
                            <label for="name">Name</label>
                            <input type="text" id="name" name="name" placeholder="Enter your Name">
                        </div>
                        <div class="form-group">
                            <label for="email">Email</label>
                            <input type="email" id="email" name="email" placeholder="Enter a valid email address">
                        </div>
                        <div class="form-group">
                            <label for="message">Message</label>
                            <textarea id="message" name="message" rows="3"></textarea>
                        </div>
                        <div class="form-group terms-group">
                            <input type="checkbox" id="terms" name="terms">
                            <label for="terms" class="terms-label">I accept the <a href="#">Terms of Service</a></label>
                        </div>
                        <button type="submit" class="submit-button">SUBMIT</button>
                    </form>
                </div>
            </div>
        </section>

        <section class="contact-details-section">
            <div class="contact-details-container">
                <div class="contact-detail-box">
                    <i class="fas fa-phone-alt"></i>
                    <h4>Contact US</h4>
                    <p>+977 980-7090836</p>
                    <p>subidhapolyclinic@gmail.com</p>
                </div>
                <div class="contact-detail-box">
                    <i class="fas fa-map-marker-alt"></i>
                    <h4>LOCATION</h4>
                    <p>Belbari, Morang</p>
                    <p>+977 980-7090836</p>
                </div>
                <div class="contact-detail-box">
                    <i class="fas fa-clock"></i>
                    <h4>HOURS</h4>
                    <p>Mon - Fri : 11 am - 8 pm</p>
                    <p>Sat, Sun :  6 am - 8 pm</p>
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