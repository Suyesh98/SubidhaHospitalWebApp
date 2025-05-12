<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.subidha.model.User, com.subidha.model.Department, java.util.List" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%
  

    List<Department> departmentList = (List<Department>) request.getAttribute("departmentList");
    Department deptToEdit = (Department) request.getAttribute("deptToEdit");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Department Management - Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/departmentManagement.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body>

    <jsp:include page="/WEB-INF/pages/navigation.jsp">
       <jsp:param name="activePage" value="departmentManagement" />

    </jsp:include>

    <main>
        <div class="container">

            <div class="management-header">
                <h1 class="management-title">Department Management</h1>
                 <a href="${pageContext.request.contextPath}/admin" class="btn btn-info"><i class="fas fa-tachometer-alt"></i> Dashboard</a>
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


             <section class="form-section">
                 <c:choose>
                     <c:when test="${not empty deptToEdit}">
                         <h2 class="form-title"><i class="fas fa-edit"></i> Edit Department</h2>
                         <form action="departmentManagement" method="POST">
                             <input type="hidden" name="action" value="update">
                             <input type="hidden" name="departmentId" value="${deptToEdit.departmentId}">
                             <div class="form-inline">
                                 <div class="form-group">
                                     <label for="departmentNameEdit">Department Name</label>
                                     <input type="text" id="departmentNameEdit" name="departmentName" required
                                            value="${fn:escapeXml(deptToEdit.departmentName)}">
                                 </div>
                                 <div class="form-actions">
                                     <button type="submit" class="btn btn-primary"><i class="fas fa-save"></i> Save Changes</button>
                                     <a href="departmentManagement" class="btn btn-secondary">Cancel</a>
                                 </div>
                             </div>
                         </form>
                     </c:when>
                     <c:otherwise>
                         <h2 class="form-title"><i class="fas fa-plus-circle"></i> Add New Department</h2>
                         <form action="departmentManagement" method="POST">
                             <input type="hidden" name="action" value="add">
                              <div class="form-inline">
                                  <div class="form-group">
                                      <label for="departmentNameAdd">Department Name</label>
                                      <input type="text" id="departmentNameAdd" name="departmentName" required placeholder="Enter new department name">
                                  </div>
                                  <div class="form-actions">
                                       <button type="submit" class="btn btn-success"><i class="fas fa-plus"></i> Add Department</button>
                                  </div>
                              </div>
                         </form>
                     </c:otherwise>
                 </c:choose>
             </section>

            <section class="table-section">
                <h2>Existing Departments</h2>
                <c:choose>
                    <c:when test="${not empty departmentList}">
                        <table class="management-table">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Department Name</th>
                                    <th>Created At</th>
                                    <th>Last Updated</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="dept" items="${departmentList}">
                                    <tr>
                                        <td data-label="ID">${dept.departmentId}</td>
                                        <td data-label="Name"><c:out value="${dept.departmentName}"/></td>
                                        <td data-label="Created"><fmt:formatDate value="${dept.createdAt}" pattern="yyyy-MM-dd HH:mm"/></td>
                                        <td data-label="Updated"><fmt:formatDate value="${dept.updatedAt}" pattern="yyyy-MM-dd HH:mm"/></td>
                                        <td data-label="Actions" class="actions-cell">
                                            <a href="departmentManagement?action=edit&departmentId=${dept.departmentId}" class="btn btn-warning btn-sm"><i class="fas fa-edit"></i> Edit</a>
                                            <form action="departmentManagement" method="POST" style="display: inline;" onsubmit="return confirm('Are you sure you want to delete the department \'${fn:escapeXml(dept.departmentName)}\'? This might fail if doctors are assigned to it.');">
                                                <input type="hidden" name="action" value="delete">
                                                <input type="hidden" name="departmentId" value="${dept.departmentId}">
                                                <button type="submit" class="btn btn-danger btn-sm"><i class="fas fa-trash"></i> Delete</button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </c:when>
                    <c:otherwise>
                        <p class="message info-message"><i class="fas fa-info-circle"></i> No departments found. Add one using the form above.</p>
                    </c:otherwise>
                </c:choose>
             </section>
        </div>
    </main>

    <footer class="footer">
        <div class="footer-container">
            <p>© <%=new java.util.GregorianCalendar().get(java.util.Calendar.YEAR)%> Subidha Polyclinic. All rights reserved.</p>
        </div>
    </footer>

</body>
</html>