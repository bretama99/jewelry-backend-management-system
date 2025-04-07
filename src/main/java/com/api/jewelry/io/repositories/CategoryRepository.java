package com.api.jewelry.io.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.api.jewelry.io.entity.CategoryEntity;

@Repository
public interface CategoryRepository extends PagingAndSortingRepository<CategoryEntity, Long>{
	
	CategoryEntity findByCategoryId(long categoryId);

	CategoryEntity findByCategoryAndIsDeleted(String searchKey, boolean b);

	Page<CategoryEntity> findByIsDeleted(boolean b, Pageable pageableRequest);

	Page<CategoryEntity> findByIsDeletedAndCategoryContaining(boolean b, String searchKey,
			Pageable pageableRequest);

	CategoryEntity findByCategoryIdAndIsDeleted(Integer categoryId, boolean b);
	
	List<CategoryEntity> findByIsDeletedAndCategory(boolean b, String stringCellValue);
	
}
