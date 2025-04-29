<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register - Subidha Polyclinic</title>
   
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/register.css">
    
    
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
	 <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">

 <style>
        .password-container {
            position: relative;
        }

        .password-toggle {
            position: absolute;
            right: 10px;
            top: 70%;
            transform: translateY(-50%);
            cursor: pointer;
            color: #888;
        }
         .password-toggle:hover{
         	color: #4a3a80;
         }
    </style>
</head>
<body>

    <div class="register-container"> 
        <div class="register-form-section"> 
            <div class="form-content">
                <img src="${pageContext.request.contextPath}/images/logo.png" alt="Subidha Polyclinic Logo" class="register-logo"> 

                <h2>Create Your Account</h2>
                <p class="subtitle">Join Subidha Polyclinic today.</p>

                <%-- Display Registration Error Message --%>
                <% if (request.getAttribute("errorMessage") != null) { %>
                    <p class="message error-message">
                        <%= request.getAttribute("errorMessage") %>
                    </p>
                <% } %>

                <form action="register" method="post" class="register-form">
                    
                    <div class="form-grid">
                        <div class="input-group">
                            <label for="firstName">First Name</label>
                            <input type="text" id="firstName" name="firstName" required placeholder="e.g., John">
                        </div>

                        <div class="input-group">
                            <label for="lastName">Last Name</label>
                            <input type="text" id="lastName" name="lastName" required placeholder="e.g., Doe">
                        </div>

                        <div class="input-group full-width"> 
                            <label for="username">Username</label>
                            <input type="text" id="username" name="username" required placeholder="Choose a unique username">
                        </div>


                        <div class="input-group">
                             <label for="gender">Gender</label>
                             <select id="gender" name="gender" required>
                                 <option value="" disabled selected>Select Gender</option> 
                                 <option value="Male">Male</option>
                                 <option value="Female">Female</option>
                                 <option value="Other">Other</option>
                             </select>
                        </div>

                         <div class="input-group">
                            <label for="phoneNumber">Phone Number</label>
                            <input type="tel" id="phoneNumber" name="phoneNumber" placeholder="e.g., +1234567890"> 
                        </div>

                         <div class="input-group">
                             <label for="visitDate">Preferred Visit Date </label>
                             <input type="date" id="visitDate" name="visitDate">
                         </div>

                        <div class="input-group">
                            <label for="department">Department </label>
                            <input type="text" id="department" name="department" placeholder="e.g., Cardiology">
                        </div>
                        
						 <div class="input-group full-width password-container">
                      		  <label for="password">Password</label>
                       		 <input type="password" id="password" name="password" required placeholder="Create a strong password">
                        <i class="fas fa-eye password-toggle" id="password-toggle"></i>
                    </div>

                    </div> 
                    <%-- End form grid --%>

                    <button type="submit" class="register-button">Register</button> 
                </form>

                <div class="register-links"> 
                    <p>Already have an account? <a href="${pageContext.request.contextPath}/login">Login</a></p>
                </div>
            </div>
            <p class="footer-text">© <%= new java.util.GregorianCalendar().get(java.util.Calendar.YEAR) %> Subidha Polyclinic. All rights reserved.</p> <%-- Reused footer --%>
        </div>

        <div class="register-image-section"> 
        </div>
    </div>


<!-- for toggling the eye icon : show / hide password -->
 <script>
        const passwordInput = document.getElementById("password");
        const passwordToggle = document.getElementById("password-toggle");

        passwordToggle.addEventListener("click", function() {
            if (passwordInput.type === "password") {
                passwordInput.type = "text";
                passwordToggle.classList.remove("fa-eye");
                passwordToggle.classList.add("fa-eye-slash");
            } else {
                passwordInput.type = "password";
                passwordToggle.classList.remove("fa-eye-slash");
                passwordToggle.classList.add("fa-eye");
            }
        });
    </script>
</body>
</html>