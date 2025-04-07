package com.api.jewelry.service.Impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.api.jewelry.exception.AppException;
import com.api.jewelry.io.entity.PriceEntity;
import com.api.jewelry.io.entity.CustomerInventoryTransactionEntity;
import com.api.jewelry.io.entity.CustomerSoldInventoryDetailEntity;
import com.api.jewelry.io.entity.CustomerSoldInventoryEntity;
import com.api.jewelry.io.entity.InventoryEntity;
import com.api.jewelry.io.entity.UserEntity;
import com.api.jewelry.io.repositories.PriceRepository;
import com.api.jewelry.io.repositories.CustomerInventoryTransactionRepository;
import com.api.jewelry.io.repositories.CustomerSoldInventoryDetailRepository;
import com.api.jewelry.io.repositories.CustomerSoldInventoryRepository;
import com.api.jewelry.io.repositories.InventoryRepository;
import com.api.jewelry.io.repositories.UserRepository;
import com.api.jewelry.service.CustomerSoldInventoryService;
import com.api.jewelry.shared.GenerateRandomString;
import com.api.jewelry.ui.model.request.CustomerSoldInventoryDetailRequestModel;
import com.api.jewelry.ui.model.request.CustomerSoldInventoryRequestModel;
import com.api.jewelry.ui.model.response.CustomerSoldInventoryDetailResponseModel;
import com.api.jewelry.ui.model.response.CustomerSoldInventoryResponseModel;

