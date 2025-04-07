package com.api.jewelry.service.Impl;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.api.jewelry.exception.AppException;
import com.api.jewelry.io.entity.InventoryCostPriceListEntity;
import com.api.jewelry.io.entity.InventoryEntity;
import com.api.jewelry.io.entity.InventorySellPriceListEntity;
import com.api.jewelry.io.entity.TransactionEntity;
import com.api.jewelry.io.entity.OrderItemEntity;
import com.api.jewelry.io.entity.OrderTimeLimitEntity;
import com.api.jewelry.io.entity.UserEntity;
import com.api.jewelry.io.repositories.InventoryCostPriceListRepository;
import com.api.jewelry.io.repositories.InventoryRepository;
import com.api.jewelry.io.repositories.InventorySellPriceListRepository;
import com.api.jewelry.io.repositories.TransactionRepository;
import com.api.jewelry.io.repositories.OrderItemRepository;
import com.api.jewelry.io.repositories.OrderTimeLimitRepository;
import com.api.jewelry.io.repositories.UserRepository;
import com.api.jewelry.service.InventoryTransactionService;
import com.api.jewelry.shared.dto.InventoryItemsDto;
import com.api.jewelry.ui.model.request.InventoryItemsRequestModel;
import com.api.jewelry.ui.model.request.InventorySellPriceRequestModel;
import com.api.jewelry.ui.model.request.InventoryTransactionUpdateRequestModel;
import com.api.jewelry.ui.model.response.CustomerReportResponseModel;
import com.api.jewelry.ui.model.response.TransactionHistoryResponse;

@Transactional
@Service
public class InventoryTransactionServiceImpl implements InventoryTransactionService {
	
	@Autowired
	TransactionRepository inventoryTransactionDetailRepository;
	
	@Autowired
	InventoryRepository inventoryRepository;
	
	@Autowired
	InventoryCostPriceListRepository inventoryCostPriceListRepository;
	
	@Autowired
	InventorySellPriceListRepository inventorySellPriceListRepository;

	@Autowired
	OrderItemRepository orderItemRepository;

	@Autowired
	OrderTimeLimitRepository orderTimeLimitRepository;
	
	@Autowired
	EntityManager entityManager;
	
	@Autowired
	UserRepository userRepository;
	
