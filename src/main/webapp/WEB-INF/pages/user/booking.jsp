<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Book Consultation - Subidha Polyclinic</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/booking.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>

    <jsp:include page="/WEB-INF/pages/navigation.jsp">
        <jsp:param name="activePage" value="booking"/> 
    </jsp:include>

    <main class="booking-main">
        <div class="booking-container">
            <div class="booking-card">
                <h1 class="card-title">Book Your Consultation</h1>
                <p class="card-subtitle">Fill out the form below to request an appointment. We'll confirm via email or phone.</p>

                <form action="#" method="post" class="booking-form"> 
                    <div class="form-grid">
                        <div class="input-group">
                            <label for="fullName">Full Name</label>
                            <input type="text" id="fullName" name="fullName" required placeholder="e.g., Jane Smith">
                        </div>

                        <div class="input-group">
                            <label for="email">Email Address</label>
                            <input type="email" id="email" name="email" required placeholder="e.g., jane.smith@example.com">
                        </div>

                         <div class="input-group">
                            <label for="phoneNumber">Phone Number</label>
                            <input type="tel" id="phoneNumber" name="phoneNumber" required placeholder="e.g., 98XXXXXXXX">
                        </div>

                         <div class="input-group">
                            <label for="department">Select Department</label>
                            <select id="department" name="department" required>
                                <option value="" disabled selected>Choose a department</option>
                                <option value="Cardiology">Cardiology (Dr. Suyesh)</option>
                                <option value="Gynecology">Gynecology (Dr. Ritika)</option>
                                <option value="Orthopedics">Orthopedics (Dr. Snehal)</option>
                                <option value="General">General Consultation</option>
                                <option value="Other">Other</option>
                            </select>
                        </div>

                        <div class="input-group">
                             <label for="preferredDate">Preferred Date</label>
                             <input type="date" id="preferredDate" name="preferredDate" required>
                        </div>

                        <div class="input-group">
                            <label for="preferredTime">Preferred Time</label>
                            <input type="time" id="preferredTime" name="preferredTime" required>
                        </div>

                        <div class="input-group full-width">
                            <label for="reason">Reason for Visit</label>
                            <textarea id="reason" name="reason" rows="4" placeholder="Briefly describe the reason for your consultation (optional)"></textarea>
                        </div>
                    </div> 

                    <button type="submit" class="submit-button">Request Appointment</button>
                </form>

                <div class="form-footer">
                     <a href="${pageContext.request.contextPath}/home" class="back-button">
                        <i class="fas fa-arrow-left"></i> Back to Home
                    </a>
                </div>
            </div> 
        </div> 
    </main>

</body>
</html>