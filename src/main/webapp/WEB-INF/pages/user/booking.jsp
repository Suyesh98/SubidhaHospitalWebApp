<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Book Consultation - Subidha Polyclinic</title>
    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/booking.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css" integrity="sha512-DTOQO9RWCH3ppGqcWaEA1BIZOC6xxalwEsw9c2QQeAIftl+Vegovlnee1c9QX4TctnWMn13TZye+giMm8e2LwA==" crossorigin="anonymous" referrerpolicy="no-referrer" />
</head>
<body>

    <jsp:include page="/WEB-INF/pages/navigation.jsp">
        <jsp:param name="activePage" value="booking"/>
    </jsp:include>

    <main class="booking-main">
        <div class="booking-container"> 
            <div class="booking-card">
                <h1 class="card-title">Book Your Consultation</h1>
                <p class="card-subtitle">Fill out the form below to request an appointment. Your existing appointments are listed further down.</p>

                
                <c:if test="${not empty message}">
                    <p class="message ${messageType == 'success' ? 'success-message' : (messageType == 'error' ? 'error-message' : 'info-message')}">
                        <c:choose>
                            <c:when test="${messageType == 'success'}">
                                <i class="fas fa-check-circle"></i>
                            </c:when>
                            <c:when test="${messageType == 'error'}">
                                <i class="fas fa-times-circle"></i> 
                            </c:when>
                            <c:otherwise> 
                                <i class="fas fa-info-circle"></i>
                            </c:otherwise>
                        </c:choose>
                        <c:out value="${message}"/> 
                    </p>
                </c:if>

                <c:if test="${not empty user && not empty patient}">
                    <form action="${pageContext.request.contextPath}/booking" method="post" class="booking-form">
                        <h2 class="form-section-title">Your Details</h2>
                        <p style="font-size: 0.85rem; color: var(--light-text); margin: -15px 0 20px 0;">Please <a href="${pageContext.request.contextPath}/updateProfile">update your profile</a> if any details below are incorrect.</p>
                        <div class="form-grid personal-details-grid">
                            <div class="input-group">
                                <label for="fullName">Full Name</label>
                                <input type="text" id="fullName" name="fullName" value="${user.firstName} ${user.lastName}" readonly>
                            </div>
                            <div class="input-group">
                                <label for="username">Username</label>
                                <input type="text" id="username" name="username" value="${user.username}" readonly>
                            </div>
                             <div class="input-group">
                                <label for="phoneNumber">Phone Number</label>
                                <input type="tel" id="phoneNumber" name="phoneNumber" value="${user.phoneNumber}" readonly>
                            </div>

                            
                             <div class="input-group">
                                <c:choose>
                                    <c:when test="${empty patient.dateOfBirth}">
                                        <label for="dateOfBirth">Date of Birth <span class="required">*</span></label>
                                        <input type="date" id="dateOfBirth" name="dateOfBirth" required max=""> 
                                        <span style="font-size: 0.8rem; color: var(--medium-text); margin-top: 4px;">This will be saved to your profile.</span>
                                    </c:when>
                                    <c:otherwise>
                                        <label for="dateOfBirth">Date of Birth</label>
                                        <input type="text" id="dateOfBirth" value="<fmt:formatDate value="${patient.dateOfBirth}" pattern="yyyy-MM-dd"/>" readonly>
                                    </c:otherwise>
                                </c:choose>
                            </div>

                            
                             <div class="input-group full-width">
                                 <c:choose>
                                    <c:when test="${empty patient.address}">
                                         <label for="address">Address <span class="required">*</span></label>
                                         <textarea id="address" name="address" rows="3" required placeholder="Enter your full address" style="resize: none;"></textarea>
                                         <span style="font-size: 0.8rem; color: var(--medium-text); margin-top: 4px;">This will be saved to your profile.</span>
                                    </c:when>
                                    <c:otherwise>
                                         <label for="address">Address</label>
                                         <textarea id="address" rows="3" readonly><c:out value="${patient.address}"/></textarea>
                                    </c:otherwise>
                                 </c:choose>
                              </div>
                        </div> 

                        <h2 class="form-section-title">Appointment Details</h2>
                        <div class="form-grid appointment-details-grid">
                             <div class="input-group">
                                <label for="doctor">Select Doctor <span class="required">*</span></label>
                                <select id="doctor" name="doctorId" required>
                                    <option value="" disabled selected>Choose a doctor...</option>
                                    <c:forEach var="doc" items="${doctors}">
                                        <option value="${doc.doctorId}">
                                            <c:out value="${doc.fullName}"/> - (<c:out value="${not empty doc.departmentName ? doc.departmentName : 'General'}"/>)
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div class="input-group">
                                <label for="preferredDate">Preferred Date <span class="required">*</span></label>
                                <input type="date" id="preferredDate" name="preferredDate" required min=""> 
                            </div>

                            <div class="input-group">
                                <label for="preferredTime">Preferred Time <span class="required">*</span></label>
                                <input type="time" id="preferredTime" name="preferredTime" required>
                            </div>

                            <div class="input-group full-width">
                                <label for="reason">Reason for Visit (Optional)</label>
                                <textarea id="reason" name="reason" rows="4" placeholder="Briefly describe the reason for your consultation..."></textarea>
                            </div>
                        </div> 

                        <button type="submit" class="submit-button">
                           <i class="fa-regular fa-calendar-check"></i> Request Appointment
                        </button>
                    </form>
                </c:if>

                
                <div class="appointments-section">
                    <h2>Your Appointments</h2>

                    
                    <form action="${pageContext.request.contextPath}/booking" method="get" class="table-controls">
                         <div>
                            <label for="searchInput">Search</label>
                            <input type="text" id="searchInput" name="searchTerm" placeholder="Search by doctor, department, reason..." value="${not empty searchTerm ? searchTerm : ''}">
                        </div>
                         <div>
                            <label for="statusFilter">Filter by Status</label>
                            <select id="statusFilter" name="statusFilter">
                                <option value="all" ${statusFilter == 'all' ? 'selected' : ''}>All Statuses</option>
                                <option value="Pending" ${statusFilter == 'Pending' ? 'selected' : ''}>Pending</option>
                                <option value="Confirmed" ${statusFilter == 'Confirmed' ? 'selected' : ''}>Confirmed</option>
                                <option value="Cancelled" ${statusFilter == 'Cancelled' ? 'selected' : ''}>Cancelled</option>
                                <option value="Completed" ${statusFilter == 'Completed' ? 'selected' : ''}>Completed</option>
                            </select>
                        </div>
                         <button type="submit" class="filter-button">
                            <i class="fas fa-filter"></i> Filter / Search
                        </button>
                    </form>


                    
                    <c:choose>
                        <c:when test="${not empty visits}">
                            <div class="table-responsive">
                                <table class="visits-table" id="appointmentsTable"> 
                                    <thead>
                                        <tr>
                                            
                                            
                                            <th data-column="doctorName">Doctor</th>
                                            <th data-column="departmentName">Department</th>
                                            <th data-column="visitDate">Date</th>
                                            <th data-column="visitTime">Time</th>
                                            <th data-column="reason">Reason</th>
                                            <th data-column="status">Status</th>
                                        </tr>
                                    </thead>
                                    <tbody id="appointmentsTableBody">
                                        <c:forEach var="visit" items="${visits}">
                                            <tr>
                                                
                                                <td data-label="Doctor"><c:out value="${visit.doctorName}"/></td>
                                                <td data-label="Department"><c:out value="${not empty visit.departmentName ? visit.departmentName : '-'}"/></td>
                                                <td data-label="Date"><fmt:formatDate value="${visit.visitDate}" pattern="yyyy-MM-dd"/></td>
                                                <td data-label="Time"><fmt:formatDate value="${visit.visitTime}" pattern="hh:mm a"/></td>
                                                <td data-label="Reason" class="reason-cell" title="${visit.reason}"> 
                                                   <c:out value="${visit.reason}" default="-"/>
                                                </td>
                                                <td data-label="Status">
                                                    <span class="status status-${visit.status.toLowerCase()}">
                                                        <c:out value="${visit.status}"/>
                                                    </span>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                            
                        </c:when>
                        <c:otherwise>
                             
                             <c:set var="isFiltered" value="${not empty searchTerm || (not empty statusFilter && statusFilter != 'all')}" />
                             <p class="no-appointments-message ${isFiltered ? 'filtered' : ''}">
                                <c:choose>
                                    <c:when test="${isFiltered}">
                                        No appointments match your current search or filter criteria.
                                    </c:when>
                                    <c:otherwise>
                                        You currently have no appointments scheduled.
                                    </c:otherwise>
                                </c:choose>
                            </p>
                        </c:otherwise>
                    </c:choose>
                </div> 

                 <div class="form-footer">
                     <a href="${pageContext.request.contextPath}/home" class="back-button">
                        <i class="fas fa-arrow-left"></i> Back to Home
                    </a>
                </div>

            </div> 
        </div> 
    </main>

<!-- Handle date limits  -->
    <script>
        document.addEventListener("DOMContentLoaded", function() {
            
            var today = new Date();
            
            var dd = String(today.getDate()).padStart(2, '0');
            var mm = String(today.getMonth() + 1).padStart(2, '0'); 
            var yyyy = today.getFullYear();
            var todayFormatted = yyyy + '-' + mm + '-' + dd;

            var preferredDateInput = document.getElementById("preferredDate");
            if (preferredDateInput) {
                preferredDateInput.setAttribute('min', todayFormatted);
            }

            var dobInput = document.getElementById("dateOfBirth");
             if (dobInput && dobInput.type === 'date') { 
                
                dobInput.setAttribute('max', todayFormatted);
             }

            
            
            function addTitleToReasonCells() {
                const reasonCells = document.querySelectorAll('.visits-table tbody td.reason-cell');
                reasonCells.forEach(cell => {
                    
                    
                    if (!cell.getAttribute('title')) {
                         cell.setAttribute('title', cell.textContent || cell.innerText);
                    }
                });
            }
            addTitleToReasonCells(); 
            

        });
    </script>

</body>
</html>