	@Override
	public void orderPreOrderManagementJob() {
		OrderTimeLimitEntity orderTimeLimitEntity = orderTimeLimitRepository.findTopByIsDeleted(false);
		if(orderTimeLimitEntity==null) 
			return;
		Long orderPaymentLimitTime=Long.valueOf(orderTimeLimitEntity.getTimeLimit());

		List<Long> inventoryIdsList = inventoryRepository.findByIsDeleted(false).stream().map(i->i.getInventoryId()).collect(Collectors.toList());
		Long[] inventoryIds=inventoryIdsList.toArray(new Long[inventoryIdsList.size()]);
//		List<Long> updatedInventotyIdsList=orderItemRepository.findByInventoryIdInAndOrderQuantityGreaterThanAndIsDeletedOrderByCreatedAt(inventoryIds, 0, false).stream().filter(orderItemEntity->{
//			
//			return false;
//		}).map(oi->oi.getInventoryId()).collect(Collectors.toList());
//			
//		Long[] updatedInventotyIds=updatedInventotyIdsList.toArray(new Long[updatedInventotyIdsList.size()]);
//		List<InventoryEntity> updatedInventoryEntities=inventoryRepository.findByInventoryIdIn(updatedInventotyIds);
//		for(InventoryEntity inventoryEntity: updatedInventoryEntities) {
////			double remainedQuantity=this.orderPreOrderManagementLogic(inventoryEntity, inventoryEntity.getAvailableQuantity());
//			// Update inventory stock level with the remaining receivableQuantity
////			inventoryEntity.setAvailableQuantity(remainedQuantity);
//			inventoryRepository.save(inventoryEntity);
//		}
		
	}		
	public double orderPreOrderManagementLogic(InventoryEntity inventoryEntity, double receivableQuantity) {
		
		OrderTimeLimitEntity orderTimeLimitEntity = orderTimeLimitRepository.findTopByIsDeleted(false);
		if(orderTimeLimitEntity==null) 
			return receivableQuantity;
		Long orderPaymentLimitTime=Long.valueOf(orderTimeLimitEntity.getTimeLimit());

		System.out.println("ontimePaymentOrderItemEntities receivableQuantity: "+receivableQuantity);
		// Priority #2 Current orders within payment time limit ordered by orderDateTime
//		List<OrderItemEntity> currentOrdersWithinPaymentTimeLimit=orderItemRepository.findByInventoryIdAndPreOrderQuantityGreaterThanAndIsDeletedOrderByCreatedAt(inventoryEntity.getInventoryId(), 0, false).stream().filter(orderItemEntity->{
//			return Date.from(Instant.now()).getTime() - Date.from(orderItemEntity.getCreatedAt()).getTime() <= orderPaymentLimitTime;
//		}).collect(Collectors.toList());
//		receivableQuantity = this.convertPreOrderToOrder(inventoryEntity, currentOrdersWithinPaymentTimeLimit, receivableQuantity);
		System.out.println("currentOrdersWithinPaymentTimeLimit receivableQuantity: "+receivableQuantity);

		System.out.println("delayedPaymentsOrderedByDifferenceInTimeBetweenOrderAndPaymentArr receivableQuantity: "+receivableQuantity);

		// Priority #4 Unpayed orders order by order date DESC
//		List<OrderItemEntity> unpayedOrdersOrderByOrderDateDesc=orderItemRepository.findByInventoryIdAndPreOrderQuantityGreaterThanAndIsDeletedOrderByCreatedAtDesc(inventoryEntity.getInventoryId(), 0, false);
//		receivableQuantity = this.convertPreOrderToOrder(inventoryEntity, unpayedOrdersOrderByOrderDateDesc, receivableQuantity);
//		System.out.println("unpayedOrdersOrderByOrderDateDesc receivableQuantity: "+receivableQuantity);

		return receivableQuantity;

	}
	public double convertPreOrderToOrder(InventoryEntity inventoryEntity, List<OrderItemEntity> 
	orderItemEntities, double receivableQuantity) {		
			
		// List<OrderItemEntity> orderItemEntities=orderItemRepository.findByInventoryIdAndPreOrderQuantityGreaterThanAndIsDeletedOrderByCreatedAt(inventoryEntity.getInventoryId(), 0, false);
		for(OrderItemEntity orderItemEntity: orderItemEntities) {
			double addedQuantity=0;
			orderItemRepository.save(orderItemEntity);
			receivableQuantity -= addedQuantity;
			if(receivableQuantity <= 0)
				break;
		}
		return receivableQuantity;
	}

	@Override
	public List<InventoryItemsDto> getTransactionHistory(int page, int limit) {
		
		List<InventoryItemsDto> returnValue = new ArrayList<>();
	    
	    if(page > 0) page = page - 1;
	   
	    Pageable pageableRequest = PageRequest.of(page, limit,Sort.by("inventoryTransactionDetailId").descending());
	    
	    Page<TransactionEntity> transactionHistoryPage = inventoryTransactionDetailRepository.findAll(pageableRequest);
	    int totalPages = transactionHistoryPage.getTotalPages();
	    List<TransactionEntity> transactionHistories = transactionHistoryPage.getContent();
	    
	    for(TransactionEntity transactionHistory : transactionHistories) {
	    	InventoryItemsDto inventoryItemsDto = new InventoryItemsDto(); 
	    	BeanUtils.copyProperties(transactionHistory, inventoryItemsDto); 
	    	
	    	InventoryEntity inventoryEntity = inventoryRepository.findByInventoryId(transactionHistory.getInventoryId());
			if (inventoryEntity != null) {
//				inventoryItemsDto.setInventoryGenericName(inventoryEntity.getInventoryGenericName());
//				inventoryItemsDto.setInventoryBrandName(inventoryEntity.getInventoryBrandName());
//				inventoryItemsDto.setMeasuringUnit(inventoryEntity.getMeasuringUnit());
//				inventoryItemsDto.setInventoryType(inventoryEntity.getInventoryType());
//				inventoryItemsDto.setDosageForm(inventoryEntity.getDosageForm());
//				inventoryItemsDto.setStrength(inventoryEntity.getStrength());
//				inventoryItemsDto.setVolume(inventoryEntity.getVolume());
				returnValue.add(inventoryItemsDto);
			}
			
	    	if(returnValue.size() == 0) {
	    		inventoryItemsDto.setTotalPages(totalPages);
	    	}
	    	

	    }
	    
		return returnValue;
	}

