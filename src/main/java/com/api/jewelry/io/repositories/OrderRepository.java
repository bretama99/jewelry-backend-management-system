package com.api.jewelry.io.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.api.jewelry.io.entity.OrderEntity;

@Repository
public interface OrderRepository extends PagingAndSortingRepository<OrderEntity, Long>{

	OrderEntity findByOrderId(long orderId);

	Object findByOrderNumber(String orderNumber);

	Page<OrderEntity> findByUserId(String userId, Pageable pageableRequest);

	OrderEntity findByOrderIdAndIsDeleted(long orderId, boolean b);

	List<OrderEntity> findByOrderIdInAndIsDeletedOrderByOrderIdDesc(List<Long> orderIds, boolean b);

//	OrderEntity findByOrderNumberAndOrderTypeIdAndIsDeleted(String orderNumber, Integer orderTypeId, boolean b);

	OrderEntity findByOrderNumberAndIsDeleted(String orderNumber, boolean b);
	
}
