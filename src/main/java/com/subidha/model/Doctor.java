 package com.subidha.model;

 import java.sql.Timestamp;

 public class Doctor {
     private int doctorId;
     private int userId; 
     private Integer departmentId; 
     private Timestamp createdAt; 
     private Timestamp updatedAt; 

   
     private User userDetails;
     private String firstName;
     private String lastName;
     private String departmentName;


     // Constructors
     public Doctor() {}

     // Getters and Setters
     public int getDoctorId() { return doctorId; }
     public void setDoctorId(int doctorId) { this.doctorId = doctorId; }

     public int getUserId() { return userId; }
     public void setUserId(int userId) { this.userId = userId; }

     public Integer getDepartmentId() { return departmentId; }
     public void setDepartmentId(Integer departmentId) { this.departmentId = departmentId; }

     public Timestamp getCreatedAt() { return createdAt; }
     public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

     public Timestamp getUpdatedAt() { return updatedAt; }
     public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

     public User getUserDetails() { return userDetails; }
     public void setUserDetails(User userDetails) { this.userDetails = userDetails; }

     public String getFirstName() { return firstName; }
     public void setFirstName(String firstName) { this.firstName = firstName; }
     public String getLastName() { return lastName; }
     public void setLastName(String lastName) { this.lastName = lastName; }
     public String getDepartmentName() { return departmentName; }
     public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
     
     
     
     public String getFullName() {
         return (firstName != null ? firstName : "") + " " + (lastName != null ? lastName : "");
    }

     @Override
     public String toString() {
         return "Doctor [doctorId=" + doctorId + ", userId=" + userId + ", departmentId=" + departmentId +
                ", name=" + getFullName() + ", departmentName=" + departmentName + "]";
     }
 }