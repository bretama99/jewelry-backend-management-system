package com.api.jewelry.ui.model.request;

import javax.validation.constraints.NotBlank;

public class LoginRequestModel {

		@NotBlank
	    private String email;

	    @NotBlank
	    private String password;

	

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}
	    
	    
}
