package com.api.jewelry.ui.model.response;

import org.springframework.web.multipart.MultipartFile;

import com.api.jewelry.model.audit.Audit;

public class UserRest extends Audit{
	
	private static final long serialVersionUID = -4053530459677628972L;
	private String userId;
	private String firstName;
	private String lastName;
	private String address;
	private String email;
	private String phone;
	private String userType;
	private String userStatus;
	
	private long companyId;
	private int totalPages;
	private MultipartFile profilePicture;
	private String profilePicture1;
	
	public String getProfilePicture1() {
		return profilePicture1;
	}
	public void setProfilePicture1(String profilePicture1) {
		this.profilePicture1 = profilePicture1;
	}
	public MultipartFile getProfilePicture() {
		return profilePicture;
	}
	public void setProfilePicture(MultipartFile profilePicture) {
		this.profilePicture = profilePicture;
	}
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	public String getUserStatus() {
		return userStatus;
	}
	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}
	public long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
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
	
	
}
