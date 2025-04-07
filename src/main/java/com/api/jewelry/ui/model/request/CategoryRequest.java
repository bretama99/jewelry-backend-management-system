package com.api.jewelry.ui.model.request;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CategoryRequest {
    public static class Create {
        @NotBlank(message = "Category name is required")
        private String name;
        
        @NotNull(message = "Labor rate is required")
        @Min(value = 0, message = "Labor rate must be positive")
        private Double laborRate;
        
        @NotNull(message = "Markup is required")
        @Min(value = 0, message = "Markup must be positive")
        private Integer markup;
        
        @NotBlank(message = "Status is required")
        private String status;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Double getLaborRate() {
			return laborRate;
		}

		public void setLaborRate(Double laborRate) {
			this.laborRate = laborRate;
		}

		public Integer getMarkup() {
			return markup;
		}

		public void setMarkup(Integer markup) {
			this.markup = markup;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		} 
    }
    
    public static class Update {
        @NotBlank(message = "Category name is required")
        private String name;
        
        @NotNull(message = "Labor rate is required")
        @Min(value = 0, message = "Labor rate must be positive")
        private Double laborRate;
        
        @NotNull(message = "Markup is required")
        @Min(value = 0, message = "Markup must be positive")
        private Integer markup;
        
        @NotBlank(message = "Status is required")
        private String status;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Double getLaborRate() {
			return laborRate;
		}

		public void setLaborRate(Double laborRate) {
			this.laborRate = laborRate;
		}

		public Integer getMarkup() {
			return markup;
		}

		public void setMarkup(Integer markup) {
			this.markup = markup;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}  
    }
    
    public static class UpdateStatus {
        @NotBlank(message = "Status is required")
        private String status;

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}
    }
}
