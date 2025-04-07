package com.api.jewelry.io.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.api.jewelry.io.entity.TransactionEntity;

public interface TransactionRepository extends PagingAndSortingRepository<TransactionEntity, Long>{

	TransactionEntity findByTransactionId(long transactionId);

	Page<TransactionEntity> findByInventoryId(long inventoryId, Pageable pageableRequest);

}
