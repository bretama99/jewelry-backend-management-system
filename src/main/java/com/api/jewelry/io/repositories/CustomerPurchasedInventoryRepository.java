package com.api.jewelry.io.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.api.jewelry.io.entity.CustomerPurchasedInventoryEntity;
import com.api.jewelry.io.entity.OrderEntity;

@Repository
public interface CustomerPurchasedInventoryRepository extends PagingAndSortingRepository<CustomerPurchasedInventoryEntity, Long>{
	CustomerPurchasedInventoryEntity findByCustomerPurchasedInventoryIdAndIsDeleted(
			Integer customerPurchasedInventoryId, boolean b);

	CustomerPurchasedInventoryEntity findByCustomerPurchasedInventoryIdAndIsDeleted(long customerPurchasedInventoryId,
			boolean b);

	List<CustomerPurchasedInventoryEntity> findByCustomerIdAndIsDeleted(String name, boolean b);	
}
