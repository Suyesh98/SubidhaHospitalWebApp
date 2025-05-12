<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.subidha.model.User, com.subidha.model.Department, java.util.List" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%
User loggedInUser = (User) session.getAttribute("user");


List<User> userList = (List<User>) request.getAttribute("userList");
List<Department> departmentList = (List<Department>) request.getAttribute("departmentList");
String currentRoleFilter = request.getParameter("role") != null ? request.getParameter("role") : "all";
String currentSearchQuery = request.getParameter("search") != null ? request.getParameter("search") : "";
User userToEdit = (User) request.getAttribute("userToEdit");
Integer doctorDepartmentId = (Integer) request.getAttribute("doctorDepartmentId");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Management - Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/userManagement.css">
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

    <jsp:include page="/WEB-INF/pages/navigation.jsp">
        <jsp:param name="activePage" value="userManagement" />
    </jsp:include>

    <main>
        <div class="container">

            <div class="management-header">
                <h1 class="management-title">User Management</h1>
                <div class="header-actions">
                     <form action="userManagement" method="GET" class="search-filter-form">
                        <input type="text" name="search" class="search-input" placeholder="Search users..." value="<c:out value="${param.search}"/>">
                        <select name="role" class="filter-select" onchange="this.form.submit()">
                            <option value="all" ${param.role == 'all' or empty param.role ? 'selected' : ''}>All Roles</option>
                            <option value="admin" ${param.role == 'admin' ? 'selected' : ''}>Admin</option>
                            <option value="doctor" ${param.role == 'doctor' ? 'selected' : ''}>Doctor</option>
                            <option value="patient" ${param.role == 'patient' ? 'selected' : ''}>Patient</option>
                        </select>
                        <button type="submit" class="btn btn-primary"><i class="fas fa-search"></i> Filter</button>
                    </form>
                    <button class="btn btn-success" onclick="showAddDoctorForm()"><i class="fas fa-user-md"></i> Add Doctor</button>
                     <a href="${pageContext.request.contextPath}/admin" class="btn btn-info"><i class="fas fa-tachometer-alt"></i> Dashboard</a>
                </div>
            </div>

            <c:if test="${not empty param.successMessage}">
                <p class="message success-message"><i class="fas fa-check-circle"></i> ${param.successMessage}</p>
            </c:if>
            <c:if test="${not empty param.errorMessage}">
                <p class="message error-message"><i class="fas fa-exclamation-circle"></i> ${param.errorMessage}</p>
            </c:if>
             <c:if test="${not empty requestScope.errorMessage}">
                <p class="message error-message"><i class="fas fa-exclamation-circle"></i> ${requestScope.errorMessage}</p>
            </c:if>

            <section class="form-section" id="addEditDoctorSection" style="display: ${not empty userToEdit ? 'block' : 'none'};">
                 <h2 class="form-title" id="formTitle">${not empty userToEdit ? 'Edit User' : 'Add New Doctor'}</h2>
                 <form action="userManagement" method="POST" id="doctorForm">
                     <input type="hidden" name="action" id="formAction" value="${not empty userToEdit ? 'updateUser' : 'addDoctor'}">
                     <input type="hidden" name="userId" id="userId" value="${not empty userToEdit ? userToEdit.userId : ''}">

                     <div class="form-grid">
                         <div class="form-group">
                             <label for="firstName">First Name</label>
                             <input type="text" id="firstName" name="firstName" required value="${not empty userToEdit ? userToEdit.firstName : ''}">
                         </div>
                         <div class="form-group">
                             <label for="lastName">Last Name</label>
                             <input type="text" id="lastName" name="lastName" required value="${not empty userToEdit ? userToEdit.lastName : ''}">
                         </div>
                          <div class="form-group">
                             <label for="username">Username</label>
                             <input type="text" id="username" name="username" required value="${not empty userToEdit ? userToEdit.username : ''}" ${not empty userToEdit ? 'readonly' : ''}>
                             <c:if test="${empty userToEdit}">
                                <small>Used for login.</small>
                             </c:if>
                              <c:if test="${not empty userToEdit}">
                                <small>Username cannot be changed.</small>
                             </c:if>
                         </div>
                          <div class="form-group">
                             <div class="input-group full-width password-container">
                      		  <label for="password">Password</label>
                      		   <input type="password" id="password" name="password" ${empty userToEdit ? 'required' : ''}>
                        <i class="fas fa-eye password-toggle" id="password-toggle"></i>
                    </div>
                              <c:if test="${not empty userToEdit}">
                                <small>Leave blank to keep current password.</small>
                             </c:if>
                         </div>
                         <div class="form-group">
                             <label for="phoneNumber">Phone Number</label>
                             <input type="tel" id="phoneNumber" name="phoneNumber" required value="${not empty userToEdit ? userToEdit.phoneNumber : ''}">
                         </div>
                         <div class="form-group">
                            <label for="gender">Gender</label>
                            <select id="gender" name="gender" required>
                                <option value="" disabled ${empty userToEdit ? 'selected' : ''}>Select Gender</option>
                                <option value="Male" ${not empty userToEdit && userToEdit.gender == 'Male' ? 'selected' : ''}>Male</option>
                                <option value="Female" ${not empty userToEdit && userToEdit.gender == 'Female' ? 'selected' : ''}>Female</option>
                                <option value="Other" ${not empty userToEdit && userToEdit.gender == 'Other' ? 'selected' : ''}>Other</option>
                            </select>
                        </div>

                         <div class="form-group" id="departmentGroup" style="display: ${ (not empty userToEdit && userToEdit.role == 'doctor') or empty userToEdit ? 'block' : 'none'};" >
                            <label for="departmentId">Department</label>
                            <select id="departmentId" name="departmentId" ${ (not empty userToEdit && userToEdit.role == 'doctor') or empty userToEdit ? 'required' : ''} >
                                <option value="" disabled selected>Select Department</option>
                                <c:forEach var="dept" items="${departmentList}">
                                    <option value="${dept.departmentId}" ${not empty doctorDepartmentId && doctorDepartmentId == dept.departmentId ? 'selected' : ''}>
                                        <c:out value="${dept.departmentName}"/>
                                    </option>
                                </c:forEach>
                            </select>
                             <small>Assign a department for Doctors.</small>
                         </div>
                     </div>

                     <div class="form-actions">
                         <button type="button" class="btn btn-secondary" onclick="hideAddEditForm()">Cancel</button>
                         <button type="submit" class="btn btn-primary">${not empty userToEdit ? 'Save Changes' : 'Add Doctor'}</button>
                     </div>
                 </form>
             </section>


            <table class="management-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Username</th>
                        <th>Full Name</th>
                        <th>Role</th>
                        <th>Phone</th>
                        <th>Department</th>
                        <th>Registered</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${not empty userList}">
                            <c:forEach var="user" items="${userList}">
                                <tr>
                                    <td data-label="Username">${user.userId}</td>
                                    <td data-label="Username"><c:out value="${user.username}"/></td>
                                    <td data-label="Username"><c:out value="${user.firstName} ${user.lastName}"/></td>
                                    <td data-label="Username"><span class="role-badge role-${user.role}">${user.role}</span></td>
                                    <td data-label="Username"><c:out value="${user.phoneNumber}"/></td>
                                    <td data-label="Username">
                                        <c:if test="${user.role == 'doctor'}">
                                           ${user.departmentName != null ? user.departmentName : 'N/A'}
                                        </c:if>
                                         <c:if test="${user.role != 'doctor'}"> - </c:if>
                                    </td>
                                    <td data-label="Username"><fmt:formatDate value="${user.createdAt}" pattern="yyyy-MM-dd HH:mm"/></td>
                                    <td data-label="Username" class="actions-cell">
                                        <a href="userManagement?action=edit&userId=${user.userId}" class="btn btn-warning btn-sm"><i class="fas fa-edit"></i> Edit</a>
                                        <c:if test="${user.userId != loggedInUser.userId}">
                                             <form action="userManagement" method="POST" style="display: inline;" onsubmit="return confirm('Are you sure you want to delete user ${user.username}? This cannot be undone.');">
                                                <input type="hidden" name="action" value="deleteUser">
                                                <input type="hidden" name="userId" value="${user.userId}">
                                                <button type="submit" class="btn btn-danger btn-sm"><i class="fas fa-trash"></i> Delete</button>
                                            </form>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="8" style="text-align: center;">No users found matching criteria.</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>

        </div>
    </main>

    <footer class="footer">
        <div class="footer-container">
            <p>© <%=new java.util.GregorianCalendar().get(java.util.Calendar.YEAR)%> Subidha Polyclinic. All rights reserved.</p>
        </div>
    </footer>


