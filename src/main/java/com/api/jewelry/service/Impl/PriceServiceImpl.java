package com.api.jewelry.service.Impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;

import org.apache.xmlbeans.UserType;
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
import com.api.jewelry.io.entity.InventoryEntity;
import com.api.jewelry.io.entity.UserEntity;
import com.api.jewelry.io.repositories.PriceRepository;
import com.api.jewelry.io.repositories.CustomerInventoryTransactionRepository;
import com.api.jewelry.io.repositories.InventoryRepository;
import com.api.jewelry.io.repositories.UserRepository;
import com.api.jewelry.service.PriceService;
import com.api.jewelry.ui.model.request.PriceRequestModel;
import com.api.jewelry.ui.model.response.PriceResponseModel;

@Transactional
@Service
public class PriceServiceImpl implements PriceService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	InventoryRepository inventoryRepository;
	
	@Autowired
	PriceRepository priceRepository;
	
	@Autowired
	CustomerInventoryTransactionRepository customerInventoryTransactionRepository;
	
	@Override
	public String deletePrice(long priceId) {
		// TODO Auto-generated method stub
		PriceEntity priceEntity = priceRepository.findTopByInventoryIdAndEffectiveDateTimeLessThanAndIsDeletedOrderByEffectiveDateTimeDesc(priceId,Instant.now(), false);
		if(priceEntity==null) throw new AppException("no data with this id");
		priceEntity.setDeleted(true);
		priceRepository.save(priceEntity);
		return "Deleted!";
	}

	@Override
	public PriceResponseModel updatePrice(long priceId,
			PriceRequestModel priceDetail) {
		// TODO Auto-generated method stub
		PriceEntity priceEntity = priceRepository.findTopByInventoryIdAndEffectiveDateTimeLessThanAndIsDeletedOrderByEffectiveDateTimeDesc(priceId, Instant.now(), false);
		if(priceEntity==null) throw new AppException("no data with this id");
		PriceResponseModel returnValue = new PriceResponseModel();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        priceEntity.setCustomerId(auth.getName());
		BeanUtils.copyProperties(priceDetail, priceEntity);
		PriceEntity savedPriceEntity = priceRepository.save(priceEntity);
		BeanUtils.copyProperties(savedPriceEntity, returnValue);
		return returnValue;
	}

	@Override
	public PriceResponseModel getPrice(long priceId) {
		// TODO Auto-generated method stub         
		PriceResponseModel returnValue = new PriceResponseModel();
		PriceEntity priceEntity = priceRepository.findTopByInventoryIdAndEffectiveDateTimeLessThanAndIsDeletedOrderByEffectiveDateTimeDesc(priceId, Instant.now(), false);
		if(priceEntity==null) throw new AppException("no data with this id");
		 List<CustomerInventoryTransactionEntity> customerInventoryTransactionEntities = customerInventoryTransactionRepository.findByInventoryIdAndIsDeleted(priceEntity.getInventoryId(), false);
		   double availableQuantity=0;
		   double outQuantity =0;
		   for(CustomerInventoryTransactionEntity customerInventoryTransactionEntity1:customerInventoryTransactionEntities) {   
			   if(customerInventoryTransactionEntity1.getTransactionType()=="In") {
				  availableQuantity=availableQuantity+ customerInventoryTransactionEntity1.getQuantity();
			   }
			   else if(customerInventoryTransactionEntity1.getTransactionType()=="Out") {
				   outQuantity = outQuantity+customerInventoryTransactionEntity1.getQuantity();
			   }                
		   }
		   double difference=availableQuantity-outQuantity;
//		   returnValue.setQuantity(difference);
		 
		   InventoryEntity inventoryEntity = inventoryRepository.findByInventoryIdAndIsDeleted(priceEntity.getInventoryId(), false);
		
		BeanUtils.copyProperties(priceEntity, returnValue);
		return returnValue;
	}

	@Override
	public List<PriceResponseModel> getAllPrices(int page, int limit,
			long inventoryId) {
		
		// TODO Auto-generated method stub
		List<PriceResponseModel> returnValue = new ArrayList<>();
		 if(page > 0) page = page - 1;   
		    Pageable pageableRequest = PageRequest.of(page, limit,Sort.by("priceId").ascending());
		    Page<PriceEntity> pricePage = null;    
//		    if("".equals(searchKey))
		    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		    UserEntity userEntity1 = userRepository.findByUserId(auth.getName());
		    String userType = null;
		    if(userEntity1!=null)
		    	userType = userEntity1.getUserType();
		  
		    		pricePage = priceRepository.findByInventoryIdAndIsDeleted(inventoryId,false,pageableRequest);
		    
		 
		   List<PriceEntity> priceEntities = pricePage.getContent();  
		    int totalPages = pricePage.getTotalPages();	    
		    for(PriceEntity priceEntity : priceEntities) {
		    	PriceResponseModel priceResponseModel = new PriceResponseModel();
		    	BeanUtils.copyProperties(priceEntity, priceResponseModel);
		    	InventoryEntity inventoryEntity = inventoryRepository.findByInventoryId(priceEntity.getInventoryId());
		    	if(inventoryEntity!=null) {
		    		priceResponseModel.setProductName(inventoryEntity.getProductName());
		    	}
		    	returnValue.add(priceResponseModel);
		    }
		return returnValue;
	}

	@Override
	public PriceResponseModel savePrice(
			PriceRequestModel priceDetail) {
		// TODO Auto-generated method stub
		
		PriceEntity priceEntity = new PriceEntity();
		PriceResponseModel returnValue = new PriceResponseModel();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        priceEntity.setCustomerId(priceDetail.getCustomerId());
		UserEntity userEntity = userRepository.findByUserId(auth.getName());
		BeanUtils.copyProperties(priceDetail, priceEntity);
	
		priceEntity.setCreatedBy(auth.getName());
		PriceEntity savedPriceEntity = priceRepository.save(priceEntity);
		BeanUtils.copyProperties(savedPriceEntity, returnValue);
		return returnValue;
	}
}