	@Override
	public InventoryTransactionUpdateRequestModel updateInventoryTransactionHistory(long inventoryTransactionDetailId,
			InventoryTransactionUpdateRequestModel newTransactionDetail) {
	
		return null;
	}

	@Override
	public List<InventoryItemsDto> searchTransactionHistory(long inventoryId, int page, int limit) {
		
		List<InventoryItemsDto> returnValue = new ArrayList<>();
	    
	    if(page > 0) page = page - 1;
	   
	    Pageable pageableRequest = PageRequest.of(page, limit,Sort.by("inventoryTransactionDetailId").descending());
	    
	    Page<TransactionEntity> transactionHistoryPage = inventoryTransactionDetailRepository.findByInventoryId(inventoryId,pageableRequest);
	    int totalPages = transactionHistoryPage.getTotalPages();
	    List<TransactionEntity> transactionHistories = transactionHistoryPage.getContent();
	    
	    for(TransactionEntity transactionHistory : transactionHistories) {
	    	InventoryItemsDto inventoryItemsDto = new InventoryItemsDto(); 
	    	BeanUtils.copyProperties(transactionHistory, inventoryItemsDto); 
	    	
	    	InventoryEntity inventoryEntity = inventoryRepository.findByInventoryId(transactionHistory.getInventoryId());
//	    	inventoryItemsDto.setInventoryGenericName(inventoryEntity.getInventoryGenericName());
//	    	inventoryItemsDto.setInventoryBrandName(inventoryEntity.getInventoryBrandName() );
//	    	inventoryItemsDto.setMeasuringUnit(inventoryEntity.getMeasuringUnit());
//	    	inventoryItemsDto.setInventoryType(inventoryEntity.getInventoryType());
	    	if(returnValue.size() == 0) {
	    		inventoryItemsDto.setTotalPages(totalPages);
	    	}
	    	returnValue.add(inventoryItemsDto);
	    }
	    
		return returnValue;
	}

