package com.api.jewelry.io.repositories;

import java.time.Instant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.api.jewelry.io.entity.PriceEntity;

public interface PriceRepository extends PagingAndSortingRepository<PriceEntity, Long> {

	Page<PriceEntity> findByIsDeleted(boolean b, Pageable pageableRequest);

	PriceEntity findTopByInventoryIdAndEffectiveDateTimeLessThanAndIsDeletedOrderByEffectiveDateTimeDesc(
			long inventoryId, Instant now, boolean b);

	PriceEntity findByPriceIdAndIsDeleted(long priceId,
			boolean b);

	Page<PriceEntity> findByInventoryIdAndIsDeleted(long inventoryId, boolean b,
			Pageable pageableRequest);

	PriceEntity findTopByInventoryId(long inventoryId);

}