<!--  For handling add/edit doctor form visibility, field setup, and password toggle -->
    <script>
        function showAddDoctorForm() {
            document.getElementById('addEditDoctorSection').style.display = 'block';
            document.getElementById('formTitle').innerText = 'Add New Doctor';
            document.getElementById('formAction').value = 'addDoctor';
            document.getElementById('doctorForm').reset();
            document.getElementById('userId').value = '';
            document.getElementById('username').readOnly = false;
            document.getElementById('departmentGroup').style.display = 'block';
            document.getElementById('departmentId').required = true;
            document.getElementById('password').required = true;
             document.getElementById('firstName').value = '';
             document.getElementById('lastName').value = '';
             document.getElementById('username').value = '';
             document.getElementById('phoneNumber').value = '';
             document.getElementById('gender').value = '';
             document.getElementById('departmentId').value = '';
        }

         function hideAddEditForm() {
            document.getElementById('addEditDoctorSection').style.display = 'none';
            document.getElementById('doctorForm').reset();
             const errorMessages = document.querySelectorAll('.error-message');
             errorMessages.forEach(msg => {
                 if (msg.closest('.form-section')) {
                     msg.remove();
                 }
             });
        }

         document.addEventListener('DOMContentLoaded', function() {
             const userIdInput = document.getElementById('userId');
             if (userIdInput && userIdInput.value !== '') {
                 document.getElementById('addEditDoctorSection').style.display = 'block';
                 document.getElementById('formTitle').innerText = 'Edit User';
                 document.getElementById('formAction').value = 'updateUser';
                 document.getElementById('username').readOnly = true;
                 document.getElementById('password').required = false;

                 const userRole = '<c:out value="${userToEdit.role}"/>';
                  const deptGroup = document.getElementById('departmentGroup');
                  const deptSelect = document.getElementById('departmentId');

                  if (userRole === 'doctor') {
                      deptGroup.style.display = 'block';
                      deptSelect.required = true;
                  } else {
                      deptGroup.style.display = 'none';
                      deptSelect.required = false;
                      deptSelect.value = '';
                  }

             } else {
                  if (!'${param.action}' || '${param.action}' !== 'edit') {
                     document.getElementById('addEditDoctorSection').style.display = 'none';
                  }
             }
         });
         
         
      
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