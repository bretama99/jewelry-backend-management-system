package com.api.jewelry.ui.model.request;
import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

public class CustomerDto {

    public static class Request {
        private String firstName;
        private String lastName;
        private String email;
        private String phone;
        private String address;
        private String idNumber;
        private MultipartFile avatar;
		public String getFirstName() {
			return firstName;
		}
		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}
		public String getLastName() {
			return lastName;
		}
		public void setLastName(String lastName) {
			this.lastName = lastName;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public String getPhone() {
			return phone;
		}
		public void setPhone(String phone) {
			this.phone = phone;
		}
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
		}
		public String getIdNumber() {
			return idNumber;
		}
		public void setIdNumber(String idNumber) {
			this.idNumber = idNumber;
		}

		public MultipartFile getAvatar() {
			return avatar;
		}
		public void setAvatar(MultipartFile avatar) {
			this.avatar = avatar;
		}
		public Request(String firstName, String lastName, String email, String phone, String address, String idNumber,
				MultipartFile avatar) {
			super();
			this.firstName = firstName;
			this.lastName = lastName;
			this.email = email;
			this.phone = phone;
			this.address = address;
			this.idNumber = idNumber;
			this.avatar = avatar;
		}
		public Request() {
			super();
		}
		
		
        
    }

    public static class Response {
        private Long id;
        private String firstName;
        private String lastName;
        private String email;
        private String phone;
        private String address;
        private String idNumber;
        private String avatar;
        private LocalDateTime createdAt;
        private LocalDateTime lastActive;
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getFirstName() {
			return firstName;
		}
		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}
		public String getLastName() {
			return lastName;
		}
		public void setLastName(String lastName) {
			this.lastName = lastName;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public String getPhone() {
			return phone;
		}
		public void setPhone(String phone) {
			this.phone = phone;
		}
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
		}
		public String getIdNumber() {
			return idNumber;
		}
		public void setIdNumber(String idNumber) {
			this.idNumber = idNumber;
		}
		public String getAvatar() {
			return avatar;
		}
		public void setAvatar(String avatar) {
			this.avatar = avatar;
		}
		public LocalDateTime getCreatedAt() {
			return createdAt;
		}
		public void setCreatedAt(LocalDateTime createdAt) {
			this.createdAt = createdAt;
		}
		public LocalDateTime getLastActive() {
			return lastActive;
		}
		public void setLastActive(LocalDateTime lastActive) {
			this.lastActive = lastActive;
		}
		public Response(Long id, String firstName, String lastName, String email, String phone, String address,
				String idNumber, String avatar, LocalDateTime createdAt, LocalDateTime lastActive) {
			super();
			this.id = id;
			this.firstName = firstName;
			this.lastName = lastName;
			this.email = email;
			this.phone = phone;
			this.address = address;
			this.idNumber = idNumber;
			this.avatar = avatar;
			this.createdAt = createdAt;
			this.lastActive = lastActive;
		}
		public Response() {
			super();
		}
        
        
    }
    
    public static class UpdateRequest {
        private String firstName;
        private String lastName;
        private String email;
        private String phone;
        private String address;
        private String avatar;
		public String getFirstName() {
			return firstName;
		}
		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}
		public String getLastName() {
			return lastName;
		}
		public void setLastName(String lastName) {
			this.lastName = lastName;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public String getPhone() {
			return phone;
		}
		public void setPhone(String phone) {
			this.phone = phone;
		}
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
		}
		public String getAvatar() {
			return avatar;
		}
		public void setAvatar(String avatar) {
			this.avatar = avatar;
		}
		public UpdateRequest(String firstName, String lastName, String email, String phone, String address,
				String avatar) {
			super();
			this.firstName = firstName;
			this.lastName = lastName;
			this.email = email;
			this.phone = phone;
			this.address = address;
			this.avatar = avatar;
		}
		public UpdateRequest() {
			super();
		}
        
        
    }
}