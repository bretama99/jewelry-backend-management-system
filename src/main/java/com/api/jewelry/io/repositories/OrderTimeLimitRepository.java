package com.api.jewelry.io.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.api.jewelry.io.entity.OrderTimeLimitEntity;

@Repository
public interface OrderTimeLimitRepository extends PagingAndSortingRepository<OrderTimeLimitEntity, Integer> {
	
	
	OrderTimeLimitEntity findTopByIsDeletedOrderByOrderTimeLimitIdDesc(boolean b);
	
	OrderTimeLimitEntity findByOrderTimeLimitIdAndIsDeleted(Integer orderTimeLimitId, boolean b);

	OrderTimeLimitEntity findTopByIsDeleted(boolean b);
	

}