	@Override
	public TransactionHistoryResponse getTransactionByInventoryTransactionDetailId(long inventoryTransactionDetailId) {
		
		TransactionHistoryResponse returnValue = new TransactionHistoryResponse(); 
    	 
		TransactionEntity transaction = inventoryTransactionDetailRepository.findByTransactionId(inventoryTransactionDetailId);
		InventoryEntity inventoryEntity = inventoryRepository.findByInventoryId(transaction.getInventoryId());
		BeanUtils.copyProperties(transaction, returnValue);
//		returnValue.setInventoryGenericName(inventoryEntity.getInventoryGenericName());
//		returnValue.setInventoryBrandName(inventoryEntity.getInventoryBrandName() );
//		returnValue.setMeasuringUnit(inventoryEntity.getMeasuringUnit());
//		returnValue.setInventoryType(inventoryEntity.getInventoryType());
		
		return returnValue;
	}
	@Override
	public String InsertInventoryItems(List<InventoryItemsRequestModel> inventoryItemsDtos) {
		
		int listSize = inventoryItemsDtos.size();
		int count = 0;
		for(InventoryItemsRequestModel inventoryItem : inventoryItemsDtos) {
			
			double receivableQuantity = inventoryItem.getQuantity();
				InventoryEntity inventoryEntity = inventoryRepository.findByInventoryId(inventoryItem.getInventoryId());
				if(inventoryEntity == null) 
					continue;
				
			if (inventoryItem.getTransactionType().equals("In")) {
				double remainedQuantity = this.orderPreOrderManagementLogic(inventoryEntity, receivableQuantity);
				// Update inventory stock level with the remaining receivableQuantity
				if (remainedQuantity > 0) {
					
//					inventoryEntity.setAvailableQuantity(inventoryEntity.getAvailableQuantity() + remainedQuantity);
					inventoryRepository.save(inventoryEntity);
				}
			}
			
			else {
//				double remainedQuantity = inventoryEntity.getAvailableQuantity() - inventoryItem.getQuantity();
//				if (remainedQuantity < 0)
//					inventoryEntity.setAvailableQuantity(0);
//				else
//					inventoryEntity.setAvailableQuantity(remainedQuantity);
				
				inventoryRepository.save(inventoryEntity);
				}
				/*if(inventoryEntity == null) 
					continue;
				
				Long paymentDateTime=Date.from(Instant.now()).getTime();
				Long orderPaymentLimitTime=Long.valueOf(10000);
				// Priority #1 Ontime Payments ordered by orderDateTime
				List<OrderItemEntity> ontimePaymentOrderItemEntities=orderItemRepository.findByInventoryIdAndPreOrderQuantityGreaterThanAndIsDeletedOrderByCreatedAt(inventoryEntity.getInventoryId(), 0, false).stream().filter(o->{
					return paymentDateTime - Date.from(o.getCreatedAt()).getTime() <= orderPaymentLimitTime;
				}).collect(Collectors.toList());
				receivableQuantity = this.convertPreOrderToOrder(inventoryEntity, ontimePaymentOrderItemEntities, receivableQuantity);

				// Priority #2 Current orders within payment time limit ordered by orderDateTime
				List<OrderItemEntity> currentOrdersWithinPaymentTimeLimit=orderItemRepository.findByInventoryIdAndPreOrderQuantityGreaterThanAndIsDeletedOrderByCreatedAt(inventoryEntity.getInventoryId(), 0, false).stream().filter(o->{
					return Date.from(Instant.now()).getTime() - Date.from(o.getCreatedAt()).getTime() <= orderPaymentLimitTime;
				}).collect(Collectors.toList());
				receivableQuantity = this.convertPreOrderToOrder(inventoryEntity, currentOrdersWithinPaymentTimeLimit, receivableQuantity);
				
				// Priority #3 Delayed payments ordered by difference in time between order & payment
				List<OrderItemEntity> delayedPaymentsOrederItems=orderItemRepository.findByInventoryIdAndPreOrderQuantityGreaterThanAndIsDeletedOrderByCreatedAt(inventoryEntity.getInventoryId(), 0, false).stream().map(o->{
					if(paymentDateTime!=null && (paymentDateTime - Date.from(o.getCreatedAt()).getTime() > orderPaymentLimitTime))	
						o.setOrderPaymentDifferenceInTime(paymentDateTime - Date.from(o.getCreatedAt()).getTime());
					return o;
				}).collect(Collectors.toList());
				OrderItemEntity[] delayedPaymentsOrderedByDifferenceInTimeBetweenOrderAndPaymentArr=delayedPaymentsOrederItems.toArray(new OrderItemEntity[delayedPaymentsOrederItems.size()]);
				Arrays.sort(delayedPaymentsOrderedByDifferenceInTimeBetweenOrderAndPaymentArr, Comparator.comparing(OrderItemEntity::getOrderPaymentDifferenceInTime));
				List<OrderItemEntity> delayedPaymentsOrderedByDifferenceInTimeBetweenOrderAndPayment=Arrays.asList(delayedPaymentsOrderedByDifferenceInTimeBetweenOrderAndPaymentArr);
				receivableQuantity = this.convertPreOrderToOrder(inventoryEntity, delayedPaymentsOrderedByDifferenceInTimeBetweenOrderAndPayment, receivableQuantity);

				// Priority #4 Unpayed orders order by order date desc
				List<OrderItemEntity> unpayedOrdersOrderByOrderDateDesc=orderItemRepository.findByInventoryIdAndPreOrderQuantityGreaterThanAndIsDeletedOrderByCreatedAtDesc(inventoryEntity.getInventoryId(), 0, false);
				receivableQuantity = this.convertPreOrderToOrder(inventoryEntity, unpayedOrdersOrderByOrderDateDesc, receivableQuantity);

				// Update inventory stock level with the remaining receivableQuantity
				if(receivableQuantity > 0) {

					inventoryEntity.setAvailableQuantity(inventoryEntity.getAvailableQuantity() + receivableQuantity);
					inventoryRepository.save(inventoryEntity);
				}

			inventoryRepository.save(inventoryEntity);
			*/
			TransactionEntity inventoryTransactionDetailEntity = new TransactionEntity();
			BeanUtils.copyProperties(inventoryItem, inventoryTransactionDetailEntity);
			inventoryTransactionDetailRepository.save(inventoryTransactionDetailEntity);
			
			if (inventoryItem.getTransactionType().equals("In")) {
				InventoryCostPriceListEntity inventoryCostPriceListEntity = new InventoryCostPriceListEntity();
				BeanUtils.copyProperties(inventoryItem, inventoryCostPriceListEntity);
				inventoryCostPriceListRepository.save(inventoryCostPriceListEntity);
				
				InventorySellPriceListEntity inventorySellPriceListEntity = new InventorySellPriceListEntity();
				BeanUtils.copyProperties(inventoryItem, inventorySellPriceListEntity);
				inventorySellPriceListEntity.setEffectiveDateTime(Instant.now());
				inventorySellPriceListRepository.save(inventorySellPriceListEntity);
			}
			
			count++;
		}
		
		String returnvalue = "";
		if( listSize == count) {
			returnvalue = "Transaction List Saved";
		}else {
			returnvalue = "List Saving Failed";
		}
		return returnvalue;
	}
	
