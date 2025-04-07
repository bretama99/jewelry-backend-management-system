package com.api.jewelry.ui.model.response;
import java.util.List;

public class CategoryResponse {
    
    public static class CategoryDto {
        private Long id;
        private String name;
        private Integer itemCount;
        private Double laborRate;
        private Integer markup;
        private String status;
        
    	public static CategoryDto CategoryDto(Long id, String name, Integer itemCount, Double laborRate, Integer markup,
    			String status) {
    		// TODO Auto-generated method stub
    		return null;
    	}
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Integer getItemCount() {
			return itemCount;
		}
		public void setItemCount(Integer itemCount) {
			this.itemCount = itemCount;
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
    

    public static class PagedResponse {
        private List<CategoryDto> categories;
        private long totalCategories;
        private int page;
        private int size;
        public PagedResponse(List<CategoryDto> categories, long totalCategories, int page, int size) {
            this.categories = categories;
            this.totalCategories = totalCategories;
            this.page = page;
            this.size = size;
        }
		public List<CategoryDto> getCategories() {
			return categories;
		}
		public void setCategories(List<CategoryDto> categories) {
			this.categories = categories;
		}
		public long getTotalCategories() {
			return totalCategories;
		}
		public void setTotalCategories(long totalCategories) {
			this.totalCategories = totalCategories;
		}
		public int getPage() {
			return page;
		}
		public void setPage(int page) {
			this.page = page;
		}
		public int getSize() {
			return size;
		}
		public void setSize(int size) {
			this.size = size;
		}
        
        
    }



}