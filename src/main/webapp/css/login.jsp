<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>




<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sign in - Subidha Polyclinic</title>
   <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css">
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

    <div class="login-container">
        <div class="login-form-section">
            <div class="form-content">
               
                <img src="${pageContext.request.contextPath}/images/logo.png" alt="Subidha Polyclinic Logo" class="login-logo">

                <h2>Sign in to Subidha Polyclinic</h2>
                <p class="subtitle">Please enter your login details below.</p> 

                <%-- Display Registration Success Message --%>
                <% if (request.getParameter("registrationSuccess") != null) { %>
                <p class="message success-message">
                        <i class="fas fa-check-circle"></i>  <%= request.getParameter("registrationSuccess") %>
                    </p>
                    
                <% } %>

                 <%-- Display Login Error Message --%>
                <% if (request.getAttribute("errorMessage") != null) { %>
                     <p class="message error-message">
                         <i class="fas fa-exclamation-circle"></i>  <%= request.getAttribute("errorMessage") %>
                    </p>
                       
                <% } %>

                <form action="login" method="post" class="login-form">
                    <div class="input-group">
                        <label for="username">Username</label>
                        <input type="text" id="username" name="username" required placeholder="Enter your username">
                    </div>

                     <div class="input-group password-container">
                        <label for="password">Password</label>
                        <input type="password" id="password" name="password" required placeholder="Enter your password">
                        <i class="fas fa-eye password-toggle" id="password-toggle"></i>
                    </div>
                    
                    <button type="submit" class="login-button">Login</button>
                </form>

                <div class="login-links">
                    <a href="#">Forgot Password?</a>
                    <a href="${pageContext.request.contextPath}/register">Create an account</a>
                </div>
            </div>
             <p class="footer-text">© <%= new java.util.GregorianCalendar().get(java.util.Calendar.YEAR) %> Subidha Polyclinic. All rights reserved.</p> 
        </div>

        <div class="login-image-section">
           
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