	@Override
	public InventorySellPriceRequestModel insertInventorySellPrice(InventorySellPriceRequestModel requestDetail) {
		
		InventorySellPriceRequestModel returnValue = new InventorySellPriceRequestModel();

		InventoryEntity inventoryEntity = inventoryRepository.findByInventoryId(requestDetail.getInventoryId());
		if (inventoryEntity == null)
			throw new AppException("No record with this id");
		
		InventorySellPriceListEntity inventorySellPriceListEntity = new InventorySellPriceListEntity();
		BeanUtils.copyProperties(requestDetail, inventorySellPriceListEntity);
		InventorySellPriceListEntity savedDetail = inventorySellPriceListRepository.save(inventorySellPriceListEntity);
		
		BeanUtils.copyProperties(savedDetail, returnValue);
		

		return returnValue;
	}
	
	
	@Override
	public List<CustomerReportResponseModel> customerInventoryReport(int page, int limit, String fromDate, String toDate,
	        long inventoryId, String customerId) {
		List<CustomerReportResponseModel> returnValue = new ArrayList<>();
		
		Instant toDateInstant = null;
		Instant fromDateInstant = null;
		if (!fromDate.equals("")) {
			LocalDate startDateInLocal = LocalDate.parse(fromDate.split(" ")[0]);
			fromDateInstant = startDateInLocal.atStartOfDay(ZoneId.systemDefault()).toInstant();
			fromDateInstant = fromDateInstant.plus(1, ChronoUnit.DAYS);
			
		}
		
		if (!toDate.equals("")) {
			LocalDate endDateInLocal = LocalDate.parse(toDate.split(" ")[0]);
			toDateInstant = endDateInLocal.atStartOfDay(ZoneId.systemDefault()).toInstant();
			toDateInstant = toDateInstant.plus(1, ChronoUnit.DAYS);
			
		}
		
		List<Object[]> results = new ArrayList<Object[]>();
		if (page > 0)
			page = page - 1;
		
		String attributesInfo = "i.inventoryId, i.inventoryGenericName, i.strength, i.dosageForm, cit.quantity, cit.transactionType, cit.customerId";
		
		String fromCondition = " from inventory i, customer_inventory_transaction cit";
		
		String whereCondition = " where i.inventoryId=cit.inventoryId AND (i.inventoryId=:inventoryId OR 0=:inventoryId)"
		        + " AND (cit.customerId=:customerId OR ''=:customerId) AND (cit.transactionDate>=:fromDateInstant OR "
		        + "null=:fromDateInstant) AND (cit.transactionDate<=:toDateInstant OR null=:toDateInstant)"
		        + " AND i.isDeleted=0 order by cit.transactionDate desc";
		
		results = entityManager.createQuery("SELECT " + attributesInfo + fromCondition + whereCondition, Object[].class)
		        .setParameter("toDateInstant", toDateInstant).setParameter("fromDateInstant", fromDateInstant)
		        .setParameter("inventoryId", inventoryId).setParameter("customerId", customerId).getResultList();
		
		for (Object[] result : results) {
			CustomerReportResponseModel customerReportResponseModel = new CustomerReportResponseModel();
			
			List<CustomerReportResponseModel> checkInventory = returnValue.stream()
			        .filter(inventory -> inventory.getInventoryId() == (long) result[0]).collect(Collectors.toList());
			
			if (checkInventory.size() == 0) {
				customerReportResponseModel.setInventoryId((long) result[0]);
				customerReportResponseModel
				        .setInventoryName((String) result[1] + " " + (String) result[2] + " " + (String) result[3]);
				
				customerReportResponseModel.setCustomerId((String)result[6]);
				UserEntity userEntity = userRepository.findByUserId((String)result[6]);
				if (userEntity != null)
					customerReportResponseModel.setCustomerName(userEntity.getFirstName()+" "+userEntity.getLastName());;
				
				if ("In".equals(result[5]))
					customerReportResponseModel.setPurchasedQuantity((double) result[4]);
				
				if ("Out".equals(result[5]))
					customerReportResponseModel.setSoldQuantity((double) result[4]);
				
				long iId = (long) result[0];
				//				double outQuantity = (double) entityManager.createQuery(
				//				    "SELECT sum(quantity) from customer_inventory_transaction where inventoryId=:iId and transactionType='Out'")
				//				        .setParameter("iId", iId).getSingleResult();
				
				double availableQuantity = (double) entityManager
				        .createQuery(
				            "SELECT sum(quantity) from customer_inventory_transaction where inventoryId=:iId")
				        .setParameter("iId", iId).getSingleResult();
				customerReportResponseModel.setAvailableQuantity(availableQuantity);
				returnValue.add(customerReportResponseModel);
				

			}
			
			else {
				if ("In".equals(result[5]))
					returnValue.stream().filter(inventory -> inventory.getInventoryId() == (long) result[0])
					        .collect(Collectors.toList()).get(0)
					        .setPurchasedQuantity(checkInventory.get(0).getPurchasedQuantity() + (double) result[4]);
				if ("Out".equals(result[5]))
					returnValue.stream().filter(inventory -> inventory.getInventoryId() == (long) result[0])
					        .collect(Collectors.toList()).get(0)
					        .setSoldQuantity(checkInventory.get(0).getSoldQuantity() + (double) result[4]);
				
			}
			
		}
		
		return returnValue;
	}

}
