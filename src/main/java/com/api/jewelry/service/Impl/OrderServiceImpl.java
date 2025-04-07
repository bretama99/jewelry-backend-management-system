package com.api.jewelry.service.Impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import org.apache.tomcat.util.http.parser.Authorization;
import org.jboss.logging.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.api.jewelry.exception.AppException;
import com.api.jewelry.io.entity.InventoryCostPriceListEntity;
import com.api.jewelry.io.entity.InventoryEntity;
import com.api.jewelry.io.entity.InventorySellPriceListEntity;
import com.api.jewelry.io.entity.TransactionEntity;
import com.api.jewelry.io.entity.OrderEntity;
import com.api.jewelry.io.entity.OrderItemEntity;
import com.api.jewelry.io.entity.UserEntity;
import com.api.jewelry.io.repositories.InventoryCostPriceListRepository;
import com.api.jewelry.io.repositories.InventoryRepository;
import com.api.jewelry.io.repositories.InventorySellPriceListRepository;
import com.api.jewelry.io.repositories.TransactionRepository;
import com.api.jewelry.io.repositories.OrderItemRepository;
import com.api.jewelry.io.repositories.OrderRepository;
import com.api.jewelry.io.repositories.UserRepository;
import com.api.jewelry.security.JwtTokenProvider;
import com.api.jewelry.service.OrderService;
import com.api.jewelry.service.UserNotificationsService;
import com.api.jewelry.shared.GenerateRandomString;
import com.api.jewelry.ui.model.request.OrderDetailRequestModel;
import com.api.jewelry.ui.model.request.OrderItemRequestModel;
import com.api.jewelry.ui.model.request.OrderItemStatusRequestModel;
import com.api.jewelry.ui.model.request.SearchRequestModel;
import com.api.jewelry.ui.model.request.UploadOrderDocumentRequestModel;
import com.api.jewelry.ui.model.request.UserNotificationsRequestModel;
import com.api.jewelry.ui.model.response.OrderDetailResponseModel;
import com.api.jewelry.ui.model.response.OrderItemResponseModel;
import com.api.jewelry.ui.model.response.OrderItemStatusResponseModel;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	InventoryRepository inventoryRepository;
	
	@Autowired
	InventoryCostPriceListRepository inventoryCostPriceListRepository;
	
	@Autowired
	TransactionRepository inventoryTransactionDetailRepository;
	
	@Autowired
	GenerateRandomString generateRandomString;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	UserNotificationsService userNotificationsService;

    @Autowired
    private JwtTokenProvider tokenProvider;

	@Autowired
	EntityManager entityManager;
	
	@Autowired
	OrderItemRepository orderItemRepository;

	@Autowired
	InventorySellPriceListRepository inventorySellPriceListRepository;
	
	private String rootDirectory = "src";

	private String uploadDir = rootDirectory + "/uploadedOrderDocuments/";

	Logger LOGGER = Logger.getLogger(OrderServiceImpl.class.getName());

	@Override
	public OrderDetailResponseModel createOrder(OrderDetailRequestModel orderDetail) {
		
		OrderDetailResponseModel returnValue = new OrderDetailResponseModel();
		OrderEntity newOrder = new OrderEntity();
		BeanUtils.copyProperties(orderDetail, newOrder);

		List<OrderItemEntity> orderItems = new ArrayList<>();		
	    for(OrderItemRequestModel item : orderDetail.getOrderItems()) {
	    	OrderItemEntity newOrderItemEntity = new OrderItemEntity(); 
	    	BeanUtils.copyProperties(item, newOrderItemEntity); 
			InventoryEntity inventoryEntity=inventoryRepository.findByInventoryId(item.getInventoryId());
//	    	if(inventoryEntity.getAvailableQuantity() < item.getOrderQuantity()) {
////	    		double unavailableQuantity = item.getOrderQuantity() - inventoryEntity.getAvailableQuantity();
////	    		newOrderItemEntity.setOrderQuantity(item.getOrderQuantity() - unavailableQuantity);
////	    		newOrderItemEntity.setPreOrderQuantity(item.getPreOrderQuantity() + unavailableQuantity);
//	    	}
	    	orderItems.add(newOrderItemEntity);
	    }
		newOrder.setOrderItems(orderItems);
		
		String orderNumber = "";
		boolean orderIdUnique = false;
		while(!orderIdUnique) {
			orderNumber = generateRandomString.generateOrderNumber(7);
			if (orderRepository.findByOrderNumber(orderNumber) == null) {
				orderIdUnique = true;
			}
			
		}
		newOrder.setOrderNumber(orderNumber);
		//		newOrder.setPaymentStatus("Unpaid");
		//		newOrder.setPreOrderpaymentStatus("Unpaid");
		newOrder.setOrderDateTime(Instant.now());
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		newOrder.setCreatedBy(auth.getName());
		OrderEntity savedOrderEntity = orderRepository.save(newOrder);
	
		
		List<OrderItemEntity> orderItemEntities = orderItemRepository.findByOrderId(savedOrderEntity.getOrderId());
		for (OrderItemEntity orderItemEntity : orderItemEntities) {
			InventoryEntity inventoryEntity=inventoryRepository.findByInventoryId(orderItemEntity.getInventoryId());
//			inventoryEntity.setAvailableQuantity(inventoryEntity.getAvailableQuantity() - orderItemEntity.getOrderQuantity());
			inventoryRepository.save(inventoryEntity);
			
			TransactionEntity inventoryTransactionDetailEntity=new TransactionEntity();
			inventoryTransactionDetailEntity.setInventoryId(orderItemEntity.getInventoryId());
			inventoryTransactionDetailEntity.setType("Out");
			inventoryTransactionDetailEntity.setTransactionTime(Instant.now());
	    	inventoryTransactionDetailRepository.save(inventoryTransactionDetailEntity);
		}
		
		BeanUtils.copyProperties(savedOrderEntity, returnValue);
		
		returnValue.setOrderItems(this.getOrderItemResponseModels(savedOrderEntity));
		return returnValue;
	}

	@Override
	public OrderDetailResponseModel getOrderByOrderId(long orderId) {
		
		OrderDetailResponseModel returnValue = new OrderDetailResponseModel();
		OrderEntity orderEntity = orderRepository.findByOrderId(orderId);
		if (orderEntity == null)
			throw new AppException("Order not found.");
		
		BeanUtils.copyProperties(orderEntity, returnValue);
		UserEntity userEntity = userRepository.findByUserId(orderEntity.getUserId());
		if(userEntity != null) {
	    	String fullName = userEntity.getFirstName() + " " + userEntity.getLastName();
	    	returnValue.setFullName(fullName);
    	}
		List<OrderItemEntity> orderItemEntities = orderItemRepository.findByOrderIdAndIsDeleted(orderId, false);
		List<OrderItemResponseModel> orderItemsResponseModel = new ArrayList<OrderItemResponseModel>();
		for (OrderItemEntity orderItemEntity : orderItemEntities) {
			OrderItemResponseModel orderItemResponseModel = new OrderItemResponseModel();
			BeanUtils.copyProperties(orderItemEntity, orderItemResponseModel);
			InventoryEntity inventoryEntity=inventoryRepository.findByInventoryId(orderItemEntity.getInventoryId());
			if(inventoryEntity!=null)
//				orderItemResponseModel.setInventoryGenericName(inventoryEntity.getInventoryGenericName());
			orderItemsResponseModel.add(orderItemResponseModel);
		}
		returnValue.setOrderItems(orderItemsResponseModel);
		
		returnValue.setCreatedDateTime(Instant.now());
		returnValue.setOrderedDateTime(orderEntity.getOrderDateTime());
		returnValue.setOrderItems(this.getOrderItemResponseModels(orderEntity));
		return returnValue;
	}

	@Override
	public List<OrderDetailResponseModel> getOrders(SearchRequestModel searchDetail, int page, int limit) {
		List<OrderDetailResponseModel> returnValue = new ArrayList<>();
	    
	    if(page > 0) page = page - 1;

		String fromDate = searchDetail.getFromDate().trim().toLowerCase();
		String toDate = searchDetail.getToDate().trim().toLowerCase();
		Integer orderStatusTypeId = searchDetail.getOrderStatusTypeId();
	    Instant toDateInstant = null;
		Instant fromDateInstant = null;

		if (!fromDate.equals("")) {
			LocalDate startDateInLocal = LocalDate.parse(fromDate.split("\\s+")[0]);
			fromDateInstant = startDateInLocal.atStartOfDay(ZoneId.systemDefault()).toInstant();
		}else {
			LocalDate startDateInLocal = LocalDate.now().minusYears(1000);
			fromDateInstant = startDateInLocal.atStartOfDay(ZoneId.systemDefault()).toInstant();
		}

		if (!toDate.equals("")) {
			LocalDate endDateInLocal = LocalDate.parse(toDate.split("\\s+")[0]);
			toDateInstant = endDateInLocal.atStartOfDay(ZoneId.systemDefault()).toInstant();
		}else {
			LocalDate endDateInLocal = LocalDate.now().plusYears(1000);
			toDateInstant = endDateInLocal.atStartOfDay(ZoneId.systemDefault()).toInstant();			
		}

		long totalRows = 0;
	    PageRequest.of(page, limit,Sort.by("orderId").descending());
	    
	    Integer[] orderTypeIds;
	    if(searchDetail.getOrderTypeId()!=null && searchDetail.getOrderTypeId()!=0)
	    	orderTypeIds=new Integer[]{searchDetail.getOrderTypeId()};
	 
	       
	    List<Object> results = entityManager.createQuery(
				"SELECT DISTINCT o.orderId FROM orders o WHERE o.orderTypeId in (:orderTypeIds) AND o.isDeleted=false AND o.createdAt>=:fromDateInstant AND o.createdAt<:toDateInstant ORDER BY o.orderId DESC",
				Object.class).setParameter("fromDateInstant", fromDateInstant).setParameter("toDateInstant", toDateInstant).setParameter("orderTypeIds", Arrays.asList())
			.setFirstResult((page) * limit).setMaxResults(limit).getResultList();

		totalRows = (long) entityManager.createQuery(
				"SELECT count(*) FROM orders o WHERE o.orderTypeId in (:orderTypeIds) AND o.isDeleted=false AND o.createdAt>=:fromDateInstant AND o.createdAt<:toDateInstant").setParameter("fromDateInstant", fromDateInstant).setParameter("toDateInstant", toDateInstant).setParameter("orderTypeIds", Arrays.asList()).getSingleResult();
		  
		List<Long> orderIds = new ArrayList<>();		
		for(Object result: results) {
			long orderId = (Long)result;
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			UserEntity userEntity = userRepository.findByUserId(auth.getName());
		
			orderIds.add(orderId);
		}
	    List<OrderEntity> orderEntities = orderRepository.findByOrderIdInAndIsDeletedOrderByOrderIdDesc(orderIds, false);
	    List<OrderEntity> filteredOrderEntities;
	    if(searchDetail.getOrderStatusTypeId() == null || searchDetail.getOrderStatusTypeId() == 0)
	    	filteredOrderEntities= orderEntities;	    
		return returnValue;
	}

	@Override
	public OrderItemStatusRequestModel sellOrders(OrderItemStatusRequestModel sellOrder) {
		OrderItemStatusRequestModel returnValue = new OrderItemStatusRequestModel();
	
		return returnValue;
	}

	@Override
	public List<OrderDetailResponseModel> searchOrders(SearchRequestModel searchkeyDetail, int page, int limit) {
		
		searchkeyDetail.getSearchKey();
		List<OrderDetailResponseModel> returnValue = new ArrayList<>();
//	    
	    if(page > 0) page = page - 1;
		return returnValue;
	}

	@Override
	public OrderDetailResponseModel getOrdersByOrderNumber(String orderNumber, Integer orderTypeId) {
		OrderDetailResponseModel returnValue = new OrderDetailResponseModel();

		OrderEntity orderEntity;
//		if(orderTypeId !=0 && orderTypeId != null)
//			orderEntity = orderRepository.findByOrderNumberAndOrderTypeIdAndIsDeleted(orderNumber, orderTypeId, false);
//		else
			orderEntity = orderRepository.findByOrderNumberAndIsDeleted(orderNumber, false);

		if(orderEntity == null) throw new AppException("Order not found.");
		
		BeanUtils.copyProperties(orderEntity, returnValue);
		
		UserEntity userEntity = userRepository.findByUserId(orderEntity.getUserId());
		if(userEntity != null) {
	    	String fullName = userEntity.getFirstName() + " " + userEntity.getLastName();
	    	returnValue.setFullName(fullName);
    	}
    	

		
		returnValue.setOrderItems(this.getOrderItemResponseModels(orderEntity));
		
    	return returnValue;
	}

	@Override
	public List<OrderDetailResponseModel> getMyOrderHistory(String userId, int page, int limit) {
		
		List<OrderDetailResponseModel> returnValue = new ArrayList<>();
	    
	    if(page > 0) page = page - 1;

	    Pageable pageableRequest = PageRequest.of(page, limit,Sort.by("orderId").descending());
	    Page<OrderEntity> orderPage = orderRepository.findByUserId(userId, pageableRequest);
	    int totalPages = orderPage.getTotalPages();
	    List<OrderEntity> orderEntities = orderPage.getContent();
	    for(OrderEntity orderEntity : orderEntities) {
	    	OrderDetailResponseModel orderDetailResponseModel = new OrderDetailResponseModel();
	    	BeanUtils.copyProperties(orderEntity, orderDetailResponseModel);
	    	if(returnValue.size() == 0) {
	    		orderDetailResponseModel.setTotalPages(totalPages);
	    	}
		
	    	orderDetailResponseModel.setOrderItems(this.getOrderItemResponseModels(orderEntity));
			returnValue.add(orderDetailResponseModel);
		}
		return returnValue;
	}
	
	public ResponseEntity<byte[]>  downloadPDF(String filename) {
		byte[] contents;
	    try {
	      contents = Files.readAllBytes(Paths.get(uploadDir+""+filename));
	    
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_PDF);
	        // Here you have to set the actual filename of your pdf
	        headers.setContentDispositionFormData(filename, filename);
	        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
	        ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
	        return response;
	      
	    } catch (IOException e) {
	      // TODO Auto-generated catch block
	      e.printStackTrace();
	      throw new AppException("Document not found");
	    }

	  }

	private List<OrderItemResponseModel> getOrderItemResponseModels(OrderEntity orderEntity) {
    	List<OrderItemResponseModel> orderItemResponseModels = new ArrayList<>();
    	for(OrderItemEntity orderItemEntity: orderEntity.getOrderItems()) {
    		OrderItemResponseModel orderItemResponseModel = new OrderItemResponseModel();
    		BeanUtils.copyProperties(orderItemEntity, orderItemResponseModel);
			InventoryEntity inventoryEntity = inventoryRepository.findByInventoryIdAndIsDeleted(orderItemEntity.getInventoryId(), false);
    		if(inventoryEntity!=null)
//    			orderItemResponseModel.setInventoryGenericName(inventoryEntity.getInventoryGenericName());
    	    		orderItemResponseModels.add(orderItemResponseModel);
    	}
    	
    	return orderItemResponseModels;
	}
}