@Transactional
@Service
public class CustomerSoldInventoryServiceImpl implements CustomerSoldInventoryService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	InventoryRepository inventoryRepository;
	
	@Autowired
	CustomerSoldInventoryRepository customerSoldInventoryRepository;
	
	@Autowired
	CustomerSoldInventoryDetailRepository customerSoldInventoryDetailRepository;
	
	@Autowired
	CustomerInventoryTransactionRepository customerInventoryTransactionRepository;
	
	@Autowired
	PriceRepository customerInventorySellPriceRepository;
	
	@Autowired
	GenerateRandomString generateRandomString;
	@Override
	public CustomerSoldInventoryResponseModel saveCustomerSoldInventory(
			CustomerSoldInventoryRequestModel customerSoldInventoryDetail) {
		// TODO Auto-generated method stub
		for(CustomerSoldInventoryDetailRequestModel customerSoldInventoryDetailResponseModel:customerSoldInventoryDetail.getCustomerSoldInventoryDetailRequestModels()) {
		   List<CustomerInventoryTransactionEntity> customerInventoryTransactionEntities = customerInventoryTransactionRepository.findByInventoryIdAndIsDeleted(customerSoldInventoryDetailResponseModel.getInventoryId(), false);

		   double availableQuantity=0;
		   double outQuantity=0;
		   for(CustomerInventoryTransactionEntity customerInventoryTransactionEntity:customerInventoryTransactionEntities) {   

			   if(customerInventoryTransactionEntity.getTransactionType().equals("In")) {
				  availableQuantity=availableQuantity+ customerInventoryTransactionEntity.getQuantity();

			   }
			   else if(customerInventoryTransactionEntity.getTransactionType().equals("Out")) {
				   outQuantity = outQuantity+customerInventoryTransactionEntity.getQuantity();

			   }                
		   }
		   double difference=availableQuantity-outQuantity;

		   if(difference<customerSoldInventoryDetailResponseModel.getQuantity()) {
			   return null;
		   }
		}
		CustomerSoldInventoryResponseModel returnValue = new CustomerSoldInventoryResponseModel();
		CustomerSoldInventoryEntity customerSoldInventoryEntity = new CustomerSoldInventoryEntity();
		BeanUtils.copyProperties(customerSoldInventoryDetail, customerSoldInventoryEntity);
		String sellNumber = "SN-";
		boolean sellNumberUnique = false;
		while(!sellNumberUnique) {
			sellNumber =sellNumber + generateRandomString.generateOrderNumber(7);
			if (customerSoldInventoryRepository.findBySellNumber(sellNumber) == null) {
				sellNumberUnique = true;
			}
			
		}
		customerSoldInventoryEntity.setSellNumber(sellNumber);
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    UserEntity userEntity = userRepository.findByUserId(auth.getName());
	 
    	customerSoldInventoryEntity.setCreatedBy(auth.getName());
		CustomerSoldInventoryEntity savedCustomerSoldInventoryEntity = customerSoldInventoryRepository.save(customerSoldInventoryEntity);
	    	
		for(CustomerSoldInventoryDetailRequestModel customerSoldInventoryDetailResponseModel:customerSoldInventoryDetail.getCustomerSoldInventoryDetailRequestModels()) {
			CustomerSoldInventoryDetailEntity customerSoldInventoryDetailEntity = new CustomerSoldInventoryDetailEntity();
			BeanUtils.copyProperties(customerSoldInventoryDetailResponseModel, customerSoldInventoryDetailEntity);
//	    	PriceEntity price = customerInventorySellPriceRepository.findTopByInventoryIdAndEffectiveDateTimeLessThanAndIsDeletedOrderByEffectiveDateTimeDesc(customerSoldInventoryDetailResponseModel.getInventoryId(), auth.getName(), Instant.now(), false);						
//	    	if(price != null) {
//	    		customerSoldInventoryDetailEntity.setSellPrice(price.getPrice());
//	    	}
			customerSoldInventoryDetailEntity.setCustomerSoldInventoryId(savedCustomerSoldInventoryEntity.getCustomerSoldInventoryId());
			customerSoldInventoryDetailEntity.setCreatedBy(auth.getName());
			CustomerSoldInventoryDetailEntity savedCustomerSoldInventoryDetailEntity = customerSoldInventoryDetailRepository.save(customerSoldInventoryDetailEntity);

		    CustomerInventoryTransactionEntity customerInventoryTransactionEntity = new CustomerInventoryTransactionEntity();
		    BeanUtils.copyProperties(customerSoldInventoryDetailResponseModel, customerInventoryTransactionEntity);
		    customerInventoryTransactionEntity.setTransactionType("Out");
		    customerInventoryTransactionEntity.setQuantity(customerSoldInventoryDetailResponseModel.getQuantity());
		    customerInventoryTransactionEntity.setInventoryId(customerSoldInventoryDetailResponseModel.getInventoryId());
		    customerInventoryTransactionEntity.setTransactionDate(customerSoldInventoryDetail.getSoldDate());
		  
		   
		    customerInventoryTransactionEntity.setCreatedBy(auth.getName());
		    customerInventoryTransactionRepository.save(customerInventoryTransactionEntity);   
		}
		BeanUtils.copyProperties(savedCustomerSoldInventoryEntity, returnValue);
		
		return returnValue;
	}

	@Override
	public List<CustomerSoldInventoryResponseModel> getAllCustomerSoldInventories(int page, int limit,
			String searchKey, long companyId, String userType) {
		// TODO Auto-generated method stub
		List<CustomerSoldInventoryResponseModel> returnValue = new ArrayList<>();
		 if(page > 0) page = page - 1; 
		 Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		 
		    Pageable pageableRequest = PageRequest.of(page, limit,Sort.by("customerSoldInventoryId").ascending());
		    Page<CustomerSoldInventoryEntity> customerSoldInventoryPage = null;
		    UserEntity userEntity1 = userRepository.findByUserId(auth.getName());
		    String userType1=null;
		    if(userEntity1!=null) 
		    	userType1 = userEntity1.getUserType();
		    if("Admin".equals(userType1)) {
		    	if(companyId!=0)
		            customerSoldInventoryPage = customerSoldInventoryRepository.findByCompanyIdAndIsDeleted(companyId,false, pageableRequest);//.findAll(pageableRequest);
		    	else
		    		customerSoldInventoryPage = customerSoldInventoryRepository.findByIsDeleted(false, pageableRequest);
		    }
		
		    List<CustomerSoldInventoryEntity> customerSoldInventoryEntities = customerSoldInventoryPage.getContent();
		    
		    
		    
		    int totalPages = customerSoldInventoryPage.getTotalPages();	  
		    for(CustomerSoldInventoryEntity customerSoldInventoryEntity : customerSoldInventoryEntities) {
		    	
		    	CustomerSoldInventoryResponseModel customerSoldInventoryResponseModel = new CustomerSoldInventoryResponseModel(); 
		    	BeanUtils.copyProperties(customerSoldInventoryEntity, customerSoldInventoryResponseModel);
		    	
		    	UserEntity userEntity= userRepository.findByUserId(customerSoldInventoryEntity.getCreatedBy());
				if(userEntity!=null)
					customerSoldInventoryResponseModel.setCustomer(userEntity.getFirstName()+" "+userEntity.getLastName());
		    	else
		    		customerSoldInventoryResponseModel.setCustomer("");
				List<CustomerSoldInventoryDetailResponseModel> customerSoldInventoryDetailResponseModels = new ArrayList<>();
				List<CustomerSoldInventoryDetailEntity> customerSoldInventoryDetailEntities = customerSoldInventoryDetailRepository.findByCustomerSoldInventoryIdAndIsDeleted(customerSoldInventoryEntity.getCustomerSoldInventoryId(),false);
				for(CustomerSoldInventoryDetailEntity customerSoldInventoryDetailEntity:customerSoldInventoryDetailEntities) {
					CustomerSoldInventoryDetailResponseModel customerSoldInventoryDetailResponseModel = new CustomerSoldInventoryDetailResponseModel();
					BeanUtils.copyProperties(customerSoldInventoryDetailEntity, customerSoldInventoryDetailResponseModel);
					InventoryEntity inventoryEntity = inventoryRepository.findByInventoryIdAndIsDeleted(customerSoldInventoryDetailEntity.getInventoryId(), false);
					if(inventoryEntity!=null) {
//						customerSoldInventoryDetailResponseModel.setInventoryName(inventoryEntity.getInventoryBrandName()+" "+inventoryEntity.getInventoryGenericName());
					}
					customerSoldInventoryDetailResponseModels.add(customerSoldInventoryDetailResponseModel);
				}
				customerSoldInventoryResponseModel.setCustomerSoldInventoryDetailResponseModels(customerSoldInventoryDetailResponseModels);
		    	
				if(returnValue.size() == 0) {
		    		customerSoldInventoryResponseModel.setTotalPages(totalPages);
		    	}
		    	
		    	returnValue.add(customerSoldInventoryResponseModel);
		    }
		return returnValue;
	}

	@Override
	public CustomerSoldInventoryResponseModel getCustomerSoldInventory(long customerSoldInventoryId) {
		    	
		    	CustomerSoldInventoryResponseModel returnValue = new CustomerSoldInventoryResponseModel(); 
		    	CustomerSoldInventoryEntity customerSoldInventoryEntity = customerSoldInventoryRepository.findByCustomerSoldInventoryIdAndIsDeleted(customerSoldInventoryId,false);
		    	if(customerSoldInventoryEntity==null) throw new AppException("No data with this id");
		    	BeanUtils.copyProperties(customerSoldInventoryEntity, returnValue);
		    	
		    	UserEntity userEntity= userRepository.findByUserId(customerSoldInventoryEntity.getCreatedBy());
				if(userEntity!=null)
					returnValue.setCustomer(userEntity.getFirstName()+" "+userEntity.getLastName());
		    	else
		    		returnValue.setCustomer("");
				List<CustomerSoldInventoryDetailResponseModel> customerSoldInventoryDetailResponseModels = new ArrayList<>();
				List<CustomerSoldInventoryDetailEntity> customerSoldInventoryDetailEntities = customerSoldInventoryDetailRepository.findByCustomerSoldInventoryIdAndIsDeleted(customerSoldInventoryEntity.getCustomerSoldInventoryId(),false);
				for(CustomerSoldInventoryDetailEntity customerSoldInventoryDetailEntity:customerSoldInventoryDetailEntities) {
					CustomerSoldInventoryDetailResponseModel customerSoldInventoryDetailResponseModel = new CustomerSoldInventoryDetailResponseModel();
				   BeanUtils.copyProperties(customerSoldInventoryDetailEntity, customerSoldInventoryDetailResponseModel);
					InventoryEntity inventoryEntity = inventoryRepository.findByInventoryIdAndIsDeleted(customerSoldInventoryDetailEntity.getInventoryId(), false);
					if(inventoryEntity!=null) {
//						customerSoldInventoryDetailResponseModel.setInventoryName(inventoryEntity.getInventoryBrandName()+" "+inventoryEntity.getInventoryGenericName());
					}
					customerSoldInventoryDetailResponseModels.add(customerSoldInventoryDetailResponseModel);
				}
					returnValue.setCustomerSoldInventoryDetailResponseModels(customerSoldInventoryDetailResponseModels);

		return returnValue;
	}

	@Override
	public CustomerSoldInventoryResponseModel updateCustomerInventoryTransaction(long customerSoldInventoryId,
			CustomerSoldInventoryRequestModel customerSoldInventoryDetail) {
		// TODO Auto-generated method stub
		
		CustomerSoldInventoryEntity customerSoldInventoryEntity = customerSoldInventoryRepository.findByCustomerSoldInventoryIdAndIsDeleted(customerSoldInventoryId, false);
		if(customerSoldInventoryEntity==null) throw new AppException("no data with this id");
		
		List<CustomerSoldInventoryDetailEntity> customerSoldInventoryDetailEntities = customerSoldInventoryDetailRepository.findByCustomerSoldInventoryIdAndIsDeleted(customerSoldInventoryId, false);
		for(CustomerSoldInventoryDetailEntity customerSoldInventoryDetailEntity:customerSoldInventoryDetailEntities) {
			customerSoldInventoryDetailEntity.setDeleted(true);
			customerSoldInventoryDetailRepository.save(customerSoldInventoryDetailEntity);
		}
		
		for(CustomerSoldInventoryDetailRequestModel customerSoldInventoryDetailResponseModel:customerSoldInventoryDetail.getCustomerSoldInventoryDetailRequestModels()) {
			List<CustomerInventoryTransactionEntity> customerInventoryTransactionEntities = customerInventoryTransactionRepository.findByInventoryIdAndIsDeleted(customerSoldInventoryDetailResponseModel.getInventoryId(), false);
			   
			double availableQuantity=0;
			   double outQuantity =0;
			   for(CustomerInventoryTransactionEntity customerInventoryTransactionEntity:customerInventoryTransactionEntities) {   
				   if(customerInventoryTransactionEntity.getTransactionType()=="In") {
					  availableQuantity=availableQuantity+ customerInventoryTransactionEntity.getQuantity();
				   }
				   else if(customerInventoryTransactionEntity.getTransactionType()=="Out") {
					   outQuantity = outQuantity+customerInventoryTransactionEntity.getQuantity();
				   } 
			   }
			   double difference = availableQuantity-customerSoldInventoryDetailResponseModel.getQuantity();
			   if(customerSoldInventoryDetailResponseModel.getQuantity()>difference) {
				   return null;
			   }
			}
		CustomerSoldInventoryResponseModel returnValue = new CustomerSoldInventoryResponseModel();
		BeanUtils.copyProperties(customerSoldInventoryDetail, customerSoldInventoryEntity);
		CustomerSoldInventoryEntity savedCustomerSoldInventoryEntity = customerSoldInventoryRepository.save(customerSoldInventoryEntity);
		for(CustomerSoldInventoryDetailRequestModel customerSoldInventoryDetailResponseModel:customerSoldInventoryDetail.getCustomerSoldInventoryDetailRequestModels()) {
		    
			CustomerSoldInventoryDetailEntity customerSoldInventoryDetailEntity = new CustomerSoldInventoryDetailEntity();
			BeanUtils.copyProperties(customerSoldInventoryDetailResponseModel, customerSoldInventoryDetailEntity);
			customerSoldInventoryDetailEntity.setCustomerSoldInventoryId(savedCustomerSoldInventoryEntity.getCustomerSoldInventoryId());
			CustomerSoldInventoryDetailEntity savedCustomerSoldInventoryDetailEntity = customerSoldInventoryDetailRepository.save(customerSoldInventoryDetailEntity);
			
		}
		BeanUtils.copyProperties(savedCustomerSoldInventoryEntity, returnValue);
		return returnValue;
	}

	@Override
	public String deleteCustomerSoldInventory(long customerSoldInventoryId) {
		// TODO Auto-generated method stub
		CustomerSoldInventoryEntity customerSoldInventoryEntity = customerSoldInventoryRepository.findByCustomerSoldInventoryIdAndIsDeleted(customerSoldInventoryId, false);
		if(customerSoldInventoryEntity==null) throw new AppException("no data with this id");
		List<CustomerSoldInventoryDetailEntity> customerSoldInventoryDetailEntities = customerSoldInventoryDetailRepository.findByCustomerSoldInventoryIdAndIsDeleted(customerSoldInventoryId, false);
		for(CustomerSoldInventoryDetailEntity customerSoldInventoryDetailEntity:customerSoldInventoryDetailEntities) {
			customerSoldInventoryDetailEntity.setDeleted(true);
			customerSoldInventoryDetailRepository.save(customerSoldInventoryDetailEntity);
		}
		customerSoldInventoryEntity.setDeleted(true);
		customerSoldInventoryRepository.save(customerSoldInventoryEntity);
		return "Deleted!";
	}

}
