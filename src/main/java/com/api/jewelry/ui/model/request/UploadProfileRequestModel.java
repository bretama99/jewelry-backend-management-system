package com.api.jewelry.ui.model.request;

import org.springframework.web.multipart.MultipartFile;

public class UploadProfileRequestModel {
	
	private MultipartFile  profilePicture;
	private String userId;
	private Long id;
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public MultipartFile getProfilePicture() {
		return profilePicture;
	}

	public void setProfilePicture(MultipartFile profilePicture) {
		this.profilePicture = profilePicture;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
